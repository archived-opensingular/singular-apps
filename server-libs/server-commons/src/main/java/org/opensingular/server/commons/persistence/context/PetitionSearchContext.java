package org.opensingular.server.commons.persistence.context;


import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.DslExpression;
import com.querydsl.jpa.hibernate.HibernateQuery;
import org.hibernate.Session;
import org.opensingular.server.commons.persistence.filter.QuickFilter;
import org.opensingular.server.commons.spring.security.SingularPermission;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PetitionSearchContext {

    private final QuickFilter              quickFilter;
    private final List<SingularPermission> permissions;

    private Boolean evaluatePermissions = Boolean.FALSE;
    private Boolean count               = Boolean.FALSE;

    private PetitionSearchAliases         petitionSearchAliases = new PetitionSearchAliases();
    private List<BooleanExpression>       whereClasuses         = new ArrayList<>();
    private Map<String, DslExpression<?>> selects               = new LinkedHashMap<>();

    private HibernateQuery<Map<String, Object>> query;

    public PetitionSearchContext(QuickFilter quickFilter) {
        this.quickFilter = quickFilter;
        this.permissions = new ArrayList<>();
    }

    public QuickFilter getQuickFilter() {
        return quickFilter;
    }

    public List<SingularPermission> getPermissions() {
        return permissions;
    }

    public Boolean getEvaluatePermissions() {
        return evaluatePermissions;
    }

    public PetitionSearchContext setEvaluatePermissions(Boolean evaluatePermissions) {
        this.evaluatePermissions = evaluatePermissions;
        return this;
    }

    public Boolean getCount() {
        return count;
    }

    public PetitionSearchContext setCount(Boolean count) {
        this.count = count;
        return this;
    }

    public PetitionSearchContext addPermissions(List<SingularPermission> permissions) {
        this.permissions.addAll(permissions);
        return this;
    }

    public PetitionSearchAliases getPetitionSearchAliases() {
        return petitionSearchAliases;
    }

    public HibernateQuery<Map<String, Object>> createQuery(Session session) {
        return query = new HibernateQuery<>(session);
    }

    public List<BooleanExpression> getWhereClasuses() {
        return whereClasuses;
    }

    public Map<String, DslExpression<?>> getSelects() {
        return selects;
    }

    public HibernateQuery<Map<String, Object>> getQuery() {
        return query;
    }
}