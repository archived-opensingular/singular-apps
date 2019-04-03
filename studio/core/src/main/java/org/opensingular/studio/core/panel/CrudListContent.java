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

package org.opensingular.studio.core.panel;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.opensingular.form.SInstance;
import org.opensingular.form.wicket.component.BFModalWindow;
import org.opensingular.form.wicket.component.SingularValidationButton;
import org.opensingular.form.wicket.panel.SingularFormPanel;
import org.opensingular.form.wicket.util.FormStateUtil;
import org.opensingular.lib.commons.base.SingularException;
import org.opensingular.lib.commons.ui.Icon;
import org.opensingular.lib.wicket.util.datatable.BSDataTableBuilder;
import org.opensingular.lib.wicket.util.datatable.column.BSActionPanel;
import org.opensingular.lib.wicket.util.modal.BSModalBorder;
import org.opensingular.lib.wicket.util.util.WicketUtils;
import org.opensingular.studio.core.definition.BasicStudioTableDataProvider;
import org.opensingular.studio.core.definition.StudioTableDataProvider;
import org.opensingular.studio.core.definition.StudioTableDefinition;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class CrudListContent extends CrudShellContent {

    private IModel<Icon> iconModel = new Model<>();
    private IModel<String> titleModel = new Model<>();
    private List<HeaderRightButton> headerRightButtons = new ArrayList<>();

    private BFModalWindow modalFilter;
    private SingularFormPanel filterPanel;
    protected FormStateUtil.FormState filterState;

    private final CrudListConfig crudListConfig;

    public CrudListContent(CrudShellManager crudShellManager) {
        this(crudShellManager, new CrudListConfig());
    }

    public CrudListContent(CrudShellManager crudShellManager, CrudListConfig crudListConfig) {
        super(crudShellManager);
        this.crudListConfig = crudListConfig;
        addDefaultHeaderRightActions();
    }

    private void addDefaultHeaderRightActions() {
        if (getDefinition().getPermissionStrategy().canCreate()) {
            headerRightButtons.add(new CreateNewHeaderRightButton());
        }
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        addIcon();
        addTitle();
        addPortletHeaderRightButtons();
        setTitle(getDefinition().getTitle());
        createModalFilter();
        createFilterHeaderRigthButton();
        addTable();
    }

    private void addTable() {
        BSDataTableBuilder<SInstance, String, IColumn<SInstance, String>> tableBuilder = new BSDataTableBuilder<>(resolveProvider());

        StudioTableDefinition configuredStudioTable = getConfiguredStudioTable();
        configuredStudioTable
                .getColumns()
                .forEach((name, path) -> tableBuilder.appendPropertyColumn(Model.of(name), path, ins -> ins.getField(path).toStringDisplay()));

        tableBuilder.appendActionColumn("", (BSDataTableBuilder.BSActionColumnCallback<SInstance, String>)
                actionColumn -> configuredStudioTable.getActions().forEach(listAction -> {
                    BSActionPanel.ActionConfig<SInstance> config = newConfig();
                    listAction.configure(config);
                    actionColumn.appendAction(config, (org.opensingular.lib.wicket.util.datatable.IBSAction<SInstance>) (target, model) -> listAction.onAction(target, model, getCrudShellManager()));
                }));

        tableBuilder.setBorderedTable(false);

        if (crudListConfig.getRowsPerPage() != null) {
            tableBuilder.setRowsPerPage(crudListConfig.getRowsPerPage());
        }

        if (crudListConfig.getAdvancedTable() != null) {
            tableBuilder.setAdvancedTable(crudListConfig.getAdvancedTable());
        }

        if (crudListConfig.getBorderedTable() != null) {
            tableBuilder.setBorderedTable(crudListConfig.getBorderedTable());
        }

        if (crudListConfig.getCondensedTable() != null) {
            tableBuilder.setCondensedTable(crudListConfig.getCondensedTable());
        }

        if (crudListConfig.getHoverRows() != null) {
            tableBuilder.setHoverRows(crudListConfig.getHoverRows());
        }

        if (crudListConfig.getStripedRows() != null) {
            tableBuilder.setStripedRows(crudListConfig.getStripedRows());
        }

        add(tableBuilder.build("table"));
    }

    private SortableDataProvider<SInstance, String> resolveProvider() {
        StudioTableDataProvider dataProvider = Optional.ofNullable(getConfiguredStudioTable().getDataProvider()).orElse(new DefaultStudioTableDataProvider());
        if (isFilterDefined()) {
            return new StudioDataProviderAdapter(dataProvider, formKey -> getFormPersistence().load(formKey), filterPanel.getInstanceModel());
        }
        return new StudioDataProviderAdapter(dataProvider, formKey -> getFormPersistence().load(formKey), null);
    }

    private BSActionPanel.ActionConfig<SInstance> newConfig() {
        BSActionPanel.ActionConfig<SInstance> config = new BSActionPanel.ActionConfig<>();
        config.styleClasses(WicketUtils.$m.ofValue("btn btn-link btn-xs black md-skip studio-action"));
        return config;
    }

    public StudioTableDefinition getConfiguredStudioTable() {
        StudioTableDefinition studioDataTable = new StudioTableDefinition(getDefinition(), getCrudShellManager());
        getDefinition().configureStudioDataTable(studioDataTable);
        return studioDataTable;
    }

    private void addPortletHeaderRightButtons() {
        add(new HeaderRightActions(headerRightButtons));
    }

    private void addTitle() {
        add(new Label("title", titleModel).setRenderBodyOnly(true));
    }

    private void addIcon() {
        add(new StudioIcon("icon", iconModel));
    }

    private CrudListContent setIcon(Icon icon) {
        iconModel.setObject(icon);
        return this;
    }

    private CrudListContent setTitle(String title) {
        titleModel.setObject(title);
        return this;
    }

    public CrudListContent addPorletHeaderRightAction(HeaderRightButton headerRightButton) {
        headerRightButtons.add(headerRightButton);
        return this;
    }

    private void createModalFilter() {
        modalFilter = new BFModalWindow("modalFilter");
        modalFilter.setTitleText(Model.of("Pesquisar"));
        modalFilter.setSize(BSModalBorder.Size.LARGE);

        if (isFilterDefined()) {
            filterPanel = new SingularFormPanel("filterPanel", getCrudShellManager().getStudioDefinition().getFilterType());
            filterPanel.setNested(true);
            modalFilter.setBody(filterPanel);

            modalFilter.addButton(BSModalBorder.ButtonStyle.CONFIRM, Model.of("Pesquisar"), new SingularValidationButton("btnPesquisar", filterPanel.getInstanceModel()) {
                @Override
                protected void onValidationSuccess(AjaxRequestTarget target, Form<?> form, IModel<? extends SInstance> instanceModel) {
                    target.add(CrudListContent.this);
                    modalFilter.hide(target);
                }

                @Override
                protected void onValidationError(AjaxRequestTarget target, Form<?> form, IModel<? extends SInstance> instanceModel) {
                    super.onValidationError(target, form, instanceModel);
                    modalFilter.hide(target);
                    modalFilter.show(target);
                }
            });

            AjaxButton limparButton = new AjaxButton("btnLimpar") {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    filterPanel.getInstanceModel().getObject().clearInstance();
                    form.clearInput();
//                    filterPanel.getInstanceModel().getObject().init();
                    target.add(filterPanel);
                }
            };
            limparButton.setDefaultFormProcessing(false);
            modalFilter.addButton(BSModalBorder.ButtonStyle.CANCEL, Model.of("Limpar"), limparButton);

            AjaxButton cancelButton = new AjaxButton("btnCancelar") {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    rollbackFilterState();
                    modalFilter.hide(target);
                }
            };
            cancelButton.setDefaultFormProcessing(false);
            modalFilter.addButton(BSModalBorder.ButtonStyle.CANCEL, Model.of("Cancelar"), cancelButton);
        }

        add(modalFilter);
    }

    private boolean isFilterDefined() {
        return getCrudShellManager().getStudioDefinition().getFilterType() != null;
    }

    private static class HeaderRightActions extends ListView<HeaderRightButton> {

        private HeaderRightActions(List<HeaderRightButton> list) {
            super("headerRightActions", list);
        }

        @Override
        protected void populateItem(ListItem<HeaderRightButton> item) {
            item.setRenderBodyOnly(true);
            item.add(new HeadRightButtonPanel(item.getModelObject()));
        }
    }

    private class CreateNewHeaderRightButton extends HeaderRightAjaxLink {

        @Override
        public void onAction(AjaxRequestTarget target) {
            CrudEditContent crudEditContent = getCrudShellManager()
                    .makeEditContent(getCrudShellManager().getCrudShellContent(), null);
            getCrudShellManager().replaceContent(target, crudEditContent);
        }

        @Override
        public String getLabel() {
            return "Novo";
        }

        @Override
        public String getTitle() {
            return getCrudShellManager().getCrudShellContent().getDefinition().getTitle();
        }

        @Override
        public String getIcon() {
            return "fa fa-plus";
        }
    }

    private void createFilterHeaderRigthButton() {
        HeaderRightButton btnFilter = new HeaderRightAjaxLink() {
            @Override
            public void onAction(AjaxRequestTarget target) {
                saveFilterState();
                modalFilter.show(target);
            }

            @Override
            public String getLabel() {
                return "Filtrar";
            }

            @Override
            public boolean isVisible() {
                return isFilterDefined();
            }

            @Override
            public String getIcon() {
                return "fa fa-search";
            }
        };
        addPorletHeaderRightAction(btnFilter);
    }

    private class DefaultStudioTableDataProvider implements BasicStudioTableDataProvider<SInstance> {

        @Override
        public Iterator<SInstance> iterator(long first, long count) {
            return getFormPersistence().loadAll(first, count).iterator();
        }

        @Override
        public long size() {
            return getFormPersistence().countAll();
        }
    }

    private void saveFilterState() {
        filterState = FormStateUtil.keepState(filterPanel.getInstance());
    }

    private void rollbackFilterState() {
        try {
            if (filterState != null && filterPanel != null && filterPanel.getInstanceModel().getObject() != null) {
                FormStateUtil.restoreState(filterPanel.getInstanceModel().getObject(), filterState);
            }
        } catch (Exception e) {
            throw SingularException.rethrow(e.getMessage(), e);
        }
    }

    interface HeaderRightButton extends Serializable {

        String getLabel();

        default String getTitle() {
            return getLabel();
        }

        String getIcon();

        default boolean isVisible() {
            return true;
        }

    }
}