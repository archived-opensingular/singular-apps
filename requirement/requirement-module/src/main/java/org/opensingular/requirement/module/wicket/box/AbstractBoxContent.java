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

package org.opensingular.requirement.module.wicket.box;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.opensingular.form.SInstance;
import org.opensingular.form.document.RefType;
import org.opensingular.form.wicket.panel.SingularFormPanel;
import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.lib.wicket.util.datatable.BSDataTable;
import org.opensingular.lib.wicket.util.datatable.BSDataTableBuilder;
import org.opensingular.lib.wicket.util.datatable.BaseDataProvider;
import org.opensingular.requirement.module.box.BoxItemDataMap;
import org.opensingular.requirement.module.box.form.STypeDynamicAdvancedFilter;
import org.opensingular.requirement.module.persistence.filter.BoxFilter;
import org.opensingular.requirement.module.service.FormRequirementService;
import org.opensingular.requirement.module.service.RequirementService;
import org.opensingular.requirement.module.wicket.view.behavior.SingularJSBehavior;
import org.opensingular.requirement.module.workspace.BoxDefinition;

import javax.inject.Inject;
import java.util.Iterator;
import java.util.List;

import static org.opensingular.lib.wicket.util.util.WicketUtils.$b;

/**
 * Classe base para construição de caixas do servidor de petições
 */
@SuppressWarnings("WeakerAccess")
public abstract class AbstractBoxContent extends GenericPanel<BoxItemDataMap> implements Loggable {

    public static final int DEFAULT_ROWS_PER_PAGE = 15;

    private static final long serialVersionUID = -3611649597709058163L;

    @Inject
    protected RequirementService requirementService;

    @Inject
    protected FormRequirementService formRequirementService;

    /**
     * Tabela de registros
     */
    protected BSDataTable<BoxItemDataMap, String> table;

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

    private WebMarkupContainer confirmModalWrapper = new WebMarkupContainer("confirmModalWrapper");

    protected IModel<BoxDefinition> boxDefinition;

    private SingularFormPanel advancedFilter;

    public AbstractBoxContent(String id, IModel<BoxDefinition> boxDefinition) {
        super(id, new Model<>());
        this.boxDefinition = boxDefinition;
    }

    protected Component buildNewRequirementButton(String id) {
        return new WebMarkupContainer(id);
    }

    @SuppressWarnings("SameParameterValue")
    protected Component buildBeforeTableContainer(String id) {
        return new EmptyPanel(id);
    }

    @SuppressWarnings("SameParameterValue")
    protected Component buildAfterTableContainer(String id) {
        return new EmptyPanel(id);
    }

    protected abstract void appendPropertyColumns(BSDataTableBuilder<BoxItemDataMap, String, IColumn<BoxItemDataMap, String>> builder);

    /**
     * @return Um par String e Boolean representando a propriedade de ordenação e se deve ser ascendente respectivamente.
     */
    protected abstract Pair<String, SortOrder> getSortProperty();

    protected abstract void appendActionColumns(BSDataTableBuilder<BoxItemDataMap, String, IColumn<BoxItemDataMap, String>> builder);

    protected BSDataTable<BoxItemDataMap, String> createTable(BSDataTableBuilder<BoxItemDataMap, String, IColumn<BoxItemDataMap, String>> builder) {
        appendPropertyColumns(builder);
        appendActionColumns(builder);
        builder.setRowsPerPage(getRowsPerPage());
        return builder.setRowsPerPage(getRowsPerPage()).build(SingularJSBehavior.SINGULAR_JS_BEAHAVIOR_UPDATE_REGION);
    }

    protected void showConfirm(AjaxRequestTarget target, BoxContentConfirmModal<BoxItemDataMap> boxContentConfirmModal) {
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

    @SuppressWarnings("unused")
    protected void onFiltroRapido(IModel<String> model, AjaxRequestTarget target) {
        target.add(table);
    }

    protected Integer getRowsPerPage() {
        return DEFAULT_ROWS_PER_PAGE;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        BSDataTableBuilder<BoxItemDataMap, String, IColumn<BoxItemDataMap, String>> builder = new BSDataTableBuilder<>(createDataProvider());
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

        WebMarkupContainer advancedFilterContainer = new WebMarkupContainer("advancedFilterContainer");
        createAdvancedFilter();
        advancedFilterContainer.add(createClearFilterButton());
        advancedFilterContainer.add(advancedFilter);
        queue(advancedFilterContainer);
    }

    private Component createClearFilterButton() {
        return new AjaxLink<Void>("filter") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                advancedFilter.getInstance().clearInstance();
                target.add(this.getParent());
            }
        };

    }

    private void createAdvancedFilter() {
        advancedFilter = new SingularFormPanel("advancedFilter", this::createDynamicAdvancedFilterInstance);
        advancedFilter.setNested(true);
    }

    private SInstance createDynamicAdvancedFilterInstance(){
        return formRequirementService.createInstance(RefType.of(this::createDynamicAdvancedFilter));
    }

    protected STypeDynamicAdvancedFilter createDynamicAdvancedFilter() {
        STypeDynamicAdvancedFilter sTypeDynamicAdvancedFilter = (STypeDynamicAdvancedFilter) formRequirementService
                .loadRefType(STypeDynamicAdvancedFilter.class).get();
        boxDefinition.getObject().setupDynamicAdvancedFilterType(sTypeDynamicAdvancedFilter);
        return sTypeDynamicAdvancedFilter;
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

    protected BaseDataProvider<BoxItemDataMap, String> createDataProvider() {
        BaseDataProvider<BoxItemDataMap, String> dataProvider = new BaseDataProvider<BoxItemDataMap, String>() {
            @Override
            public long size() {
                return countQuickSearch(newFilter());
            }

            @Override
            public Iterator<? extends BoxItemDataMap> iterator(int first, int count, String sortProperty,
                                                               boolean ascending) {
                BoxFilter boxFilter = newFilter()
                        .first(first)
                        .count(count)
                        .sortProperty(sortProperty)
                        .ascending(ascending);

                return quickSearch(boxFilter).iterator();
            }

        };
        Pair<String, SortOrder> sort = getSortProperty();
        if (sort != null) {
            dataProvider.setSort(sort.getLeft(), sort.getRight());
        }
        return dataProvider;
    }

    protected BoxFilter newFilter() {
        return newFilterBasic().advancedFilterInstance(advancedFilter.getInstance());
    }

    protected abstract BoxFilter newFilterBasic();

    protected abstract List<BoxItemDataMap> quickSearch(BoxFilter filter);

    protected abstract long countQuickSearch(BoxFilter filter);

    protected StringResourceModel getMessage(String prop) {
        return new StringResourceModel(prop.trim(), this, null);
    }
}
