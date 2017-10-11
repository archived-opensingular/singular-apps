/*
 * Copyright (C) 2016 Singular Studios (a.k.a Atom Tecnologia) - www.opensingular.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.opensingular.server.core.wicket.box;

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
import org.opensingular.server.commons.exception.SingularServerException;
import org.opensingular.server.commons.form.FormAction;
import org.opensingular.server.commons.persistence.filter.QuickFilter;
import org.opensingular.server.commons.service.PetitionService;
import org.opensingular.server.commons.service.dto.BoxConfigurationData;
import org.opensingular.server.commons.service.dto.FormDTO;
import org.opensingular.server.commons.service.dto.ProcessDTO;
import org.opensingular.server.commons.wicket.SingularSession;
import org.opensingular.server.commons.wicket.view.template.MenuService;

import javax.inject.Inject;
import java.io.Serializable;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import static org.opensingular.lib.wicket.util.util.WicketUtils.$b;

/**
 * Classe base para construição de caixas do servidor de petições
 */
public abstract class AbstractBoxContent<T extends Serializable> extends Panel implements Loggable {

    public static final int DEFAULT_ROWS_PER_PAGE = 15;
    private static final long serialVersionUID = -3611649597709058163L;

    @Inject
    protected PetitionService<?, ?> petitionService;

    @Inject
    @SpringBean(required = false)
    protected MenuService menuService;

    /**
     * Tabela de registros
     */
    protected BSDataTable<T, String> tabela;
    /**
     * Confirmation Form
     */

    private String moduleCod;
    private String menu;
    private List<ProcessDTO> processes;
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

    protected String getBaseUrl() {
        return getModuleContext() + SingularSession.get().getServerContext().getUrlPath();
    }

    protected String getModuleCod() {
        return moduleCod;
    }

    public ModuleEntity getModule() {
        return module;
    }

    protected Component buildNewPetitionButton(String id) {
        return new WebMarkupContainer(id);
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
        actionColumn.appendStaticAction(getMessage("label.table.column.edit"), DefaultIcons.PENCIL_SQUARE, this::criarLinkEdicao);
    }

    protected void appendViewAction(BSActionColumn<T, String> actionColumn) {
        actionColumn.appendStaticAction(getMessage("label.table.column.view"), DefaultIcons.EYE, this::criarLinkVisualizacao);
    }

    protected void appendDeleteAction(BSActionColumn<T, String> actionColumn) {
        actionColumn.appendAction(getMessage("label.table.column.delete"), DefaultIcons.MINUS, this::deleteSelected);
    }

    protected BSDataTable<T, String> construirTabela(BSDataTableBuilder<T, String, IColumn<T, String>> builder) {
        appendPropertyColumns(builder);
        appendActionColumns(builder);
        builder.setRowsPerPage(getRowsPerPage());
        return builder.setRowsPerPage(getRowsPerPage()).build("tabela");
    }

    protected WebMarkupContainer criarLinkEdicao(String id, IModel<T> peticao) {
        return criarLink(id, peticao, FormAction.FORM_FILL);
    }

    protected WebMarkupContainer criarLinkExigencia(String id, IModel<T> peticao) {
        return criarLink(id, peticao, FormAction.FORM_FILL);
    }

    protected WebMarkupContainer criarLinkVisualizacao(String id, IModel<T> peticao) {
        return criarLink(id, peticao, FormAction.FORM_VIEW);
    }

    protected abstract WebMarkupContainer criarLink(String id, IModel<T> peticao, FormAction formAction);

    protected Map<String, String> getCriarLinkParameters(T peticao) {
        return Collections.emptyMap();
    }

    protected BoxContentConfirmModal<T> construirModalDeleteBorder(IConsumer<T> action) {
        return new BoxContentDeleteConfirmModal<T>(dataModel) {
            @Override
            protected void onConfirm(AjaxRequestTarget target) {
                action.accept(dataModel.getObject());
                dataModel.setObject(null);
                target.add(tabela);
            }
        };
    }

    private void deleteSelected(AjaxRequestTarget target, IModel<T> model) {
        dataModel = model;
        showConfirm(target, construirModalDeleteBorder(this::onDelete));
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
        target.add(tabela);
    }

    protected Integer getRowsPerPage() {
        return DEFAULT_ROWS_PER_PAGE;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        module = petitionService.findByModuleCod(getModuleCod());

        BSDataTableBuilder<T, String, IColumn<T, String>> builder = new BSDataTableBuilder<>(criarDataProvider());
        builder.setStripedRows(false).setBorderedTable(false);
        tabela = construirTabela(builder);
        tabela.add($b.classAppender("worklist"));

        queue(form.add(filtroRapido, pesquisarButton, buildNewPetitionButton("newButtonArea")));
        queue(tabela);
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

    protected BaseDataProvider<T, String> criarDataProvider() {
        BaseDataProvider<T, String> dataProvider = new BaseDataProvider<T, String>() {
            @Override
            public long size() {
                if (getProcessesNames().isEmpty()) {
                    return 0;
                }
                return countQuickSearch(novoFiltro(), getProcessesNames(), getFormNames());
            }

            @Override
            public Iterator<? extends T> iterator(int first, int count, String sortProperty,
                                                  boolean ascending) {
                QuickFilter filtroRapido = novoFiltro()
                        .withFirst(first)
                        .withCount(count)
                        .withSortProperty(sortProperty)
                        .withAscending(ascending);

                return quickSearch(filtroRapido, getProcessesNames(), getFormNames()).iterator();
            }

            private List<String> getProcessesNames() {
                if (getProcesses() == null) {
                    return Collections.emptyList();
                } else {
                    return getProcesses()
                            .stream()
                            .map(ProcessDTO::getAbbreviation)
                            .collect(Collectors.toList());
                }
            }

            private List<String> getFormNames() {
                if (getProcesses() == null) {
                    return Collections.emptyList();
                } else {
                    return getProcesses()
                            .stream()
                            .map(ProcessDTO::getFormName)
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


    public String getModuleContext() {
        final String groupConnectionURL = getModule().getConnectionURL();
        try {
            final String path = new URL(groupConnectionURL).getPath();
            int indexOf = path.indexOf('/', 1);
            if(indexOf > 0) {
                return path.substring(0, indexOf);
            }
            return groupConnectionURL;
        } catch (Exception e) {
            throw SingularServerException.rethrow(String.format("Erro ao tentar fazer o parse da URL: %s", groupConnectionURL), e);
        }
    }

    protected QuickFilter novoFiltro() {
        return montarFiltroBasico();
    }

    protected abstract QuickFilter montarFiltroBasico();

    protected abstract List<T> quickSearch(QuickFilter filtro, List<String> siglasProcesso, List<String> formNames);

    protected abstract long countQuickSearch(QuickFilter filter, List<String> processesNames, List<String> formNames);

    public List<ProcessDTO> getProcesses() {
        return processes;
    }

    public void setProcesses(List<ProcessDTO> processes) {
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
