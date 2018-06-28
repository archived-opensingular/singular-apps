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

package org.opensingular.requirement.commons.persistence.query;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import org.opensingular.flow.persistence.entity.QVariableInstanceEntity;
import org.opensingular.requirement.commons.persistence.context.RequirementSearchAliases;
import org.opensingular.requirement.commons.persistence.context.RequirementSearchContext;

import javax.annotation.Nonnull;

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
        this( variableName, queryAlias, TO_CHAR_TEMPLATE);
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
        RequirementSearchQuery query = ctx.getQuery();
        RequirementSearchAliases $ = ctx.getAliases();
        createSelect(ctx, variableEntity);
        query.leftJoin($.flowInstance.variables, variableEntity).on(variableEntity.name.eq(variableName));
        ctx.extendQuickFilterWhereClause((token, tokenExpression) -> tokenExpression.or(toChar(variableEntity).likeIgnoreCase(token)));
    }

    protected void createSelect(RequirementSearchContext ctx, QVariableInstanceEntity qVariableInstance) {
        ctx.getQuery().getSelect().add(toChar(qVariableInstance).as(queryAlias));
    }

    @Nonnull
    protected StringTemplate toChar(QVariableInstanceEntity var) {
        return Expressions.stringTemplate(toCharTemplate, var.value);
    }
}