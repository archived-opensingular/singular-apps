package org.opensingular.server.commons.persistence.requirement;

import com.querydsl.core.types.dsl.Expressions;
import org.opensingular.flow.persistence.entity.QVariableInstanceEntity;
import org.opensingular.server.commons.persistence.context.RequirementSearchContext;
import org.opensingular.server.commons.persistence.filter.QuickFilter;
import org.opensingular.server.commons.persistence.query.RequirementSearchQuery;

/**
 * Adiciona uma variavel nomeada a consulta de requerimentos
 */
public class FlowVariableRequirementSearchExtender implements RequirementSearchExtender {

    private static final String TO_CHAR_TEMPLATE = "to_char({0})";

    private final String variableName;
    private final String queryAlias;

    public FlowVariableRequirementSearchExtender(String variableName, String queryAlias) {
        this.variableName = variableName;
        this.queryAlias = queryAlias;
    }

    @Override
    public void extend(RequirementSearchContext context) {
        QVariableInstanceEntity var   = new QVariableInstanceEntity(variableName);
        RequirementSearchQuery  query = context.getQuery();

        query.leftJoin(query.getAliases().processInstance.variables, var).on(var.name.eq(variableName));

        if (Boolean.FALSE.equals(context.getCount())) {
            query.getSelectBuilder()
                    .add(Expressions.stringTemplate(TO_CHAR_TEMPLATE, var.value).as(queryAlias));
        }

        QuickFilter quickFilter = context.getQuickFilter();
        if (context.getQuickFilter().hasFilter()) {
            query.getQuickFilterWhereBuilder()
                    .or(Expressions.stringTemplate(TO_CHAR_TEMPLATE, var.value).likeIgnoreCase(quickFilter.filterWithAnywhereMatchMode()))
                    .or(Expressions.stringTemplate(TO_CHAR_TEMPLATE, var.value).likeIgnoreCase(quickFilter.numberAndLettersFilterWithAnywhereMatchMode()));
        }
    }

}