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

package org.opensingular.server.commons.wicket.view.form;

import org.apache.commons.lang3.StringUtils;
import org.opensingular.flow.core.ProcessDefinition;
import org.opensingular.form.wicket.enums.AnnotationMode;
import org.opensingular.form.wicket.enums.ViewMode;
import org.opensingular.server.commons.flow.LazyFlowDefinitionResolver;
import org.opensingular.server.commons.form.FormActions;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class FormPageConfig implements Serializable {

    private FormActions                        formAction;
    private Long                               petitionId;
    private String                             formType;
    private Map<String, Serializable>          contextParams = new HashMap<>();
    private LazyFlowDefinitionResolver         lazyFlowDefinitionResolver;
    private Class<? extends ProcessDefinition> processDefinition;
    private Long                               formVersionPK;
    private Long                             parentPetitionId;
    private boolean                            diff;
    private Map<String, String>                additionalParams = new HashMap<>();

    private FormPageConfig() {
        formAction = FormActions.FORM_VIEW;
    }

    private static FormPageConfig newConfig(String formType,
                                            String petitionId,
                                            FormActions formAction,
                                            Long formVersionPK,
                                            String parentPetitionId) {
        final FormPageConfig cfg = new FormPageConfig();
        cfg.formType = formType;
        cfg.petitionId = StringUtils.isBlank(petitionId) ? null : Long.valueOf(petitionId);
        cfg.formAction = formAction;
        cfg.formVersionPK = formVersionPK;
        cfg.parentPetitionId = StringUtils.isBlank(parentPetitionId) ? null : Long.valueOf(parentPetitionId);
        return cfg;
    }

    public static FormPageConfig newConfig(String petitionId,
                                           String formType,
                                           FormActions formAction,
                                           Long formVersionPK,
                                           String parentPetitionId,
                                           Class<? extends ProcessDefinition> processDefinition) {
        final FormPageConfig cfg = newConfig(formType, petitionId, formAction, formVersionPK, parentPetitionId);
        cfg.processDefinition = processDefinition;
        return cfg;
    }


    public static FormPageConfig newConfig(String formType,
                                           String petitionId,
                                           FormActions formAction,
                                           Long formVersionPK,
                                           String parentPetitionId,
                                           LazyFlowDefinitionResolver lazyFlowDefinitionResolver) {
        final FormPageConfig cfg = newConfig(formType, petitionId, formAction, formVersionPK, parentPetitionId);
        cfg.lazyFlowDefinitionResolver = lazyFlowDefinitionResolver;
        return cfg;
    }

    public static FormPageConfig newConfig(String formType,
                                           String petitionId,
                                           FormActions formAction,
                                           Long formVersionPK,
                                           String parentPetitionId,
                                           LazyFlowDefinitionResolver lazyFlowDefinitionResolver,
                                           boolean diff) {
        final FormPageConfig cfg = newConfig(formType, petitionId, formAction, formVersionPK, parentPetitionId, lazyFlowDefinitionResolver);
        cfg.diff = diff;
        return cfg;
    }

    public ViewMode getViewMode() {
        return formAction.getViewMode();
    }

    public AnnotationMode getAnnotationMode() {
        return formAction.getAnnotationMode();
    }

    public Long getPetitionId() {
        return petitionId;
    }

    public void setPetitionId(Long petitionId) {
        this.petitionId = petitionId;
    }

    public String getFormType() {
        return formType;
    }

    public void setFormType(String formType) {
        this.formType = formType;
    }

    public Map<String, Serializable> getContextParams() {
        return contextParams;
    }

    public Class<? extends ProcessDefinition> getProcessDefinition() {
        return processDefinition;
    }

    public void setProcessDefinition(Class<? extends ProcessDefinition> processDefinition) {
        this.processDefinition = processDefinition;
    }

    public LazyFlowDefinitionResolver getLazyFlowDefinitionResolver() {
        return lazyFlowDefinitionResolver;
    }

    public void setLazyFlowDefinitionResolver(LazyFlowDefinitionResolver lazyFlowDefinitionResolver) {
        this.lazyFlowDefinitionResolver = lazyFlowDefinitionResolver;
    }

    public boolean isWithLazyProcessResolver() {
        return lazyFlowDefinitionResolver != null;
    }

    public boolean containsProcessDefinition() {
        return processDefinition != null;
    }

    public FormActions getFormAction() {
        return formAction;
    }

    public Long getFormVersionPK() {
        return formVersionPK;
    }

    public Long getParentPetitionId() {
        return parentPetitionId;
    }

    public boolean isDiff() {
        return diff;
    }

    public void setDiff(boolean diff) {
        this.diff = diff;
    }

    public Map<String, String> getAdditionalParams() {
        return Collections.unmodifiableMap(additionalParams);
    }

    public FormPageConfig addAdditionalParam(String key, String value) {
        additionalParams.put(key, value);
        return this;
    }

}