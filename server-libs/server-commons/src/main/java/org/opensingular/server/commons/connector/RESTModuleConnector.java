package org.opensingular.server.commons.connector;

import org.opensingular.flow.persistence.entity.Actor;
import org.opensingular.flow.persistence.entity.ModuleEntity;
import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.server.commons.WorkspaceConfigurationMetadata;
import org.opensingular.server.commons.box.BoxItemDataList;
import org.opensingular.server.commons.box.BoxItemDataMap;
import org.opensingular.server.commons.box.action.ActionRequest;
import org.opensingular.server.commons.box.action.ActionResponse;
import org.opensingular.server.commons.config.IServerContext;
import org.opensingular.server.commons.persistence.filter.QuickFilter;
import org.opensingular.server.commons.service.dto.BoxItemAction;
import org.opensingular.server.commons.service.dto.ItemActionConfirmation;
import org.opensingular.server.commons.service.dto.ItemBox;
import org.opensingular.server.commons.spring.security.SingularUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.RestTemplate;

import javax.inject.Named;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.opensingular.server.commons.RESTPaths.*;

@Named
public class RESTModuleConnector implements ModuleConnector, Loggable {

    private <T extends SingularUserDetails> T getUserDetails() {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof SingularUserDetails) {
            return (T) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }
        return null;
    }

    @Override
    public WorkspaceConfigurationMetadata loadWorkspaceConfiguration(ModuleEntity module, IServerContext serverContext) {
        RestTemplate restTemplate = new RestTemplate();
        String url = module.getConnectionURL() + WORKSPACE_CONFIGURATION + "?" + MENU_CONTEXT + "=" + serverContext.getName();
        SingularUserDetails userDetails = getUserDetails();
        if (userDetails != null) {
            url += "&" + USER + "=" + userDetails.getUserPermissionKey();
        }
        return restTemplate.getForObject(url, WorkspaceConfigurationMetadata.class);
    }

    @Override
    public String count(ItemBox itemBoxDTO, List<String> siglas, String idUsuarioLogado, ModuleEntity module) {
        final String connectionURL = module.getConnectionURL();
        final String url = connectionURL + itemBoxDTO.getCountEndpoint();
        long qtd;
        try {
            QuickFilter filter = new QuickFilter()
                    .withProcessesAbbreviation(siglas)
                    .withRascunho(itemBoxDTO.isShowDraft())
                    .withEndedTasks(itemBoxDTO.getEndedTasks())
                    .withIdUsuarioLogado(idUsuarioLogado);
            qtd = new RestTemplate().postForObject(url, filter, Long.class);
        } catch (Exception e) {
            getLogger().error("Erro ao acessar serviço: " + url, e);
            qtd = 0;
        }
        return String.valueOf(qtd);
    }

    @Override
    public long countQuickSearch(ModuleEntity module, ItemBox itemBox, QuickFilter filter) {
        final String connectionURL = module.getConnectionURL();
        final String url = connectionURL + itemBox.getCountEndpoint();
        try {
            return new RestTemplate().postForObject(url, filter, Long.class);
        } catch (Exception e) {
            getLogger().error("Erro ao acessar serviço: " + url, e);
            return 0;
        }
    }

    @Override
    public List<BoxItemDataMap> quickSearch(ModuleEntity module, ItemBox itemBox, QuickFilter filter) {
        final String connectionURL = module.getConnectionURL();
        final String url = connectionURL + itemBox.getSearchEndpoint();
        try {
            return new RestTemplate().postForObject(url, filter, BoxItemDataList.class)
                    .getBoxItemDataList()
                    .stream()
                    .map(BoxItemDataMap::new)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            getLogger().error("Erro ao acessar serviço: " + url, e);
            return Collections.emptyList();
        }
    }

    @Override
    public List<Actor> buscarUsuarios(ModuleEntity module, BoxItemDataMap boxItemDataMap, ItemActionConfirmation confirmation) {
        final String connectionURL = module.getConnectionURL();
        final String url = connectionURL + PATH_BOX_SEARCH + confirmation.getSelectEndpoint();

        try {
            return Arrays.asList(new RestTemplate().postForObject(url, boxItemDataMap, Actor[].class));
        } catch (Exception e) {
            getLogger().error("Erro ao acessar serviço: " + url, e);
            return Collections.emptyList();
        }
    }

    @Override
    public ActionResponse callModule(ModuleEntity moduleEntity, BoxItemAction itemAction,
                                     Map<String, String> params, ActionRequest actionRequest) {
        String url = moduleEntity.getConnectionURL()
                + itemAction.getEndpoint()
                + appendParameters(params);
        return new RestTemplate().postForObject(url, actionRequest, ActionResponse.class);
    }

    private String appendParameters(Map<String, String> additionalParams) {
        StringBuilder paramsValue = new StringBuilder();
        if (!additionalParams.isEmpty()) {
            for (Map.Entry<String, String> entry : additionalParams.entrySet()) {
                paramsValue.append(String.format("&%s=%s", entry.getKey(), entry.getValue()));
            }
        }
        return paramsValue.toString();
    }


}