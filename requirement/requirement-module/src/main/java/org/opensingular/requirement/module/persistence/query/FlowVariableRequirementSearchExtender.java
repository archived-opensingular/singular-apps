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

package org.opensingular.requirement.module.persistence.query;

import javax.annotation.Nonnull;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import org.opensingular.flow.persistence.entity.QVariableInstanceEntity;
import org.opensingular.form.SInstance;
import org.opensingular.requirement.module.persistence.context.RequirementSearchContext;

/**
 * Adiciona uma variavel nomeada a consulta de requerimentos
 */
public class FlowVariableRequirementSearchExtender implements RequirementSearchExtender {
    public static final String TO_CHAR_TEMPLATE = "to_char({0})";
    public static final String TO_CHAR_DATE_TEMPLATE = "to_date(to_char({0}), 'dd/MM/yyyy')";

    private final String variableName;
    private final String queryAlias;
    private final String toCharTemplate;
    private final QVariableInstanceEntity variableEntity;

    public FlowVariableRequirementSearchExtender(@Nonnull String variableName,
                                                 @Nonnull String queryAlias) {
        this(variableName, queryAlias, TO_CHAR_TEMPLATE);
    }

    public FlowVariableRequirementSearchExtender(@Nonnull String variableName,
                                                 @Nonnull String queryAlias,
                                                 @Nonnull String toCharTemplate) {
        this.variableName = variableName;
        this.queryAlias = queryAlias;
        this.toCharTemplate = toCharTemplate;
        this.variableEntity = new QVariableInstanceEntity(variableName);
    }

    @Override
    public void extend(RequirementSearchContext ctx) {
        RequirementSearchAliases $ = ctx.getAliases();
        ctx.getQuery()
                .addToSelect(toChar(variableEntity).as(queryAlias));
        ctx.getQuery()
                .leftJoin($.flowInstance.variables, variableEntity).on(variableEntity.name.eq(variableName));
        ctx.getQuery()
                .addConditionToQuickFilter((token) -> new BooleanBuilder().or(toChar(variableEntity).likeIgnoreCase(token)));

        SInstance advancedFilterInstance = ctx.getBoxFilter().getAdvancedFilterInstance();
        if (advancedFilterInstance != null && advancedFilterInstance.isNotEmptyOfData()) {
            advancedFilterInstance.getChildren()
                    .stream()
                    .filter(SInstance::isNotEmptyOfData)
                    .filter(i -> queryAlias.equals(i.getName()))
                    .forEach(i -> ctx.getQuery().where(toChar(variableEntity).likeIgnoreCase((String) i.getValue())));
        }
    }

    @Nonnull
    protected StringTemplate toChar(QVariableInstanceEntity var) {
        return Expressions.stringTemplate(toCharTemplate, var.value);
    }

}