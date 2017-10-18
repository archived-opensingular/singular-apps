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

package org.opensingular.server.commons.persistence.query;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BeanPath;
import com.querydsl.jpa.hibernate.HibernateQuery;
import org.hibernate.Query;
import org.hibernate.Session;
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
        where(whereClause.and(quickFilterWhereClause));
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