package org.opensingular.server.commons.persistence.context;


import org.hibernate.Session;
import org.opensingular.server.commons.persistence.filter.QuickFilter;
import org.opensingular.server.commons.persistence.query.RequirementSearchQuery;
import org.opensingular.server.commons.persistence.requirement.RequirementSearchExtender;
import org.opensingular.server.commons.spring.security.SingularPermission;

import java.util.ArrayList;
import java.util.List;

public class RequirementSearchContext {

    private final QuickFilter              quickFilter;
    private final List<SingularPermission> permissions;
    private final RequirementSearchAliases aliases;

    private Boolean evaluatePermissions = Boolean.FALSE;
    private Boolean count               = Boolean.FALSE;

    private RequirementSearchQuery          query;
    private List<RequirementSearchExtender> extenders;

    public RequirementSearchContext(QuickFilter quickFilter) {
        this.quickFilter = quickFilter;
        this.permissions = new ArrayList<>();
        aliases = new RequirementSearchAliases();
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

    public RequirementSearchContext setEvaluatePermissions(Boolean evaluatePermissions) {
        this.evaluatePermissions = evaluatePermissions;
        return this;
    }

    public Boolean getCount() {
        return count;
    }

    public RequirementSearchContext setCount(Boolean count) {
        this.count = count;
        return this;
    }

    public RequirementSearchContext addPermissions(List<SingularPermission> permissions) {
        this.permissions.addAll(permissions);
        return this;
    }

    public RequirementSearchContext setQuery(RequirementSearchQuery query) {
        this.query = query;
        return this;
    }

    public RequirementSearchQuery getQuery() {
        return query;
    }

    public List<RequirementSearchExtender> getExtenders() {
        return extenders;
    }

    public RequirementSearchContext setExtenders(List<RequirementSearchExtender> extenders) {
        this.extenders = extenders;
        return this;
    }

    public RequirementSearchAliases getAliases() {
        return aliases;
    }
}