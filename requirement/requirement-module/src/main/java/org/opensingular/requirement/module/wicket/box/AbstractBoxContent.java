/*
 *
 *  * Copyright (C) 2016 Singular Studios (a.k.a Atom Tecnologia) - www.opensingular.com
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  * http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package org.opensingular.requirement.module.wicket.box;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.opensingular.flow.persistence.entity.ModuleEntity;
import org.opensingular.lib.commons.lambda.IConsumer;
import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.lib.wicket.util.datatable.BSDataTable;
import org.opensingular.lib.wicket.util.datatable.BSDataTableBuilder;
import org.opensingular.lib.wicket.util.datatable.BaseDataProvider;
import org.opensingular.lib.wicket.util.datatable.column.BSActionColumn;
import org.opensingular.lib.wicket.util.resource.DefaultIcons;
import org.opensingular.requirement.commons.form.FormAction;
import org.opensingular.requirement.commons.persistence.filter.QuickFilter;
import org.opensingular.requirement.commons.service.RequirementService;
import org.opensingular.requirement.commons.service.dto.BoxConfigurationData;
import org.opensingular.requirement.commons.service.dto.FormDTO;
import org.opensingular.requirement.commons.service.dto.RequirementDefinitionDTO;
import org.opensingular.requirement.commons.wicket.view.behavior.SingularJSBehavior;
import org.opensingular.requirement.commons.wicket.view.template.MenuService;

import static org.opensingular.lib.wicket.util.util.WicketUtils.$b;

/**
 * Classe base para construição de caixas do servidor de petições
 */
public abstract class AbstractBoxContent<T extends Serializable> extends Panel implements Loggable {

    public static final int DEFAULT_ROWS_PER_PAGE = 15;
    private static final long serialVersionUID = -3611649597709058163L;


    @Inject
    protected RequirementService<?, ?> requirementService;

    @Inject
    @SpringBean(required = false)
    protected MenuService menuService;

    /**
     * Tabela de registros
     */
    protected BSDataTable<T, String> table;
    /**
     * Confirmation Form
     */

    private String moduleCod;
    private String menu;
    private List<RequirementDefinitionDTO> processes;
    private List<FormDTO> forms;
    /**
     * Form padrão
     */
    private Form<?> form = new Form<>("form");
    /**
     * Filtro Rapido
     */
    private TextField<String> filtroRapido = new TextField<>("filtroRapido", new Model<>());
    /**
     * Botão de pesquisa do filtro rapido
     */
    private AjaxButton pesquisarButton = new AjaxButton("pesquisar") {
        @Override
        protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
            super.onSubmit(target, form);
            onFiltroRapido(filtroRapido.getModel(), target);
        }
    };

    private IModel<T> dataModel = new Model<>();

    private WebMarkupContainer confirmModalWrapper = new WebMarkupContainer("confirmModalWrapper");

    private ModuleEntity module;

    public AbstractBoxContent(String id, String moduleCod, String menu) {
        super(id);
        this.moduleCod = moduleCod;
        this.menu = menu;
    }

    public IModel<T> getDataModel() {
        return dataModel;
    }


    protected String getModuleCod() {
        return moduleCod;
    }

    public ModuleEntity getModule() {
        return module;
    }

    protected Component buildNewRequirementButton(String id) {
        return new WebMarkupContainer(id);
    }

    protected Component buildBeforeTableContainer(String id) {
        Component c = new WebMarkupContainer(id);
        c.setVisible(false);
        return c;
    }

    protected Component buildAfterTableContainer(String id) {
        Component c = new WebMarkupContainer(id);
        c.setVisible(false);
        return c;
    }

    protected abstract void appendPropertyColumns(BSDataTableBuilder<T, String, IColumn<T, String>> builder);

    /**
     * @return Um par String e Boolean representando a propriedade de ordenação e se deve ser ascendente respectivamente.
     */
    protected abstract Pair<String, SortOrder> getSortProperty();

    protected abstract void onDelete(T peticao);

    protected void appendActionColumns(BSDataTableBuilder<T, String, IColumn<T, String>> builder) {
        BSActionColumn<T, String> actionColumn = new BSActionColumn<>(getMessage("label.table.column.actions"));
        appendEditAction(actionColumn);
        appendViewAction(actionColumn);
        appendDeleteAction(actionColumn);
        builder.appendColumn(actionColumn);
    }

    protected void appendEditAction(BSActionColumn<T, String> actionColumn) {
        actionColumn.appendStaticAction(getMessage("label.table.column.edit"), DefaultIcons.PENCIL_SQUARE, this::createEditionLink);
    }

    protected void appendViewAction(BSActionColumn<T, String> actionColumn) {
        actionColumn.appendStaticAction(getMessage("label.table.column.view"), DefaultIcons.EYE, this::createVisualizationLink);
    }

    protected void appendDeleteAction(BSActionColumn<T, String> actionColumn) {
        actionColumn.appendAction(getMessage("label.table.column.delete"), DefaultIcons.MINUS, this::deleteSelected);
    }

    protected BSDataTable<T, String> createTable(BSDataTableBuilder<T, String, IColumn<T, String>> builder) {
        appendPropertyColumns(builder);
        appendActionColumns(builder);
        builder.setRowsPerPage(getRowsPerPage());
        return builder.setRowsPerPage(getRowsPerPage()).build(SingularJSBehavior.SINGULAR_JS_BEAHAVIOR_UPDATE_REGION);
    }

    protected WebMarkupContainer createEditionLink(String id, IModel<T> requirementModel) {
        return createLink(id, requirementModel, FormAction.FORM_FILL);
    }

    protected WebMarkupContainer createExigencyLink(String id, IModel<T> requirementModel) {
        return createLink(id, requirementModel, FormAction.FORM_FILL);
    }

    protected WebMarkupContainer createVisualizationLink(String id, IModel<T> requirementModel) {
        return createLink(id, requirementModel, FormAction.FORM_VIEW);
    }

    protected abstract WebMarkupContainer createLink(String id, IModel<T> requirementModel, FormAction formAction);

    protected Map<String, String> getCreateLinkParameters(T requirement) {
        return Collections.emptyMap();
    }

    protected BoxContentConfirmModal<T> createModalDeleteBorder(IConsumer<T> action) {
        return new BoxContentDeleteConfirmModal<T>(dataModel) {
            @Override
            protected void onConfirm(AjaxRequestTarget target) {
                action.accept(dataModel.getObject());
                dataModel.setObject(null);
                target.add(table);
            }
        };
    }

    private void deleteSelected(AjaxRequestTarget target, IModel<T> model) {
        dataModel = model;
        showConfirm(target, createModalDeleteBorder(this::onDelete));
    }

    protected void showConfirm(AjaxRequestTarget target, BoxContentConfirmModal<T> boxContentConfirmModal) {
        confirmModalWrapper.addOrReplace(boxContentConfirmModal);
        boxContentConfirmModal.show(target);
        target.add(confirmModalWrapper);
    }

    protected TextField<String> getFiltroRapido() {
        return filtroRapido;
    }

    protected AjaxButton getPesquisarButton() {
        return pesquisarButton;
    }

    public String getFiltroRapidoModelObject() {
        return filtroRapido.getModelObject();
    }

    protected void onFiltroRapido(IModel<String> model, AjaxRequestTarget target) {
        target.add(table);
    }

    protected Integer getRowsPerPage() {
        return DEFAULT_ROWS_PER_PAGE;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        module = requirementService.findByModuleCod(getModuleCod());

        BSDataTableBuilder<T, String, IColumn<T, String>> builder = new BSDataTableBuilder<>(createDataProvider());
        builder.setStripedRows(false).setBorderedTable(false);
        table = createTable(builder);
        table.add($b.classAppender("worklist"));

        queue(form.add(filtroRapido, pesquisarButton, buildNewRequirementButton("newButtonArea")));

        form.add(new HelpFilterBoxPanel("help")
                .configureBody(configureSearchHelpBody()));

        queue(buildBeforeTableContainer("beforeTableContainer"));
        queue(table);
        queue(buildAfterTableContainer("afterTableContainer"));
        queue(confirmModalWrapper.add(new WebMarkupContainer("confirmationModal")));
        if (getMenu() != null) {
            if (menuService != null) {
                BoxConfigurationData boxConfigurationMetadata = menuService.getMenuByLabel(getMenu());
                setProcesses(Optional.ofNullable(boxConfigurationMetadata).map(BoxConfigurationData::getProcesses).orElse(new ArrayList<>(0)));
                setForms(Optional.ofNullable(boxConfigurationMetadata).map(BoxConfigurationData::getForms).orElse(new ArrayList<>(0)));
            }
            if (CollectionUtils.isEmpty(getProcesses())) {
                getLogger().warn("!! NENHUM PROCESSO ENCONTRADO PARA A MONTAGEM DO MENU !!");
            }
        }
    }

    /**
     * Method to be override for change the body of the help inside the input of the fast search.
     * This String can have html tags, the body is configurated to scape html false.
     *
     * @return String for the body.
     */
    protected String configureSearchHelpBody() {
        return null;
    }

    protected BaseDataProvider<T, String> createDataProvider() {
        BaseDataProvider<T, String> dataProvider = new BaseDataProvider<T, String>() {
            @Override
            public long size() {
                if (getProcessesNames().isEmpty()) {
                    return 0;
                }
                return countQuickSearch(newFilter(), getProcessesNames(), getFormNames());
            }

            @Override
            public Iterator<? extends T> iterator(int first, int count, String sortProperty,
                    boolean ascending) {
                QuickFilter quickFilter = newFilter()
                        .withFirst(first)
                        .withCount(count)
                        .withSortProperty(sortProperty)
                        .withAscending(ascending);

                return quickSearch(quickFilter, getProcessesNames(), getFormNames()).iterator();
            }

            private List<String> getProcessesNames() {
                if (getProcesses() == null) {
                    return Collections.emptyList();
                } else {
                    return getProcesses()
                            .stream()
                            .map(RequirementDefinitionDTO::getAbbreviation)
                            .collect(Collectors.toList());
                }
            }

            private List<String> getFormNames() {
                if (getProcesses() == null) {
                    return Collections.emptyList();
                } else {
                    return getProcesses()
                            .stream()
                            .map(RequirementDefinitionDTO::getFormName)
                            .collect(Collectors.toList());
                }
            }
        };
        Pair<String, SortOrder> sort = getSortProperty();
        if (sort != null) {
            dataProvider.setSort(sort.getLeft(), sort.getRight());
        }
        return dataProvider;
    }

    protected QuickFilter newFilter() {
        return newFilterBasic();
    }

    protected abstract QuickFilter newFilterBasic();

    protected abstract List<T> quickSearch(QuickFilter filter, List<String> flowDefinitionAbbreviation, List<String> formNames);

    protected abstract long countQuickSearch(QuickFilter filter, List<String> processesNames, List<String> formNames);

    public List<RequirementDefinitionDTO> getProcesses() {
        return processes;
    }

    public void setProcesses(List<RequirementDefinitionDTO> processes) {
        this.processes = processes;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public List<FormDTO> getForms() {
        return forms;
    }

    public void setForms(List<FormDTO> forms) {
        this.forms = forms;
    }

    protected StringResourceModel getMessage(String prop) {
        return new StringResourceModel(prop.trim(), this, null);
    }
}
