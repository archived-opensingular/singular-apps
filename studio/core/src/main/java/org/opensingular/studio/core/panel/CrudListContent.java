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

package org.opensingular.studio.core.panel;

import de.alpharogroup.wicket.js.addon.toastr.ToastrType;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.opensingular.form.SInstance;
import org.opensingular.form.persistence.FormKey;
import org.opensingular.form.wicket.component.BFModalWindow;
import org.opensingular.form.wicket.component.SingularButton;
import org.opensingular.form.wicket.enums.ViewMode;
import org.opensingular.form.wicket.panel.SingularFormPanel;
import org.opensingular.lib.commons.ui.Icon;
import org.opensingular.lib.wicket.util.ajax.ActionAjaxLink;
import org.opensingular.lib.wicket.util.datatable.BSDataTableBuilder;
import org.opensingular.lib.wicket.util.datatable.column.BSActionPanel;
import org.opensingular.lib.wicket.util.modal.BSModalBorder;
import org.opensingular.lib.wicket.util.resource.DefaultIcons;
import org.opensingular.lib.wicket.util.util.WicketUtils;
import org.opensingular.studio.core.definition.BasicStudioTableDataProvider;
import org.opensingular.studio.core.definition.StudioDefinition;
import org.opensingular.studio.core.definition.StudioTableDataProvider;
import org.opensingular.studio.core.definition.StudioTableDefinition;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class CrudListContent extends CrudShellContent {

    private IModel<Icon>            iconModel          = new Model<>();
    private IModel<String>          titleModel         = new Model<>();
    private List<HeaderRightButton> headerRightButtons = new ArrayList<>();
    private BFModalWindow     modalFilter;
    private SingularFormPanel filterPanel;

    public CrudListContent(CrudShellManager crudShellManager) {
        super(crudShellManager);
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
        tableBuilder.setBorderedTable(false);
        StudioTableDefinition configuredStudioTable = getConfiguredStudioTable();
        configuredStudioTable.getColumns()
                .forEach((name, path) -> tableBuilder.appendPropertyColumn(Model.of(name), path, ins -> ins.getField(path).toStringDisplay()));

        tableBuilder.appendActionColumn("", (BSDataTableBuilder.BSActionColumnCallback<SInstance, String>)
                actionColumn -> configuredStudioTable.getActions().forEach(listAction -> {
                    BSActionPanel.ActionConfig<SInstance> config = newConfig();
                    listAction.configure(config);
                    actionColumn.appendAction(config, (org.opensingular.lib.wicket.util.datatable.IBSAction<SInstance>) (target, model) -> listAction.onAction(target, model, getCrudShellManager()));
                }));

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

    private StudioTableDefinition getConfiguredStudioTable() {
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

            modalFilter.addButton(BSModalBorder.ButtonStyle.CONFIRM, Model.of("Pesquisar"), new SingularButton("btnPesquisar", filterPanel.getInstanceModel()) {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    target.add(CrudListContent.this);
                    modalFilter.hide(target);
                }
            });

            AjaxButton cancelButton = new AjaxButton("btnCancelar") {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
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

    public interface HeaderRightButton extends Serializable {
        void onAction(AjaxRequestTarget target);

        String getLabel();

        String getIcon();

        default boolean isVisible() {
            return true;
        }
    }

    public interface ListAction extends Serializable {
        void configure(BSActionPanel.ActionConfig<SInstance> config);

        void onAction(AjaxRequestTarget target, IModel<SInstance> model, CrudShellManager crudShellManager);
    }

    private static class HeaderRightActions extends ListView<HeaderRightButton> {

        private HeaderRightActions(List<HeaderRightButton> list) {
            super("headerRightActions", list);
        }

        @Override
        protected void populateItem(ListItem<HeaderRightButton> item) {
            item.add(new HeaderRightActionActionAjaxLink(item.getModelObject()));
        }

        @Override
        protected void onInitialize() {
            super.onInitialize();
            setRenderBodyOnly(true);
        }

        private static class HeaderRightActionActionAjaxLink extends ActionAjaxLink<Void> {

            private final HeaderRightButton headerRightButton;

            HeaderRightActionActionAjaxLink(HeaderRightButton headerRightButton) {
                super("headerRightAction");
                this.headerRightButton = headerRightButton;
            }

            @Override
            protected void onAction(AjaxRequestTarget target) {
                headerRightButton.onAction(target);
            }

            @Override
            protected void onInitialize() {
                super.onInitialize();
                Label btnLabel = new Label("headerRightActionLabel", headerRightButton.getLabel());

                WebMarkupContainer btnIcon = new WebMarkupContainer("headerRigthActionIcon") {
                    @Override
                    protected void onComponentTag(ComponentTag tag) {
                        super.onComponentTag(tag);
                        tag.put("class", headerRightButton.getIcon());
                    }
                };

                this.add(btnLabel);
                this.add(btnIcon);
                this.setVisible(headerRightButton.isVisible());
            }

        }
    }

    public static class EditAction implements ListAction {

        private final CrudShellManager crudShellManager;

        public EditAction(CrudShellManager crudShellManager) {
            this.crudShellManager = crudShellManager;
        }

        @Override
        public void configure(BSActionPanel.ActionConfig<SInstance> config) {
            config.iconeModel(Model.of(DefaultIcons.PENCIL));
            config.labelModel(Model.of("Editar"));
        }

        @Override
        public void onAction(AjaxRequestTarget target, IModel<SInstance> model, CrudShellManager crudShellManager) {
            CrudEditContent crudEditContent = this.crudShellManager.makeEditContent(this.crudShellManager.getCrudShellContent(), model);
            this.crudShellManager.replaceContent(target, crudEditContent);
        }
    }

    public static class ViewAction implements ListAction {

        private final CrudShellManager crudShellManager;

        public ViewAction(CrudShellManager crudShellManager) {
            this.crudShellManager = crudShellManager;
        }

        @Override
        public void configure(BSActionPanel.ActionConfig<SInstance> config) {
            config.iconeModel(Model.of(DefaultIcons.EYE));
            config.labelModel(Model.of("Visualizar"));
        }

        @Override
        public void onAction(AjaxRequestTarget target, IModel<SInstance> model, CrudShellManager crudShellManager) {
            CrudEditContent crudEditContent = this.crudShellManager
                    .makeEditContent(this.crudShellManager.getCrudShellContent(), model);
            crudEditContent.setViewMode(ViewMode.READ_ONLY);
            this.crudShellManager.replaceContent(target, crudEditContent);
        }
    }

    public static class DeleteAction implements ListAction {

        private final StudioDefinition studioDefinition;
        private final CrudShellManager crudShellManager;

        public DeleteAction(StudioDefinition studioDefinition, CrudShellManager crudShellManager) {
            this.studioDefinition = studioDefinition;
            this.crudShellManager = crudShellManager;
        }

        @Override
        public void configure(BSActionPanel.ActionConfig<SInstance> config) {
            config.iconeModel(Model.of(DefaultIcons.TRASH));
            config.labelModel(Model.of("Remover"));
        }

        @Override
        public void onAction(AjaxRequestTarget target, IModel<SInstance> model, CrudShellManager crudShellManager) {
            this.crudShellManager.addConfirm("Tem certeza que deseja excluir?", target, (ajaxRequestTarget) -> {
                studioDefinition.getRepository().delete(FormKey.from(model.getObject()));
                this.crudShellManager.addToastrMessage(ToastrType.INFO, "Item excluido com sucesso.");
                this.crudShellManager.update(ajaxRequestTarget);
            });
        }

    }

    private class CreateNewHeaderRightButton implements HeaderRightButton {
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
        public String getIcon() {
            return "fa fa-plus";
        }
    }

    private void createFilterHeaderRigthButton() {
        HeaderRightButton btnFilter = new HeaderRightButton() {
            @Override
            public void onAction(AjaxRequestTarget target) {
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


}