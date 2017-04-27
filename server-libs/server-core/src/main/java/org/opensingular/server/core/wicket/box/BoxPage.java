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

import org.apache.wicket.RestartResponseException;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.opensingular.flow.persistence.entity.ProcessGroupEntity;
import org.opensingular.server.commons.persistence.filter.QuickFilter;
import org.opensingular.server.commons.service.dto.BoxConfigurationData;
import org.opensingular.server.commons.service.dto.BoxDefinitionData;
import org.opensingular.server.commons.spring.security.SingularUserDetails;
import org.opensingular.server.commons.wicket.SingularSession;
import org.opensingular.server.commons.wicket.error.AccessDeniedContent;
import org.opensingular.server.commons.wicket.view.template.Content;
import org.opensingular.server.commons.wicket.view.template.MenuService;
import org.opensingular.server.core.wicket.template.ServerTemplate;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.opensingular.server.commons.wicket.view.util.ActionContext.ITEM_PARAM_NAME;
import static org.opensingular.server.commons.wicket.view.util.ActionContext.MENU_PARAM_NAME;
import static org.opensingular.server.commons.wicket.view.util.ActionContext.PROCESS_GROUP_PARAM_NAME;

public class BoxPage extends ServerTemplate {

    private final static org.slf4j.Logger LOGGER = LoggerFactory.getLogger(BoxPage.class);

    @Inject
    @SpringBean(required = false)
    private MenuService menuService;

    @Override
    protected Content getContent(String id) {

        String processGroupCod = getPageParameters().get(PROCESS_GROUP_PARAM_NAME).toOptionalString();
        String menu            = getPageParameters().get(MENU_PARAM_NAME).toOptionalString();
        String item            = getPageParameters().get(ITEM_PARAM_NAME).toOptionalString();


        if (processGroupCod == null
                && menu == null
                && item == null
                && menuService != null) {

            for (Iterator<Map.Entry<ProcessGroupEntity, List<BoxConfigurationData>>> it = menuService.getMap().entrySet().iterator(); it.hasNext(); ) {
                Map.Entry<ProcessGroupEntity, List<BoxConfigurationData>> entry = it.next();
                if (!entry.getValue().isEmpty()) {
                    processGroupCod = entry.getKey().getCod();
                    BoxConfigurationData mg = entry.getValue().get(0);
                    menu = mg.getLabel();
                    PageParameters pageParameters = new PageParameters();

                    if (!mg.getBoxesDefinition().isEmpty()){
                        item = mg.getItemBoxes().get(0).getName();
                        pageParameters.add(ITEM_PARAM_NAME, item);
                    }

                    pageParameters.add(PROCESS_GROUP_PARAM_NAME, processGroupCod);
                    pageParameters.add(MENU_PARAM_NAME, menu);
                    throw new RestartResponseException(getPage().getClass(), pageParameters);
                }
            }

        }

        BoxConfigurationData boxConfigurationMetadata = null;
        if (menuService != null) {
            boxConfigurationMetadata = menuService.getMenuByLabel(menu);
        }

        if (boxConfigurationMetadata != null) {
            final BoxDefinitionData boxDefinitionData = boxConfigurationMetadata.getItemPorLabel(item);
            /**
             * itemBoxDTO pode ser nulo quando nenhum item está selecionado.
             */
            if (boxDefinitionData != null) {
                return newBoxContent(id, processGroupCod, boxConfigurationMetadata, boxDefinitionData);
            }
        }

        /**
         * Fallback
         */
        LOGGER.warn("Não existe correspondencia para o label {}", String.valueOf(item));
        return new AccessDeniedContent(id);
    }

    protected BoxContent newBoxContent(String id, String processGroupCod, BoxConfigurationData boxConfigurationMetadata, BoxDefinitionData boxDefinitionData) {
        return new BoxContent(id, processGroupCod, boxConfigurationMetadata.getLabel(), boxDefinitionData);
    }

    protected Map<String, String> createLinkParams() {
        return new HashMap<>();
    }

    protected QuickFilter createFilter() {
        return new QuickFilter()
                .withIdUsuarioLogado(getIdUsuario());
    }

    protected String getIdUsuario() {
        SingularUserDetails userDetails = SingularSession.get().getUserDetails();
        return Optional.ofNullable(userDetails)
                .map(SingularUserDetails::getUsername)
                .orElse(null);
    }

}