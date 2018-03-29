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

package org.opensingular.requirement.core.wicket.box;

import org.apache.wicket.RestartResponseException;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.opensingular.flow.persistence.entity.ModuleEntity;
import org.opensingular.requirement.commons.persistence.filter.QuickFilter;
import org.opensingular.requirement.commons.service.dto.BoxConfigurationData;
import org.opensingular.requirement.commons.service.dto.BoxDefinitionData;
import org.opensingular.requirement.commons.spring.security.SingularUserDetails;
import org.opensingular.requirement.commons.wicket.SingularSession;
import org.opensingular.requirement.commons.wicket.error.AccessDeniedPage;
import org.opensingular.requirement.commons.wicket.view.template.MenuService;
import org.opensingular.requirement.core.wicket.template.ServerBoxTemplate;
import org.slf4j.LoggerFactory;
import org.wicketstuff.annotation.mount.MountPath;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.opensingular.requirement.commons.wicket.view.util.ActionContext.*;

@MountPath("/box")
public class BoxPage extends ServerBoxTemplate {

    private final static org.slf4j.Logger LOGGER = LoggerFactory.getLogger(BoxPage.class);

    private BoxDefinitionData boxDefinitionData;

    @Inject
    @SpringBean(required = false)
    private MenuService menuService;

    public BoxPage(PageParameters parameters) {
        super(parameters);
        addBox();
    }

    public void addBox() {
        String moduleCod = getPageParameters().get(MODULE_PARAM_NAME).toOptionalString();
        String menu      = getPageParameters().get(MENU_PARAM_NAME).toOptionalString();
        String item      = getPageParameters().get(ITEM_PARAM_NAME).toOptionalString();

        if (isAccessWithoutParams(moduleCod, menu, item)) {
            for (Map.Entry<ModuleEntity, List<BoxConfigurationData>> entry : menuService.getMap().entrySet()) {
                if (!entry.getValue().isEmpty()) {
                    moduleCod = entry.getKey().getCod();
                    BoxConfigurationData mg = entry.getValue().get(0);
                    menu = mg.getLabel();
                    PageParameters pageParameters = new PageParameters();

                    addItemParam(mg, pageParameters);

                    pageParameters.add(MODULE_PARAM_NAME, moduleCod);
                    pageParameters.add(MENU_PARAM_NAME, menu);
                    throw new RestartResponseException(getPageClass(), pageParameters);
                }
            }
        }

        BoxConfigurationData boxConfigurationMetadata = null;
        if (menuService != null) {
            boxConfigurationMetadata = menuService.getMenuByLabel(menu);
        }
        if (boxConfigurationMetadata != null) {
            boxDefinitionData = boxConfigurationMetadata.getItemPorLabel(item);
            //itemBoxDTO pode ser nulo quando nenhum item está selecionado.
            if (boxDefinitionData != null) {
                add(newBoxContent("box", moduleCod, boxConfigurationMetadata, boxDefinitionData));
                return;
            }
        }

        if (boxConfigurationMetadata == null) {
            LOGGER.error("As configurações de caixas não foram encontradas. Verfique se as permissões estão configuradas corretamente");
        }
        LOGGER.error("Não existe caixa correspondente para {}", String.valueOf(item));
        throw new RestartResponseException(AccessDeniedPage.class);
    }

    private boolean isAccessWithoutParams(String moduleCod, String menu, String item) {
        return moduleCod == null
                && menu == null
                && item == null
                && menuService != null;
    }

    private void addItemParam(BoxConfigurationData mg, PageParameters pageParameters) {
        if (!mg.getBoxesDefinition().isEmpty()) {
            String item = mg.getItemBoxes().get(0).getName();
            pageParameters.add(ITEM_PARAM_NAME, item);
        }
    }

    protected BoxContent newBoxContent(String id, String moduleCod, BoxConfigurationData boxConfigurationMetadata, BoxDefinitionData boxDefinitionData) {
        return new BoxContent(id, moduleCod, boxConfigurationMetadata.getLabel(), boxDefinitionData);
    }

    protected Map<String, String> createLinkParams() {
        return new HashMap<>();
    }

    protected QuickFilter createFilter() {
        return new QuickFilter()
                .withIdUsuarioLogado(getIdUsuario())
                .withIdPessoa(getIdPessoa());
    }

    protected String getIdUsuario() {
        SingularUserDetails userDetails = SingularSession.get().getUserDetails();
        return Optional.ofNullable(userDetails)
                .map(SingularUserDetails::getUsername)
                .orElse(null);
    }

    protected String getIdPessoa() {
        SingularUserDetails userDetails = SingularSession.get().getUserDetails();
        return Optional.ofNullable(userDetails)
                .map(SingularUserDetails::getUserId)
                .orElse(null);
    }

    @Override
    protected IModel<String> getContentSubtitle() {
        return new Model<String>() {
            @Override
            public String getObject() {
                if (boxDefinitionData != null) {
                    return boxDefinitionData.getItemBox().getDescription();
                }
                return null;
            }
        };
    }

    @Override
    protected IModel<String> getContentTitle() {
        return new Model<String>() {
            @Override
            public String getObject() {
                if (boxDefinitionData != null) {
                    return boxDefinitionData.getItemBox().getName();
                }
                return null;
            }
        };
    }

    @Override
    public IModel<String> getHelpText() {
        return new Model<String>() {
            @Override
            public String getObject() {
                if (boxDefinitionData != null) {
                    return boxDefinitionData.getItemBox().getHelpText();
                }
                return null;
            }
        };
    }
}