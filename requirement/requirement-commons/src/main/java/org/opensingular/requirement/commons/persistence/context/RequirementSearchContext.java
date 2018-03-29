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

package org.opensingular.requirement.commons.persistence.context;


import org.opensingular.requirement.commons.persistence.filter.QuickFilter;
import org.opensingular.requirement.commons.persistence.query.RequirementSearchQuery;
import org.opensingular.requirement.commons.persistence.query.RequirementSearchExtender;
import org.opensingular.requirement.commons.spring.security.SingularPermission;

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