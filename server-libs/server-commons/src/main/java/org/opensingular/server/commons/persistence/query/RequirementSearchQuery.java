package org.opensingular.server.commons.persistence.query;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.BeanPath;
import com.querydsl.jpa.hibernate.HibernateQuery;
import org.hibernate.Query;
import org.hibernate.Session;
import org.opensingular.server.commons.persistence.entity.form.PetitionEntity;
import org.opensingular.server.commons.persistence.entity.form.QPetitionEntity;
import org.opensingular.server.commons.query.SelectBuilder;

import java.util.Map;

public class RequirementSearchQuery extends HibernateQuery<Map<String, Object>> {

    private SelectBuilder  select                 = new SelectBuilder();
    private BooleanBuilder whereClause            = new BooleanBuilder();
    private BooleanBuilder quickFilterWhereClause = new BooleanBuilder();
    private BeanPath<?>    countPath              = null;

    public RequirementSearchQuery(Session session) {
        super(session);
    }

    public BooleanBuilder getWhereClause() {
        return whereClause;
    }

    public Query toHibernateQuery(Boolean count) {
        if (Boolean.TRUE.equals(count)) {
            select(countPath.count());
        } else {
            select(select.build());
        }
        where(whereClause.and(quickFilterWhereClause.getValue()));
        return createQuery();
    }

    public BooleanBuilder getQuickFilterWhereClause() {
        return quickFilterWhereClause;
    }

    public SelectBuilder getSelect() {
        return select;
    }

    public void countBy(BeanPath<?> entityPath) {
        this.countPath = entityPath;
    }

}