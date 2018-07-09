package org.opensingular.requirement.module.persistence.query;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.hibernate.HibernateQuery;
import org.hibernate.Session;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.opensingular.lib.commons.lambda.IFunction;
import org.opensingular.requirement.module.persistence.filter.FilterToken;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class RequirementSearchQuery extends HibernateQuery<Map<String, Object>> {
    private final List<Expression<?>>                selects               = new ArrayList<>();
    private final List<IFunction<String, Predicate>> quickFilterConditions = new ArrayList<>();

    public RequirementSearchQuery(Session session) {
        super(session);
    }

    public RequirementSearchQuery addToSelect(Expression<?> exp) {
        selects.add(exp);
        return this;
    }

    public RequirementSearchQuery addCaseToSelect(Function<CaseBuilder, Expression<?>> caseExp) {
        selects.add(caseExp.apply(new CaseBuilder()));
        return this;
    }

    public RequirementSearchQuery addConditionToQuickFilter(IFunction<String, Predicate> conditionProvider) {
        quickFilterConditions.add(conditionProvider);
        return this;
    }

    public RequirementSearchQuery setDefaultOrder(OrderSpecifier<?> orderSpecifier){
        getMetadata().clearOrderBy();
        orderBy(orderSpecifier);
        return this;
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Serializable>> fetchMap() {
        return select(selects.toArray(new Expression<?>[]{}))
                .createQuery()
                .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                .list();
    }

    public void applyQuickFilter(List<FilterToken> tokens) {
        BooleanBuilder filterBooleanBuilder = new BooleanBuilder();
        for (FilterToken token : tokens) {
            BooleanBuilder tokenBooleanBuilder = new BooleanBuilder();
            for (String filter : token.getAllPossibleMatches()) {
                BooleanBuilder tokenExpression = new BooleanBuilder();
                quickFilterConditions.stream().map(i -> i.apply(filter)).forEach(tokenExpression::or);
                tokenBooleanBuilder.or(tokenExpression);
            }
            filterBooleanBuilder.and(tokenBooleanBuilder);
        }
        where(filterBooleanBuilder);
    }

}