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

package org.opensingular.requirement.module.service;

import org.opensingular.flow.persistence.entity.ModuleEntity;
import org.opensingular.form.SType;
import org.opensingular.form.persistence.entity.FormTypeEntity;
import org.opensingular.form.service.FormTypeService;
import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.requirement.module.exception.SingularServerException;
import org.opensingular.requirement.module.form.SingularServerSpringTypeLoader;
import org.opensingular.requirement.module.persistence.dao.form.RequirementDefinitionDAO;
import org.opensingular.flow.persistence.dao.ModuleDAO;
import org.opensingular.requirement.module.persistence.entity.form.RequirementDefinitionEntity;
import org.opensingular.requirement.module.SingularRequirement;
import org.opensingular.requirement.module.wicket.SingularSession;
import org.opensingular.requirement.module.SingularModule;
import org.opensingular.requirement.module.SingularModuleConfigurationBean;
import org.opensingular.requirement.module.SingularRequirementRef;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;
import java.net.URL;

@Named
@Transactional
public class ModuleService implements Loggable {

    @Inject
    private FormTypeService formTypeService;

    @Inject
    private RequirementDefinitionDAO<RequirementDefinitionEntity> requirementDefinitionDAO;

    @Inject
    private ModuleDAO moduleDAO;

    @Inject
    private SingularModuleConfigurationBean singularModuleConfiguration;

    @Inject
    private SingularServerSpringTypeLoader singularServerSpringTypeLoader;

    /**
     * Persiste se necessário o RequirementDefinitionEntity
     * e atualiza no ref o valor que está em banco.
     *
     * @param ref - o ref a partir do qual o {@link RequirementDefinitionEntity} será criado
     */
    public void save(SingularRequirementRef ref) {
        Class<? extends SType> mainForm = ref.getRequirement().getMainForm();
        SType<?>               type     = singularServerSpringTypeLoader.loadTypeOrException(mainForm);
        FormTypeEntity         formType = formTypeService.findFormTypeEntity(type);

        RequirementDefinitionEntity requirementDefinitionEntity = getOrCreateRequirementDefinition(ref.getRequirement(), formType);
        requirementDefinitionDAO.save(requirementDefinitionEntity);
        ref.setRequirementDefinitionEntity(requirementDefinitionEntity);
    }

    private RequirementDefinitionEntity getOrCreateRequirementDefinition(SingularRequirement singularRequirement, FormTypeEntity formType) {
        ModuleEntity                module                      = getModule();
        RequirementDefinitionEntity requirementDefinitionEntity = requirementDefinitionDAO.findByModuleAndName(module, formType);

        if (requirementDefinitionEntity == null) {
            requirementDefinitionEntity = new RequirementDefinitionEntity();
            requirementDefinitionEntity.setFormType(formType);
            requirementDefinitionEntity.setModule(module);
            requirementDefinitionEntity.setName(singularRequirement.getName());
        }

        return requirementDefinitionEntity;
    }

    /**
     * Retorna o módulo a que este código pertence.
     *
     * @return o módulo
     */
    public ModuleEntity getModule() {
        SingularModule module = singularModuleConfiguration.getModule();
        return moduleDAO.findOrException(module.abbreviation());
    }

    public String getBaseUrl() {
        return getModuleContext() + SingularSession.get().getServerContext().getUrlPath();
    }


    private String getModuleContext() {
        final String groupConnectionURL = getModule().getConnectionURL();
        try {
            final String path    = new URL(groupConnectionURL).getPath();
            if (path.endsWith("/")){
                return path.substring(0, path.length() - 1);
            } else {
                return path;
            }
        } catch (Exception e) {
            throw SingularServerException.rethrow(String.format("Erro ao tentar fazer o parse da URL: %s", groupConnectionURL), e);
        }
    }
}
