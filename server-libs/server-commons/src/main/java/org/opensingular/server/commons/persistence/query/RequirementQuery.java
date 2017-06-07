package org.opensingular.server.commons.persistence.query;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.DslExpression;
import com.querydsl.jpa.hibernate.HibernateQuery;
import org.hibernate.Query;
import org.hibernate.Session;
import org.opensingular.server.commons.persistence.context.PetitionSearchAliases;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class RequirementQuery extends HibernateQuery<Map<String, Object>> {

    private PetitionSearchAliases requirementAliases = new PetitionSearchAliases();
    private List<Expression<?>>   selectClauses      = new ArrayList<>();
    private BooleanBuilder        whereClause        = new BooleanBuilder();
    private BooleanBuilder        quickFilterClause  = new BooleanBuilder();

    public RequirementQuery(Session session) {
        super(session);
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
}