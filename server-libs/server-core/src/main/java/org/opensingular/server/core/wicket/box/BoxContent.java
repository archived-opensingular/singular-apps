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

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.opensingular.flow.persistence.entity.Actor;
import org.opensingular.lib.commons.lambda.IBiFunction;
import org.opensingular.lib.commons.lambda.IFunction;
import org.opensingular.lib.wicket.util.datatable.BSDataTableBuilder;
import org.opensingular.lib.wicket.util.datatable.IBSAction;
import org.opensingular.lib.wicket.util.datatable.column.BSActionColumn;
import org.opensingular.lib.wicket.util.modal.BSModalBorder;
import org.opensingular.lib.wicket.util.resource.Icone;
import org.opensingular.server.commons.flow.rest.ActionAtribuirRequest;
import org.opensingular.server.commons.flow.rest.ActionRequest;
import org.opensingular.server.commons.flow.rest.ActionResponse;
import org.opensingular.server.commons.form.FormActions;
import org.opensingular.server.commons.persistence.filter.QuickFilter;
import org.opensingular.server.commons.service.dto.BoxItemAction;
import org.opensingular.server.commons.service.dto.FormDTO;
import org.opensingular.server.commons.service.dto.ItemAction;
import org.opensingular.server.commons.service.dto.ItemActionConfirmation;
import org.opensingular.server.commons.service.dto.ItemActionType;
import org.opensingular.server.commons.service.dto.ItemBox;
import org.opensingular.server.commons.service.dto.ProcessDTO;
import org.opensingular.server.commons.util.DispatcherPageParameters;
import org.opensingular.server.commons.wicket.view.util.DispatcherPageUtil;
import org.opensingular.server.core.wicket.ModuleLink;
import org.opensingular.server.core.wicket.historico.HistoricoPage;
import org.opensingular.server.core.wicket.model.BoxItemModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.opensingular.lib.wicket.util.util.WicketUtils.$b;
import static org.opensingular.lib.wicket.util.util.WicketUtils.$m;
import static org.opensingular.server.commons.service.IServerMetadataREST.PATH_BOX_SEARCH;
import static org.opensingular.server.commons.util.DispatcherPageParameters.FORM_NAME;

public class BoxContent extends AbstractCaixaContent<BoxItemModel> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BoxContent.class);

    private Pair<String, SortOrder> sortProperty;
    private ItemBox                 itemBoxDTO;
    protected IModel<BoxItemModel>    currentModel;

    public BoxContent(String id, String processGroupCod, String menu, ItemBox itemBoxDTO) {
        super(id, processGroupCod, menu);
        this.itemBoxDTO = itemBoxDTO;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        showNew();
        configureQuickFilter();
    }

    private void configureQuickFilter() {
        getFiltroRapido().setVisible(isShowQuickFilter());
        getPesquisarButton().setVisible(isShowQuickFilter());
    }

    private void showNew() {
        if (isShowNew() && getMenu() != null) {
            List<FormDTO> forms = getForms().stream().filter(FormDTO::isNewable).collect(Collectors.toList());
            for (FormDTO form : forms) {
                String url = DispatcherPageUtil
                        .baseURL(getBaseUrl())
                        .formAction(FormActions.FORM_FILL.getId())
                        .petitionId(null)
                        .param(DispatcherPageParameters.FORM_NAME, form.getName())
                        .params(getLinkParams())
                        .build();

                if (forms.size() > 1) {
                    dropdownMenu.adicionarMenu(id -> new ModuleLink(id, $m.ofValue(form.getDescription()), url));
                } else {
                    adicionarBotaoGlobal(id -> new ModuleLink(id, getMessage("label.button.insert"), url));
                }
            }
        }

    }

    @Override
    protected void appendPropertyColumns(BSDataTableBuilder<BoxItemModel, String, IColumn<BoxItemModel, String>> builder) {
        for (Map.Entry<String, String> entry : getFieldsDatatable().entrySet()) {
            builder.appendPropertyColumn($m.ofValue(entry.getKey()), entry.getValue(), entry.getValue());
        }
    }

    @Override
    protected void appendActionColumns(BSDataTableBuilder<BoxItemModel, String, IColumn<BoxItemModel, String>> builder) {
        BSActionColumn<BoxItemModel, String> actionColumn = new BSActionColumn<>(getMessage("label.table.column.actions"));

        if (getLogger().isDebugEnabled()) {
            getLogger().debug(String.format("A caixa %s permite a apresentação apenas das ações %s", itemBoxDTO.getName(), Arrays.toString(itemBoxDTO.getActions().keySet().toArray())));
        }
        for (ItemAction itemAction : itemBoxDTO.getActions().values()) {

            if (itemAction.getType() == ItemActionType.POPUP) {
                actionColumn.appendStaticAction(
                        $m.ofValue(itemAction.getLabel()),
                        itemAction.getIcon(),
                        linkFunction(itemAction, getBaseUrl(), getLinkParams()),
                        visibleFunction(itemAction),
                        c -> c.styleClasses($m.ofValue("worklist-action-btn")));
            } else if (itemAction.getType() == ItemActionType.ENDPOINT) {
                actionColumn.appendAction(
                        $m.ofValue(itemAction.getLabel()),
                        itemAction.getIcon(),
                        dynamicLinkFunction(itemAction, getProcessGroup().getConnectionURL(), getLinkParams()),
                        visibleFunction(itemAction),
                        c -> c.styleClasses($m.ofValue("worklist-action-btn")));
            }
        }

        actionColumn
                .appendStaticAction(
                        getMessage("label.table.column.history"),
                        Icone.HISTORY,
                        this::criarLinkHistorico,
                        (x) -> true,
                        c -> c.styleClasses($m.ofValue("worklist-action-btn")));

        builder.appendColumn(actionColumn);
    }

    private MarkupContainer criarLinkHistorico(String id, IModel<BoxItemModel> boxItemModel) {
        BoxItemModel   boxItem        = boxItemModel.getObject();
        PageParameters pageParameters = new PageParameters();
        if (boxItem.getProcessInstanceId() != null) {
            pageParameters.add(DispatcherPageParameters.PETITION_ID, boxItem.getCod());
            pageParameters.add(DispatcherPageParameters.INSTANCE_ID, boxItem.getProcessInstanceId());
            pageParameters.add(DispatcherPageParameters.PROCESS_GROUP_PARAM_NAME, getProcessGroup().getCod());
        }
        BookmarkablePageLink<?> historiLink = new BookmarkablePageLink<>(id, getHistoricoPage(), pageParameters);
        historiLink.setVisible(boxItem.getProcessBeginDate() != null);
        return historiLink;
    }

    protected Class<? extends HistoricoPage> getHistoricoPage() {
        return HistoricoPage.class;
    }

    @Override
    protected WebMarkupContainer criarLinkEdicao(String id, IModel<BoxItemModel> peticao) {
        if (peticao.getObject().getProcessBeginDate() == null) {
            return criarLink(id, peticao, FormActions.FORM_FILL);
        } else {
            return criarLink(id, peticao, FormActions.FORM_FILL_WITH_ANALYSIS);
        }
    }

    public IBiFunction<String, IModel<BoxItemModel>, MarkupContainer> linkFunction(ItemAction itemAction, String baseUrl, Map<String, String> additionalParams) {
        return (id, boxItemModel) -> {
            String url = mountStaticUrl(itemAction, baseUrl, additionalParams, boxItemModel);

            WebMarkupContainer link = new WebMarkupContainer(id);
            link.add($b.attr("target", String.format("_%s_%s", itemAction.getName(),  boxItemModel.getObject().getCod())));
            link.add($b.attr("href", url));
            return link;
        };
    }

    private String mountStaticUrl(ItemAction itemAction, String baseUrl, Map<String, String> additionalParams, IModel<BoxItemModel> boxItemModel) {
        final BoxItemAction action = boxItemModel.getObject().getActionByName(itemAction.getName());
        if (action.getEndpoint().startsWith("http")) {
            return action.getEndpoint();
        } else {
            return baseUrl
                    + action.getEndpoint()
                    + appendParameters(additionalParams);
        }
    }

    private IBSAction<BoxItemModel> dynamicLinkFunction(ItemAction itemAction, String baseUrl, Map<String, String> additionalParams) {
        if (itemAction.getConfirmation() != null) {
            return (target, model) -> {
                currentModel = model;
                final BSModalBorder confirmationModal = construirModalConfirmationBorder(itemAction, baseUrl, additionalParams);
                confirmationForm.addOrReplace(confirmationModal);
                confirmationModal.show(target);
            };
        } else {
            return (target, model) -> executeDynamicAction(itemAction, baseUrl, additionalParams, model.getObject(), target);
        }
    }

    protected void executeDynamicAction(ItemAction itemAction, String baseUrl, Map<String, String> additionalParams, BoxItemModel boxItem, AjaxRequestTarget target) {
        final BoxItemAction boxAction = boxItem.getActionByName(itemAction.getName());

        String url = baseUrl
                + boxAction.getEndpoint()
                + appendParameters(additionalParams);

        try {
            callModule(url, buildCallObject(boxAction, boxItem));
        } catch (Exception e) {
            LOGGER.error("Erro ao acessar serviço: " + url, e);
            addToastrErrorMessage("Não foi possível executar esta ação.");
        } finally {
            target.add(tabela);
        }
    }

    protected void relocate(ItemAction itemAction, String baseUrl, Map<String, String> additionalParams, BoxItemModel boxItem, AjaxRequestTarget target, Actor actor) {
        final BoxItemAction boxAction = boxItem.getActionByName(itemAction.getName());

        String url = baseUrl
                + boxAction.getEndpoint()
                + appendParameters(additionalParams);

        try {
            callModule(url, buildCallAtribuirObject(boxAction, boxItem, actor));
        } catch (Exception e) {
            LOGGER.error("Erro ao acessar serviço: " + url, e);
            addToastrErrorMessage("Não foi possível executar esta ação.");
        } finally {
            target.add(tabela);
        }
    }

    protected Object buildCallAtribuirObject(BoxItemAction boxAction, BoxItemModel boxItem, Actor actor) {
        ActionAtribuirRequest actionRequest = new ActionAtribuirRequest();
        actionRequest.setIdUsuario(getBoxPage().getIdUsuario());
        if (actor == null) {
            actionRequest.setEndLastAllocation(true);
        } else {
            actionRequest.setIdUsuarioDestino(actor.getCodUsuario());
        }
        if (boxAction.isUseExecute()) {
            actionRequest.setName(boxAction.getName());
            actionRequest.setLastVersion(boxItem.getVersionStamp());
        }

        return actionRequest;
    }

    private void callModule(String url, Object arg) {
        ActionResponse response = new RestTemplate().postForObject(url, arg, ActionResponse.class);
        if (response.isSuccessful()) {
            addToastrSuccessMessage(response.getResultMessage());
        } else {
            addToastrErrorMessage(response.getResultMessage());
        }
    }

    private Object buildCallObject(BoxItemAction boxAction, BoxItemModel boxItem) {
        ActionRequest actionRequest = new ActionRequest();
        actionRequest.setIdUsuario(getBoxPage().getIdUsuario());
        if (boxAction.isUseExecute()) {
            actionRequest.setName(boxAction.getName());
            actionRequest.setLastVersion(boxItem.getVersionStamp());
        }

        return actionRequest;
    }

    protected BSModalBorder construirModalConfirmationBorder(ItemAction itemAction, String baseUrl, Map<String, String> additionalParams) {
        final ItemActionConfirmation confirmation      = itemAction.getConfirmation();
        BSModalBorder                confirmationModal = new BSModalBorder("confirmationModal", $m.ofValue(confirmation.getTitle()));
        confirmationModal.addOrReplace(new Label("message", $m.ofValue(confirmation.getConfirmationMessage())));

        Model<Actor>        actorModel          = new Model<>();
        IModel<List<Actor>> actorsModel    = $m.get(() -> buscarUsuarios(currentModel, confirmation));
        DropDownChoice      dropDownChoice = criarDropDown(actorsModel, actorModel);
        dropDownChoice.setVisible(StringUtils.isNotBlank(confirmation.getSelectEndpoint()));
        confirmationModal.addOrReplace(dropDownChoice);

        confirmationModal.addButton(BSModalBorder.ButtonStyle.CANCEl, $m.ofValue(confirmation.getCancelButtonLabel()), new AjaxButton("cancel-delete-btn", confirmationForm) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                currentModel = null;
                confirmationModal.hide(target);
            }
        }.setDefaultFormProcessing(false));

        appendExtraButtons(confirmationModal, actorModel, itemAction, baseUrl, additionalParams);

        if (StringUtils.isNotBlank(confirmation.getSelectEndpoint())) {
            confirmationModal.addButton(BSModalBorder.ButtonStyle.CONFIRM, $m.ofValue(confirmation.getConfirmationButtonLabel()), new AjaxButton("delete-btn", confirmationForm) {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    relocate(itemAction, baseUrl, additionalParams, currentModel.getObject(), target, actorModel.getObject());
                    target.add(tabela);
                    atualizarContadores(target);
                    confirmationModal.hide(target);
                }
            });
        } else {
            confirmationModal.addButton(BSModalBorder.ButtonStyle.CONFIRM, $m.ofValue(confirmation.getConfirmationButtonLabel()), new AjaxButton("delete-btn", confirmationForm) {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    executeDynamicAction(itemAction, baseUrl, additionalParams, currentModel.getObject(), target);
                    target.add(tabela);
                    atualizarContadores(target);
                    confirmationModal.hide(target);
                }
            });
        }

        return confirmationModal;
    }

    protected void appendExtraButtons(BSModalBorder confirmationModal, Model<Actor> actorModel, ItemAction itemAction, String baseUrl, Map<String, String> additionalParams) {

    }

    protected void atualizarContadores(AjaxRequestTarget target) {
        target.appendJavaScript("(function(){window.Singular.atualizarContadores();}())");
    }

    private DropDownChoice criarDropDown(IModel<List<Actor>> actorsModel, Model<Actor> model) {
        DropDownChoice<Actor> dropDownChoice = new DropDownChoice<>("selecao",
                model,
                actorsModel,
                new ChoiceRenderer<>("nome", "codUsuario"));
        dropDownChoice.setRequired(true);
        return dropDownChoice;
    }

    @SuppressWarnings("unchecked")
    private List<Actor> buscarUsuarios(IModel<BoxItemModel> currentModel, ItemActionConfirmation confirmation) {
        final String connectionURL = getProcessGroup().getConnectionURL();
        final String url           = connectionURL + PATH_BOX_SEARCH + confirmation.getSelectEndpoint();

        try {
            return Arrays.asList(new RestTemplate().postForObject(url, currentModel.getObject(), Actor[].class));
        } catch (Exception e) {
            LOGGER.error("Erro ao acessar serviço: " + url, e);
            return Collections.emptyList();
        }
    }

    private String appendParameters(Map<String, String> additionalParams) {
        String paramsValue = "";
        if (!additionalParams.isEmpty()) {
            for (Map.Entry<String, String> entry : additionalParams.entrySet()) {
                paramsValue += "&" + entry.getKey() + "=" + entry.getValue();
            }
        }
        return paramsValue;
    }

    private IFunction<IModel<BoxItemModel>, Boolean> visibleFunction(ItemAction itemAction) {
        return (model) -> {
            BoxItemModel boxItemModel = (BoxItemModel) model.getObject();
            boolean      visible      = boxItemModel.hasAction(itemAction);
            if (!visible) {
                getLogger().debug(String.format("Action %s não está disponível para o item (%s: código da petição) da listagem ", itemAction.getName(), boxItemModel.getCod()));
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
    protected void onDelete(BoxItemModel peticao) {

    }

    @Override
    protected QuickFilter montarFiltroBasico() {
        BoxPage boxPage = getBoxPage();
        return boxPage.createFilter()
                .withFilter(getFiltroRapidoModelObject())
                .withProcessesAbbreviation(getProcessesNames())
                .withTypesNames(getFormNames())
                .withRascunho(isWithRascunho())
                .withEndedTasks(itemBoxDTO.getEndedTasks());
    }

    private BoxPage getBoxPage() {
        return (BoxPage) getPage();
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
        if (getForms() == null) {
            return Collections.emptyList();
        } else {
            return getForms()
                    .stream()
                    .map(FormDTO::getName)
                    .collect(Collectors.toList());
        }
    }

    @Override
    protected List<BoxItemModel> quickSearch(QuickFilter filter, List<String> siglasProcesso, List<String> formNames) {
        final String connectionURL = getProcessGroup().getConnectionURL();
        final String url           = connectionURL + PATH_BOX_SEARCH + getSearchEndpoint();
        try {
            return (List<BoxItemModel>) Arrays
                    .asList(new RestTemplate().postForObject(url, filter, Map[].class))
                    .stream()
                    .map(BoxItemModel::new)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            LOGGER.error("Erro ao acessar serviço: " + url, e);
            return Collections.emptyList();
        }
    }

    @Override
    protected WebMarkupContainer criarLink(String id, IModel<BoxItemModel> itemModel, FormActions formActions) {
        BoxItemModel item = itemModel.getObject();
        String href = DispatcherPageUtil
                .baseURL(getBaseUrl())
                .formAction(formActions.getId())
                .petitionId(item.getCod())
                .param(FORM_NAME, item.get("type"))
                .params(getCriarLinkParameters(item))
                .build();

        WebMarkupContainer link = new WebMarkupContainer(id);
        link.add($b.attr("target", String.format("_%s_%s", formActions.getId(), item.getCod())));
        link.add($b.attr("href", href));
        return link;
    }

    protected Map<String, String> getCriarLinkParameters(BoxItemModel item) {
        final Map<String, String> linkParameters = new HashMap<>();
        linkParameters.putAll(getLinkParams());
        return linkParameters;
    }

    private Map<String, String> getLinkParams() {
        final BoxPage page = getBoxPage();
        return page.createLinkParams();
    }

    @Override
    protected long countQuickSearch(QuickFilter filter, List<String> processesNames, List<String> formNames) {
        final String connectionURL = getProcessGroup().getConnectionURL();
        final String url           = connectionURL + PATH_BOX_SEARCH + getCountEndpoint();
        try {
            return new RestTemplate().postForObject(url, filter, Long.class);
        } catch (Exception e) {
            LOGGER.error("Erro ao acessar serviço: " + url, e);
            return 0;
        }
    }

    @Override
    public IModel<?> getContentTitleModel() {
        return $m.ofValue(itemBoxDTO.getName());
    }

    @Override
    public IModel<?> getContentSubtitleModel() {
        return $m.ofValue(itemBoxDTO.getDescription());
    }

    public boolean isShowNew() {
        return itemBoxDTO.isShowNewButton();
    }

    public boolean isShowQuickFilter() {
        return itemBoxDTO.isQuickFilter();
    }

    public Map<String, String> getFieldsDatatable() {
        return itemBoxDTO.getFieldsDatatable();
    }

    public String getSearchEndpoint() {
        return itemBoxDTO.getSearchEndpoint();
    }

    public String getCountEndpoint() {
        return itemBoxDTO.getCountEndpoint();
    }

    public boolean isWithRascunho() {
        return itemBoxDTO.isShowDraft();
    }
}
