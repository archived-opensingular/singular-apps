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

package org.opensingular.server.commons.connector;

import org.apache.commons.lang3.StringUtils;
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
import org.opensingular.server.commons.wicket.SingularSession;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.opensingular.server.commons.RESTPaths.*;

public class RESTModuleDriver implements ModuleDriver, Loggable {

    private <T extends SingularUserDetails> T getUserDetails() {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof SingularUserDetails) {
            return (T) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }
        return null;
    }

    @Override
    public WorkspaceConfigurationMetadata retrieveModuleWorkspace(ModuleEntity module, IServerContext serverContext) {
        RestTemplate restTemplate = new RestTemplate();
        String url = module.getConnectionURL() + WORKSPACE_CONFIGURATION + "?" + MENU_CONTEXT + "=" + serverContext.getName();
        SingularUserDetails userDetails = getUserDetails();
        if (userDetails != null) {
            url += "&" + USER + "=" + userDetails.getUserPermissionKey();
        }
        return restTemplate.getForObject(url, WorkspaceConfigurationMetadata.class);
    }

    @Override
    public String countAll(ModuleEntity module, ItemBox box, List<String> flowNames, String loggedUser) {
        final String connectionURL = module.getConnectionURL();
        final String url = connectionURL + box.getCountEndpoint();
        long qtd;
        try {
            QuickFilter filter = new QuickFilter()
                    .withProcessesAbbreviation(flowNames)
                    .withRascunho(box.isShowDraft())
                    .withEndedTasks(box.getEndedTasks())
                    .withIdUsuarioLogado(loggedUser)
                    .withIdPessoa(SingularSession.get().getUserDetails().getUserId());
            qtd = new RestTemplate().postForObject(url, filter, Long.class);
        } catch (Exception e) {
            getLogger().error("Erro ao acessar serviço: " + url, e);
            qtd = 0;
        }
        return String.valueOf(qtd);
    }

    @Override
    public long countFiltered(ModuleEntity module, ItemBox box, QuickFilter filter) {
        final String connectionURL = module.getConnectionURL();
        final String url = connectionURL + box.getCountEndpoint();
        try {
            return new RestTemplate().postForObject(url, filter, Long.class);
        } catch (Exception e) {
            getLogger().error("Erro ao acessar serviço: " + url, e);
            return 0;
        }
    }

    @Override
    public List<BoxItemDataMap> searchFiltered(ModuleEntity module, ItemBox box, QuickFilter filter) {
        final String connectionURL = module.getConnectionURL();
        final String url = connectionURL + box.getSearchEndpoint();
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
    public List<Actor> findEligibleUsers(ModuleEntity module, BoxItemDataMap rowItemData, ItemActionConfirmation confirmAction) {
        final String connectionURL = module.getConnectionURL();
        final String url = connectionURL + PATH_BOX_SEARCH + confirmAction.getSelectEndpoint();

        try {
            return Arrays.asList(new RestTemplate().postForObject(url, rowItemData, Actor[].class));
        } catch (Exception e) {
            getLogger().error("Erro ao acessar serviço: " + url, e);
            return Collections.emptyList();
        }
    }

    @Override
    public ActionResponse executeAction(ModuleEntity moduleEntity, BoxItemAction itemAction,
                                        Map<String, String> params, ActionRequest actionRequest) {
        String url = moduleEntity.getConnectionURL()
                + itemAction.getEndpoint()
                + appendParameters(params);
        return new RestTemplate().postForObject(url, actionRequest, ActionResponse.class);
    }

    @Override
    public String buildUrlToBeRedirected(BoxItemDataMap rowItemData, BoxItemAction rowAction, Map<String, String> params, String baseURI) {
        final BoxItemAction action = rowItemData.getActionByName(rowAction.getName());
        final String endpoint = StringUtils.trimToEmpty(action.getEndpoint());
        if (endpoint.startsWith("http")) {
            return endpoint;
        } else {
            return baseURI
                    + endpoint
                    + appendParameters(params);
        }
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