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

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.opensingular.flow.persistence.entity.Actor;
import org.opensingular.lib.commons.lambda.IBiFunction;
import org.opensingular.lib.commons.lambda.IFunction;
import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.lib.wicket.util.datatable.BSDataTableBuilder;
import org.opensingular.lib.wicket.util.datatable.IBSAction;
import org.opensingular.lib.wicket.util.datatable.column.BSActionColumn;
import org.opensingular.lib.wicket.util.datatable.column.BSActionPanel;
import org.opensingular.requirement.module.RequirementDefinition;
import org.opensingular.requirement.module.box.BoxItemDataMap;
import org.opensingular.requirement.module.box.action.ActionAtribuirRequest;
import org.opensingular.requirement.module.box.action.ActionRequest;
import org.opensingular.requirement.module.box.action.ActionResponse;
import org.opensingular.requirement.module.connector.ModuleService;
import org.opensingular.requirement.module.persistence.filter.BoxFilter;
import org.opensingular.requirement.module.persistence.filter.BoxFilterFactory;
import org.opensingular.requirement.module.service.dto.BoxItemAction;
import org.opensingular.requirement.module.service.dto.DatatableField;
import org.opensingular.requirement.module.service.dto.ItemActionType;
import org.opensingular.requirement.module.wicket.buttons.NewRequirementLink;
import org.opensingular.requirement.module.workspace.BoxDefinition;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.opensingular.lib.wicket.util.util.WicketUtils.$b;
import static org.opensingular.lib.wicket.util.util.WicketUtils.$m;

public class BoxContent extends AbstractBoxContent implements Loggable {
    @Inject
    private ModuleService moduleService;

    @Inject
    private BoxFilterFactory boxFilterFactory;

    private Pair<String, SortOrder> sortProperty;

    public BoxContent(String id, IModel<BoxDefinition> boxDefinition) {
        super(id, boxDefinition);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        configureQuickFilter();
    }

    private void configureQuickFilter() {
        getFiltroRapido().setVisible(getBoxDefinitionObject().getItemBox().isShowQuickFilter());
        getPesquisarButton().setVisible(getBoxDefinitionObject().getItemBox().isShowQuickFilter());
    }

    @Override
    public Component buildNewRequirementButton(String id) {
        IModel<LinkedHashSet<Class<? extends RequirementDefinition>>> requirementsModel = new Model<>(new LinkedHashSet<>(getBoxDefinitionObject().getItemBox().getRequirements()));
        if (!requirementsModel.getObject().isEmpty()) {
            return new NewRequirementLink(id, moduleService.getBaseUrl(), getLinkParams(), requirementsModel);
        } else {
            return super.buildNewRequirementButton(id);
        }
    }

    @Override
    protected void appendPropertyColumns(BSDataTableBuilder<BoxItemDataMap, String, IColumn<BoxItemDataMap, String>> builder) {
        for (DatatableField entry : getFieldsDatatable()) {
            builder.appendPropertyColumn($m.ofValue(entry.getKey()), entry.getLabel(), entry.getLabel(), "update-description");
        }
    }

    @Override
    protected void appendActionColumns(BSDataTableBuilder<BoxItemDataMap, String, IColumn<BoxItemDataMap, String>> builder) {
        BSActionColumn<BoxItemDataMap, String> actionColumn = new BSActionColumn<BoxItemDataMap, String>(getMessage("label.table.column.actions")) {
            @Override
            protected void onPopulateActions(IModel<BoxItemDataMap> rowModel, BSActionPanel<BoxItemDataMap> actionPanel) {
                resetActions();
                Set<Map.Entry<String, BoxItemAction>> actions = Optional
                        .ofNullable(rowModel)
                        .map(IModel::getObject)
                        .map(BoxItemDataMap::getActionsMap)
                        .map(Map::entrySet)
                        .orElse(new HashSet<>(0));

                for (Map.Entry<String, BoxItemAction> entry : actions) {
                    BoxItemAction itemAction = entry.getValue();

                    if (itemAction.getType() == ItemActionType.URL_POPUP) {
                        appendStaticAction(
                                $m.ofValue(itemAction.getLabel()),
                                itemAction.getIcon(),
                                linkFunction(itemAction, getLinkParams()),
                                visibleFunction(itemAction),
                                c -> c.styleClasses($m.ofValue("worklist-action-btn")));
                    } else if (itemAction.getType() == ItemActionType.EXECUTE) {
                        appendAction(
                                $m.ofValue(itemAction.getLabel()),
                                itemAction.getIcon(),
                                dynamicLinkFunction(itemAction, getLinkParams()),
                                visibleFunction(itemAction),
                                c -> c.styleClasses($m.ofValue("worklist-action-btn")));
                    }
                }


                super.onPopulateActions(rowModel, actionPanel);
            }

        };

        builder.appendColumn(actionColumn);
    }

    public IBiFunction<String, IModel<BoxItemDataMap>, MarkupContainer> linkFunction(BoxItemAction itemAction, Map<String, String> additionalParams) {
        return (id, boxItemModel) -> {
            String url = moduleService.buildUrlToBeRedirected(boxItemModel.getObject(), itemAction, additionalParams, moduleService.getBaseUrl());
            WebMarkupContainer link = new WebMarkupContainer(id);
            link.add($b.attr("target", String.format("_%s_%s", itemAction.getName(), boxItemModel.getObject().getCod())));
            link.add($b.attr("href", url));
            return link;
        };
    }

    private IBSAction<BoxItemDataMap> dynamicLinkFunction(BoxItemAction itemAction, Map<String, String> additionalParams) {
        if (itemAction.getConfirmation() != null) {
            return (target, model) -> {
                getModel().setObject(model.getObject());
                showConfirm(target, construirModalConfirmationBorder(itemAction, additionalParams));
            };
        } else {
            return (target, model) -> executeDynamicAction(itemAction, additionalParams, model.getObject(), target);
        }
    }

    protected void executeDynamicAction(BoxItemAction itemAction,
                                        Map<String, String> additionalParams,
                                        BoxItemDataMap boxItem, AjaxRequestTarget target) {
        final BoxItemAction boxAction = boxItem.getActionByName(itemAction.getName());
        try {
            callModule(boxAction, additionalParams, buildCallObject(boxAction, boxItem));
        } catch (Exception e) {
            getLogger().error(e.getMessage(), e);
            ((BoxPage) getPage()).addToastrErrorMessage("Não foi possível executar esta ação.");
        } finally {
            target.add(table);
        }
    }

    protected void relocate(BoxItemAction itemAction,
                            Map<String, String> additionalParams, BoxItemDataMap boxItem,
                            AjaxRequestTarget target, Actor actor) {
        final BoxItemAction boxAction = boxItem.getActionByName(itemAction.getName());
        try {
            callModule(itemAction, additionalParams, buildCallAtribuirObject(boxAction, boxItem, actor));
        } catch (Exception e) {
            getLogger().error(e.getMessage(), e);
            ((BoxPage) getPage()).addToastrErrorMessage("Não foi possível executar esta ação.");
        } finally {
            target.add(table);
        }
    }

    protected ActionRequest buildCallAtribuirObject(BoxItemAction boxAction, BoxItemDataMap boxItem, Actor actor) {
        ActionAtribuirRequest actionRequest = new ActionAtribuirRequest();
        actionRequest.setIdUsuario(getBoxPage().getIdUsuario());
        if (actor == null) {
            actionRequest.setEndLastAllocation(true);
        } else {
            actionRequest.setIdUsuarioDestino(actor.getCodUsuario());
        }
        if (boxAction.isUseExecute()) {
            actionRequest.setAction(boxAction);
            actionRequest.setLastVersion(boxItem.getVersionStamp());
        }

        return actionRequest;
    }

    private void callModule(BoxItemAction itemAction, Map<String, String> params, ActionRequest actionRequest) {
        ActionResponse response = moduleService.executeAction(itemAction, params, actionRequest);
        if (response.isSuccessful()) {
            ((BoxPage) getPage()).addToastrSuccessMessage(response.getResultMessage());
        } else {
            ((BoxPage) getPage()).addToastrErrorMessage(response.getResultMessage());
        }
    }

    private ActionRequest buildCallObject(BoxItemAction boxAction, BoxItemDataMap boxItem) {
        ActionRequest actionRequest = new ActionRequest();
        actionRequest.setIdUsuario(getBoxPage().getIdUsuario());
        if (boxAction.isUseExecute()) {
            actionRequest.setAction(boxAction);
            actionRequest.setLastVersion(boxItem.getVersionStamp());
        }

        return actionRequest;
    }

    protected BoxContentConfirmModal<BoxItemDataMap> construirModalConfirmationBorder(BoxItemAction itemAction,
                                                                                      Map<String, String> additionalParams) {
        if (StringUtils.isNotBlank(itemAction.getConfirmation().getSelectEndpoint())) {
            return new BoxContentAllocateModal(itemAction, getModel()) {
                @Override
                protected void onDeallocate(AjaxRequestTarget target) {
                    relocate(itemAction, additionalParams, getModel().getObject(), target, null);
                    target.add(table);
                    atualizarContadores(target);
                }

                @Override
                protected void onConfirm(AjaxRequestTarget target) {
                    relocate(itemAction, additionalParams, getModel().getObject(), target, usersDropDownChoice.getModelObject());
                    target.add(table);
                    atualizarContadores(target);
                }
            };
        } else {
            return new BoxContentConfirmModal<BoxItemDataMap>(itemAction, getModel()) {
                @Override
                protected void onConfirm(AjaxRequestTarget target) {
                    executeDynamicAction(itemAction, additionalParams, getModel().getObject(), target);
                    target.add(table);
                    atualizarContadores(target);
                }
            };
        }
    }

    protected void atualizarContadores(AjaxRequestTarget target) {
        target.appendJavaScript("(function(){window.Singular.atualizarContadores();}())");
    }


    private IFunction<IModel<BoxItemDataMap>, Boolean> visibleFunction(BoxItemAction itemAction) {
        return (model) -> {
            BoxItemDataMap boxItemDataMap = model.getObject();
            boolean visible = boxItemDataMap.hasAction(itemAction);
            if (!visible) {
                getLogger().debug("Action {} não está disponível para o item ({}: código da petição) da listagem ", itemAction.getName(), boxItemDataMap.getCod());
            }

            return visible;
        };
    }

    @Override
    protected Pair<String, SortOrder> getSortProperty() {
        return sortProperty;
    }

    public void setSortProperty(Pair<String, SortOrder> sortProperty) {
        this.sortProperty = sortProperty;
    }


    @Override
    protected BoxFilter newFilterBasic() {
        return boxFilterFactory.create(getBoxDefinitionObject()).filter(getFiltroRapidoModelObject());
    }

    private BoxPage getBoxPage() {
        return (BoxPage) getPage();
    }

    @Override
    protected List<BoxItemDataMap> quickSearch(BoxFilter filter) {
        return moduleService.searchFiltered(getBoxDefinitionObject(), filter);
    }

    private Map<String, String> getLinkParams() {
        final BoxPage page = getBoxPage();
        return page.createLinkParams();
    }

    @Override
    protected long countQuickSearch(BoxFilter filter) {
        return moduleService.countFiltered(getBoxDefinitionObject(), filter);
    }

    public List<DatatableField> getFieldsDatatable() {
        return getBoxDefinitionObject().getDatatableFields();
    }

    private BoxDefinition getBoxDefinitionObject() {
        return boxDefinition.getObject();
    }

}