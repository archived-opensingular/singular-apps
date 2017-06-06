package org.opensingular.server.commons.persistence.context;


import org.opensingular.server.commons.persistence.filter.QuickFilter;
import org.opensingular.server.commons.spring.security.SingularPermission;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PetitionSearchQuery {

    private final QuickFilter              quickFilter;
    private final List<SingularPermission> permissions;
    private final StringBuilder            hqlQuery;
    private final Map<String, Object>      params;

    private Boolean evaluatePermissions = Boolean.FALSE;
    private Boolean count               = Boolean.FALSE;

    public PetitionSearchQuery(QuickFilter quickFilter) {
        this.quickFilter = quickFilter;
        this.hqlQuery = new StringBuilder();
        this.permissions = new ArrayList<>();
        this.params = new HashMap<>();
    }

    public QuickFilter getQuickFilter() {
        return quickFilter;
    }

    public List<SingularPermission> getPermissions() {
        return permissions;
    }

    public String getHqlQueryString() {
        return hqlQuery.toString();
    }

    public Boolean getEvaluatePermissions() {
        return evaluatePermissions;
    }

    public PetitionSearchQuery setEvaluatePermissions(Boolean evaluatePermissions) {
        this.evaluatePermissions = evaluatePermissions;
        return this;
    }

    public Boolean getCount() {
        return count;
    }

    public PetitionSearchQuery setCount(Boolean count) {
        this.count = count;
        return this;
    }

    public PetitionSearchQuery addPermissions(List<SingularPermission> permissions) {
        this.permissions.addAll(permissions);
        return this;
    }

    public PetitionSearchQuery addParam(String param, Object value) {
        this.params.put(param, value);
        return this;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public PetitionSearchQuery append(String str) {
        this.hqlQuery.append(str);
        return this;
    }

}