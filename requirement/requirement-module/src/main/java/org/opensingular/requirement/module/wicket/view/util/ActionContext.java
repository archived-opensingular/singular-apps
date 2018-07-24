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

package org.opensingular.requirement.module.wicket.view.util;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.requirement.module.form.FormAction;
import org.opensingular.requirement.module.wicket.view.form.AbstractFormPage;
import org.opensingular.requirement.module.wicket.view.form.FormPageExecutionContext;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Optional;

/**
 * Representa o contexto de execução de uma página de um módulo.
 * Armazena as informações passadas como parâmetros pelo server para o módulo.
 */
public class ActionContext implements Serializable, Loggable {

    public static final String ACTION = "a";

    public static final String REQUIREMENT_ID = "k";

    public static final String PARENT_REQUIREMENT_ID = "p";

    public static final String FORM_NAME = "f";

    public static final String INSTANCE_ID = "i";

    public static final String MENU_PARAM_NAME = "m";

    public static final String ITEM_PARAM_NAME = "t";

    public static final String REQUIREMENT_DEFINITION_KEY = "r";

    public final static String FORM_VERSION_KEY = "v";

    public final static String DIFF = "d";

    public final static String FORM_PAGE_CLASS = "w";

    public final static String INHERIT_PARENT_FORM_DATA = "b";

    private LinkedHashMap<String, String> params = new LinkedHashMap<>();

    public ActionContext(String urlParameter) {
        this.params = ParameterHttpSerializer.decode(urlParameter);
    }

    public ActionContext() {
    }

    public ActionContext(ActionContext actionContext) {
        this.params = actionContext.params;
    }


    public static ActionContext fromFormConfig(FormPageExecutionContext config) {
        return config.copyOfInnerActionContext();
    }

    public Optional<Long> getRequirementId() {
        return Optional.ofNullable(this.params.get(REQUIREMENT_ID)).flatMap(s -> Optional.of(Long.valueOf(s)));
    }

    public ActionContext setRequirementId(Long requirementId) {
        this.params.put(REQUIREMENT_ID, String.valueOf(requirementId));
        return this;
    }

    public Optional<FormAction> getFormAction() {
        return Optional.ofNullable(this.params.get(ACTION)).flatMap(s -> Optional.of(FormAction.getById(Integer.valueOf(s))));
    }

    public ActionContext setFormAction(FormAction formAction) {
        this.params.put(ACTION, String.valueOf(formAction.getId()));
        return this;
    }

    public Optional<Long> getParentRequirementId() {
        return Optional.ofNullable(this.params.get(PARENT_REQUIREMENT_ID)).flatMap(s -> Optional.of(Long.valueOf(s)));
    }

    public Optional<Boolean> getInheritParentFormData() {
        return Optional.ofNullable(this.params.get(INHERIT_PARENT_FORM_DATA)).map(Boolean::valueOf);
    }

    public ActionContext setParentRequirementId(Long parentRequirementId) {
        this.params.put(PARENT_REQUIREMENT_ID, String.valueOf(parentRequirementId));
        return this;
    }

    public Optional<String> getRequirementDefinitionKey() {
        return Optional.ofNullable(this.params.get(REQUIREMENT_DEFINITION_KEY)).map(String::valueOf);
    }

    public void setRequirementDefinitionId(String requirementDefinitionKey) {
        this.params.put(REQUIREMENT_DEFINITION_KEY, String.valueOf(requirementDefinitionKey));
    }

    public Optional<String> getFormName() {
        return Optional.ofNullable(this.params.get(FORM_NAME));

    }

    public ActionContext setFormName(String formName) {
        this.params.put(FORM_NAME, formName);
        return this;
    }

    public Optional<Integer> getFlowInstanceId() {
        return Optional.ofNullable(this.params.get(INSTANCE_ID)).flatMap(s -> Optional.of(Integer.valueOf(s)));
    }

    public ActionContext setFlowInstanceId(Integer flowInstanceId) {
        this.params.put(INSTANCE_ID, String.valueOf(flowInstanceId));
        return this;
    }

    public Optional<String> getMenuLabel() {
        return Optional.ofNullable(this.params.get(MENU_PARAM_NAME));
    }

    public ActionContext setMenuLabel(String menuLabel) {
        this.params.put(MENU_PARAM_NAME, menuLabel);
        return this;
    }

    public Optional<String> getParam(String key) {
        return Optional.ofNullable(this.params.get("$" + key));
    }

    public ActionContext setParam(String key, String value) {
        this.params.put("$" + key, value);
        return this;
    }

    public Optional<String> getSelectedMenuItem() {
        return Optional.ofNullable(this.params.get(ITEM_PARAM_NAME));
    }

    public ActionContext setSelectedMenuItem(String menuItemLabel) {
        this.params.put(ITEM_PARAM_NAME, menuItemLabel);
        return this;
    }

    public Optional<Long> getFormVersionId() {
        return Optional.ofNullable(this.params.get(FORM_VERSION_KEY)).flatMap(s -> Optional.of(Long.valueOf(s)));
    }

    public ActionContext setFormVersionId(Long formVersionId) {
        this.params.put(FORM_VERSION_KEY, String.valueOf(formVersionId));
        return this;
    }

    public boolean getDiffEnabled() {
        return Optional.ofNullable(this.params.get(DIFF)).flatMap(s -> Optional.of(Boolean.valueOf(s))).orElse(Boolean.FALSE);
    }

    public ActionContext setDiffEnabled(boolean enabled) {
        this.params.put(DIFF, String.valueOf(enabled));
        return this;
    }

    public String toURL() {
        return ParameterHttpSerializer.encode(params);
    }

    public Optional<Class<? extends AbstractFormPage<?>>> getFormPageClass() {
        String formPageClassName = params.get(FORM_PAGE_CLASS);
        if (formPageClassName != null) {
            try {
                return Optional.ofNullable((Class<? extends AbstractFormPage<?>>) Class.forName(formPageClassName));
            } catch (ClassNotFoundException e) {
                getLogger().info("Nenhuma classe fornecida", e);
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ActionContext that = (ActionContext) o;

        return new EqualsBuilder()
                .append(params, that.params)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(params)
                .toHashCode();
    }
}
