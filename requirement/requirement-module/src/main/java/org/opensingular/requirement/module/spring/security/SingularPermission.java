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

package org.opensingular.requirement.module.spring.security;

import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Classe que representa uma permissão do Singular.
 * Ela armazena tanto a representação do próprio singular,
 * quanto o identificador utilizado no reposítorio de autorização
 * do cliente.
 */
public class SingularPermission implements Serializable {

    public static final String SEPARATOR = "|$|";
    public static final String WILDCARD  = "*";
    private             String singularId;

    private Serializable internalId;

    private String action;
    private String formType;
    private String flowDefintionKey;
    private String taskAbbreviation;

    public SingularPermission() {
    }

    public SingularPermission(@Nonnull String singularId, @Nullable Serializable internalId) {
        setSingularId(singularId);
        setInternalId(internalId);
    }

    public SingularPermission(String action, String formSimpleName, String definitionKey, String currentTaskAbbreviation) {
        this(
                StringUtils.defaultString(action, WILDCARD)
                        + SEPARATOR
                        + StringUtils.defaultString(formSimpleName, WILDCARD)
                        + SEPARATOR
                        + StringUtils.defaultString(definitionKey, WILDCARD)
                        + SEPARATOR
                        + StringUtils.defaultString(currentTaskAbbreviation, WILDCARD),
                null);
    }


    private String getField(String[] values, int i) {
        if (values.length > i) {
            return values[i];
        }
        return null;
    }


    /**
     * @param s Permission to be matched
     * @return true if this permission matches the permission parameter {@param s} .
     */
    public boolean matchesPermission(SingularPermission s) {
        boolean match = true;
        match &= isEqual(this.action, s.action);
        match &= isEqual(this.formType, s.formType);
        match &= isEqual(this.flowDefintionKey, s.flowDefintionKey);
        match &= isEqual(this.taskAbbreviation, s.taskAbbreviation);
        return match;
    }

    private boolean isEqual(String candidate, String target) {
        return WILDCARD.equals(candidate)
                || WILDCARD.equals(target)
                || Objects.equals(candidate, target);
    }

    public String getSingularId() {
        return singularId;
    }

    public void setSingularId(String singularId) {
        this.singularId = singularId.toUpperCase();
        String[] values = this.singularId.split(Pattern.quote(SEPARATOR));
        action = getField(values, 0);
        formType = getField(values, 1);
        flowDefintionKey = getField(values, 2);
        taskAbbreviation = getField(values, 3);
    }

    public Serializable getInternalId() {
        return internalId;
    }

    public void setInternalId(Serializable internalId) {
        this.internalId = internalId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SingularPermission that = (SingularPermission) o;
        return Objects.equals(singularId, that.singularId) &&
                Objects.equals(internalId, that.internalId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(singularId, internalId);
    }

    @Override
    public String toString() {
        return "SingularPermission{" +
                "singularId='" + singularId + '\'' +
                ", internalId=" + internalId +
                '}';
    }
}
