package org.opensingular.server.commons.wicket.view.util;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.opensingular.server.commons.form.FormActions;
import org.opensingular.server.commons.wicket.view.form.FormPageExecutionContext;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Optional;

/**
 * Representa o contexto de execução de uma página de um módulo.
 * Armazena as informações passadas como parâmetros pelo server para o módulo.
 */
public class ActionContext implements Serializable, Cloneable {

    private static final String ACTION = "a";

    private static final String PETITION_ID = "k";

    private static final String PARENT_PETITION_ID = "p";

    private static final String FORM_NAME = "f";

    private static final String INSTANCE_ID = "i";

    private static final String PROCESS_GROUP_PARAM_NAME = "c";

    private static final String MENU_PARAM_NAME = "m";

    private static final String ITEM_PARAM_NAME = "t";

    private final static String FORM_VERSION_KEY = "v";

    private final static String REQUIREMENT_DEFINITION = "r";

    private final static String DIFF = "d";

    private LinkedHashMap<String, String> params = new LinkedHashMap<>();

    public ActionContext(String urlParameter) {
        this.params = ParameterHttpSerializer.decode(urlParameter);
    }

    public ActionContext() {
    }

    /**
     * Used by {@link this#clone()} method only
     * @param params
     */
    private ActionContext(LinkedHashMap<String, String> params) {
        this.params = params;
    }

    public Optional<Long> getPetitionId() {
        return Optional.ofNullable(this.params.get(PETITION_ID)).flatMap(s -> Optional.of(Long.valueOf(s)));
    }

    public ActionContext setPetitionId(Long petitionId) {
        this.params.put(PETITION_ID, String.valueOf(petitionId));
        return this;
    }

    public Optional<FormActions> getFormAction() {
        return Optional.ofNullable(this.params.get(ACTION)).flatMap(s -> Optional.of(FormActions.getById(Integer.valueOf(s))));
    }

    public ActionContext setFormAction(FormActions formAction) {
        this.params.put(ACTION, String.valueOf(formAction.getId()));
        return this;
    }

    public Optional<Long> getParentPetitionId() {
        return Optional.ofNullable(this.params.get(PARENT_PETITION_ID)).flatMap(s -> Optional.of(Long.valueOf(s)));
    }

    public ActionContext setParentPetitionId(Long parentPetitionId) {
        this.params.put(PARENT_PETITION_ID, String.valueOf(parentPetitionId));
        return this;
    }

    public Optional<String> getFormName() {
        return Optional.ofNullable(this.params.get(FORM_NAME));

    }

    public ActionContext setFormName(String formName) {
        this.params.put(FORM_NAME, formName);
        return this;
    }

    public Optional<Integer> getProcessInstanceId() {
        return Optional.ofNullable(this.params.get(INSTANCE_ID)).flatMap(s -> Optional.of(Integer.valueOf(s)));
    }

    public ActionContext setProcessInstanceId(Integer processInstanceId) {
        this.params.put(INSTANCE_ID, String.valueOf(processInstanceId));
        return this;
    }

    public Optional<String> getProcessGroupName() {
        return Optional.ofNullable(this.params.get(PROCESS_GROUP_PARAM_NAME));
    }

    public ActionContext setProcessGroupName(String processGroupName) {
        this.params.put(PROCESS_GROUP_PARAM_NAME, processGroupName);
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
        return Optional.ofNullable(this.params.get(DIFF)).flatMap(s -> Optional.of(Boolean.valueOf(s))).orElse(false);
    }

    public ActionContext setDiffEnabled(boolean enabled) {
        this.params.put(DIFF, String.valueOf(enabled));
        return this;
    }

    public String toURL() {
        return ParameterHttpSerializer.encode(params);
    }

    public ActionContext clone(){
        return new ActionContext(params);
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

    @Deprecated
    public static ActionContext fromFormConfig(FormPageExecutionContext config) {
        return new ActionContext();
    }
}
