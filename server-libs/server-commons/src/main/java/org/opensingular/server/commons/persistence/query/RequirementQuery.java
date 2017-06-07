package org.opensingular.server.commons.persistence.query;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.DslExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.hibernate.HibernateQuery;
import org.hibernate.Query;
import org.hibernate.Session;
import org.opensingular.flow.persistence.entity.QVariableInstanceEntity;
import org.opensingular.server.commons.persistence.context.PetitionSearchAliases;
import org.opensingular.server.commons.persistence.filter.QuickFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class RequirementQuery extends HibernateQuery<Map<String, Object>> {

    private final QuickFilter quickFilter;
    private final Boolean     count;

    private PetitionSearchAliases requirementAliases = new PetitionSearchAliases();
    private List<Expression<?>>   selectClauses      = new ArrayList<>();
    private BooleanBuilder        whereClause        = new BooleanBuilder();
    private BooleanBuilder        quickFilterClause  = new BooleanBuilder();
    private String                toCharTemplate     = "to_char({0})";

    public RequirementQuery(Session session, QuickFilter quickFilter, Boolean count) {
        super(session);
        this.quickFilter = quickFilter;
        this.count = count;
    }

    public PetitionSearchAliases getRequirementAliases() {
        return requirementAliases;
    }

    public RequirementQuery addSelect(DslExpression<?> exp) {
        selectClauses.add(exp);
        return this;
    }

    public RequirementQuery addSelectCase(Function<CaseBuilder, DslExpression<?>> builder) {
        selectClauses.add(builder.apply(new CaseBuilder()));
        return this;
    }

    public RequirementQuery addFlowVariable(String flowVariableName, String alias) {
        QVariableInstanceEntity var = new QVariableInstanceEntity(flowVariableName);
        leftJoin(requirementAliases.processInstance.variables, var).on(var.name.eq(flowVariableName));
        if (Boolean.FALSE.equals(count)) {
            addSelect(Expressions.stringTemplate(toCharTemplate, var.value).as(alias));
        }
        if (quickFilter.hasFilter()) {
            quickFilterClause.or(Expressions.stringTemplate(toCharTemplate, var.value).likeIgnoreCase(quickFilter.filterWithAnywhereMatchMode()));
            quickFilterClause.or(Expressions.stringTemplate(toCharTemplate, var.value).likeIgnoreCase(quickFilter.numberAndLettersFilterWithAnywhereMatchMode()));
        }
        return this;
    }

    public BooleanBuilder getWhereClause() {
        return whereClause;
    }

    public Query toHibernateQuery() {
        DslExpression<?>[] selectsArray = selectClauses.toArray(new DslExpression<?>[]{});
        if (selectsArray.length > 1) {
            select(selectsArray);
        } else if (selectsArray.length == 1) {
            select(selectsArray[0]);
        }
        where(whereClause.and(quickFilterClause.getValue()));
        return createQuery();
    }

    public BooleanBuilder getQuickFilterClause() {
        return quickFilterClause;
    }

    public String getToCharTemplate() {
        return toCharTemplate;
    }
}