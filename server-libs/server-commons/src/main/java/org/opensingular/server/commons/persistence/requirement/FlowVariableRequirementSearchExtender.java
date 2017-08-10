package org.opensingular.server.commons.persistence.requirement;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import org.opensingular.flow.persistence.entity.QVariableInstanceEntity;
import org.opensingular.server.commons.persistence.context.RequirementSearchAliases;
import org.opensingular.server.commons.persistence.context.RequirementSearchContext;
import org.opensingular.server.commons.persistence.filter.FilterToken;
import org.opensingular.server.commons.persistence.filter.QuickFilter;
import org.opensingular.server.commons.persistence.query.RequirementSearchQuery;

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
    }

    @Override
    public void extend(@Nonnull RequirementSearchContext context) {
        QVariableInstanceEntity variableEntity = new QVariableInstanceEntity(variableName);
        RequirementSearchQuery query = context.getQuery();
        RequirementSearchAliases $ = context.getAliases();

        createSelect(variableEntity, context);

        query.leftJoin($.processInstance.variables, variableEntity).on(variableEntity.name.eq(variableName));

        QuickFilter quickFilter = context.getQuickFilter();
        if (context.getQuickFilter().hasFilter()) {
            BooleanBuilder filterBooleanBuilder = new BooleanBuilder();
            for (FilterToken token : quickFilter.listFilterTokens()) {
                BooleanBuilder tokenBooleanBuilder = new BooleanBuilder();
                for (String filter : token.getAllPossibleMatches()) {
                    tokenBooleanBuilder.or(toChar(variableEntity).likeIgnoreCase(filter));
                }
                filterBooleanBuilder.and(tokenBooleanBuilder);
            }
            query.getQuickFilterWhereClause().or(filterBooleanBuilder);
        }
    }

    protected void createSelect(QVariableInstanceEntity variableEntity, RequirementSearchContext context) {
        context.getQuery()
                .getSelect()
                .add(toChar(variableEntity).as(queryAlias));
    }

    @Nonnull
    protected StringTemplate toChar(QVariableInstanceEntity var) {
        return Expressions.stringTemplate(toCharTemplate, var.value);
    }
}