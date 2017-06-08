package org.opensingular.server.commons.persistence.query;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Expression;
import com.querydsl.jpa.hibernate.HibernateQuery;
import org.hibernate.Query;
import org.hibernate.Session;
import org.opensingular.server.commons.persistence.context.RequirementSearchAliases;
import org.opensingular.server.commons.query.SelectBuilder;

import java.util.Map;

public class RequirementSearchQuery extends HibernateQuery<Map<String, Object>> {

    private RequirementSearchAliases aliases                 = new RequirementSearchAliases();
    private SelectBuilder            selectBuilder           = new SelectBuilder();
    private BooleanBuilder           whereBuilder            = new BooleanBuilder();
    private BooleanBuilder           quickFilterWhereBuilder = new BooleanBuilder();

    public RequirementSearchQuery(Session session) {
        super(session);
    }

    public RequirementSearchAliases getAliases() {
        return aliases;
    }

    public BooleanBuilder getWhereBuilder() {
        return whereBuilder;
    }

    public Query toHibernateQuery() {
        Expression<?>[] columnsArray = selectBuilder.build();
        if (columnsArray.length > 1) {
            select(columnsArray);
        } else if (columnsArray.length == 1) {
            select(columnsArray[0]);
        }
        where(whereBuilder.and(quickFilterWhereBuilder.getValue()));
        return createQuery();
    }

    public BooleanBuilder getQuickFilterWhereBuilder() {
        return quickFilterWhereBuilder;
    }

    public SelectBuilder getSelectBuilder() {
        return selectBuilder;
    }
}