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

package org.opensingular.server.commons.spring.security;

import com.google.common.base.Joiner;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.opensingular.flow.persistence.entity.Actor;
import org.opensingular.form.context.SFormConfig;
import org.opensingular.lib.commons.base.SingularProperties;
import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.server.commons.box.action.BoxItemActionList;
import org.opensingular.server.commons.form.FormAction;
import org.opensingular.server.commons.persistence.entity.form.PetitionEntity;
import org.opensingular.server.commons.service.PetitionInstance;
import org.opensingular.server.commons.service.PetitionService;
import org.opensingular.server.commons.service.dto.BoxConfigurationData;
import org.opensingular.server.commons.service.dto.BoxItemAction;
import org.opensingular.server.commons.service.dto.FormDTO;
import org.opensingular.server.commons.wicket.SingularSession;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Classe responsável por resolver as permissões do usuário em permissões do singular
 *
 * @author Vinicius Nunes
 */
public class AuthorizationService implements Loggable {

    private static final String LIST_TASKS_PERMISSION_PREFIX = "LIST_TASKS";

    @Inject
    protected PermissionResolverService permissionResolverService;

    @Inject
    protected PetitionService<PetitionEntity, PetitionInstance> petitionService;

    @Inject
    @Named("peticionamentoUserDetailService")
    private SingularUserDetailsService peticionamentoUserDetailService;

    @Inject
    @Named("formConfigWithDatabase")
    private Optional<SFormConfig<String>> singularFormConfig;

    public void filterBoxWithPermissions(List<BoxConfigurationData> groupDTOs, String idUsuario) {
        List<SingularPermission> permissions = searchPermissions(idUsuario);

        for (Iterator<BoxConfigurationData> it = groupDTOs.iterator(); it.hasNext(); ) {
            BoxConfigurationData boxConfigurationMetadata = it.next();
            String permissionNeeded = boxConfigurationMetadata.getId().toUpperCase();
            if (!hasPermission(idUsuario, permissionNeeded, permissions)) {
                it.remove();
            } else {
                filterForms(boxConfigurationMetadata, permissions, idUsuario);
            }

        }
    }

    public void filterActions(String formType, Long petitionId, BoxItemActionList actions, String idUsuario) {
        List<SingularPermission> permissions = searchPermissions(idUsuario);
        filterActions(formType, petitionId, actions, idUsuario, permissions);
    }

    @SuppressWarnings("unchecked")
    private void filterActions(String formType, Long petitionId, BoxItemActionList actions, String idUsuario, List<SingularPermission> permissions) {
        PetitionAuthMetadataDTO petitionAuthMetadataDTO = null;
        if (petitionId != null) {
            petitionAuthMetadataDTO = petitionService.findPetitionAuthMetadata(petitionId);
        }
        for (Iterator<BoxItemAction> it = actions.iterator(); it.hasNext(); ) {
            BoxItemAction action = it.next();
            String permissionsNeeded;
            String typeAbbreviation = getFormSimpleName(formType);
            if (action.getFormAction() != null) {
                permissionsNeeded = buildPermissionKey(petitionAuthMetadataDTO, typeAbbreviation, action.getFormAction().name());
            } else {
                permissionsNeeded = buildPermissionKey(petitionAuthMetadataDTO, typeAbbreviation, action.getName());
            }
            if (!hasPermission(idUsuario, permissionsNeeded, permissions)) {
                it.remove();
            }
        }

    }

    public void filterActors(List<Actor> actors, Long petitionId, String actionName) {
        PetitionAuthMetadataDTO petitionAuthMetadataDTO = petitionService.findPetitionAuthMetadata(petitionId);
        if (actors != null && !actors.isEmpty()) {
            actors.removeIf(a -> !hasPermission(petitionAuthMetadataDTO, null, a.getCodUsuario(), actionName));
        }
    }

    public List<SingularPermission> filterListTaskPermissions(List<SingularPermission> permissions) {
        return permissions.stream().filter(p -> p != null && p.getSingularId() != null && p.getSingularId().startsWith(LIST_TASKS_PERMISSION_PREFIX)).collect(Collectors.toList());
    }


    private List<SingularPermission> searchPermissions(String userPermissionKey) {
        if (SingularSession.exists()) {
            SingularUserDetails userDetails = SingularSession.get().getUserDetails();
            if (userPermissionKey.equals(userDetails.getUserPermissionKey())) {
                if (CollectionUtils.isEmpty(userDetails.getPermissions())) {
                    userDetails.addPermissions(peticionamentoUserDetailService.searchPermissions((String) userDetails.getUserPermissionKey()));
                }
                return userDetails.getPermissions();
            }
        }
        return permissionResolverService.searchPermissions(userPermissionKey);
    }


    private void filterForms(BoxConfigurationData boxConfigurationMetadata, List<SingularPermission> permissions, String idUsuario) {
        for (Iterator<FormDTO> it = boxConfigurationMetadata.getForms().iterator(); it.hasNext(); ) {
            FormDTO form = it.next();
            String permissionNeeded = buildPermissionKey(null, form.getAbbreviation(), FormAction.FORM_FILL.name());
            if (!hasPermission(idUsuario, permissionNeeded, permissions)) {
                it.remove();
            }
        }
    }

    /**
     * Monta a chave de permissão do singular, não deve ser utilizado diretamente.
     *
     * @param petitionAuthMetadataDTO
     * @param formSimpleName
     * @param action
     * @return
     */
    private String buildPermissionKey(PetitionAuthMetadataDTO petitionAuthMetadataDTO, String formSimpleName, String action) {
        String permission = Joiner.on("_")
                .skipNulls()
                .join(
                        upperCaseOrNull(action),
                        upperCaseOrNull(formSimpleName),
                        getDefinitionKey(petitionAuthMetadataDTO),
                        getCurrentTaskAbbreviation(petitionAuthMetadataDTO)
                )
                .toUpperCase();
        if (getLogger().isTraceEnabled()) {
            getLogger().debug(String.format("Nome de permissão computada %s", permission));
        }
        return permission;
    }

    private String getDefinitionKey(PetitionAuthMetadataDTO petitionAuthMetadataDTO) {
        if (petitionAuthMetadataDTO != null) {
            return petitionAuthMetadataDTO.getDefinitionKey();
        }
        return null;
    }

    private String getCurrentTaskAbbreviation(PetitionAuthMetadataDTO petitionAuthMetadataDTO) {
        if (petitionAuthMetadataDTO != null) {
            return petitionAuthMetadataDTO.getCurrentTaskAbbreviation();
        }
        return null;
    }

    private String upperCaseOrNull(String string) {
        if (string != null) {
            return string.toUpperCase();
        }
        return null;
    }


    public boolean hasPermission(Long petitionId, String formType, String idUsuario, String action) {
        PetitionAuthMetadataDTO petitionAuthMetadataDTO = null;
        if (petitionId != null) {
            petitionAuthMetadataDTO = petitionService.findPetitionAuthMetadata(petitionId);
        }
        return hasPermission(petitionAuthMetadataDTO, formType, idUsuario, action);
    }

    private boolean hasPermission(PetitionAuthMetadataDTO petitionAuthMetadataDTO, String formType, String idUsuario, String action) {
        String formSimpleName = getFormSimpleName(formType);
        if (petitionAuthMetadataDTO != null) {
            formSimpleName = getFormSimpleName(petitionAuthMetadataDTO.getFormTypeAbbreviation());
        }
        return hasPermission(idUsuario, buildPermissionKey(petitionAuthMetadataDTO, formSimpleName, action));
    }

    private boolean hasPermission(String idUsuario, String permissionNeeded) {
        List<SingularPermission> permissions = searchPermissions(idUsuario);
        return hasPermission(idUsuario, permissionNeeded, permissions);
    }

    private String removeTask(String permissionId) {
        int idx = permissionId.lastIndexOf('_');
        if (idx > -1) {
            return permissionId.substring(0, idx);
        }
        return permissionId;
    }


    private boolean hasPermission(String idUsuario, String permissionNeeded, List<SingularPermission> permissions) {
        if (SingularProperties.get().isTrue(SingularProperties.DISABLE_AUTHORIZATION)) {
            return true;
        }

        if (permissions.stream().anyMatch(ps -> ps.getSingularId().equals(permissionNeeded))) {
            return true;
        }

        String definitionPermission = removeTask(permissionNeeded);
        if (permissions.stream().anyMatch(ps -> ps.getSingularId().equals(definitionPermission))) {
            return true;
        }

        getLogger().info(" Usuário logado {} não possui a permissão {} ", idUsuario, permissionNeeded);
        return false;
    }

    private String getFormSimpleName(String formTypeName) {
        if (StringUtils.isBlank(formTypeName)) {
            return null;
        }
        return singularFormConfig
                .flatMap(formConfig -> formConfig.getTypeLoader().loadType(formTypeName))
                .map(type -> type.getNameSimple())
                .map(String::toUpperCase)
                .orElse(null);
    }

}