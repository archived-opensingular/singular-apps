/*
 * Copyright (C) 2016 Singular Studios (a.k.a Atom Tecnologia) - www.opensingular.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.opensingular.requirement.module.spring.security;


import org.opensingular.requirement.module.config.IServerContext;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class DefaultUserDetails implements SingularRequirementUserDetails {
    public static final String ROLE_PREFIX = "ROLE_";

    private final String username;
    private final String displayName;
    private final List<SingularPermission> permissions = new ArrayList<>();
    private final List<Class<? extends IServerContext>> allowedContexts = new ArrayList<>();

    public DefaultUserDetails(String username, String displayName, List<SingularPermission> permissions,
                              List<Class<? extends IServerContext>> allowedContexts) {
        this.username = username;
        this.displayName = displayName;
        this.permissions.addAll(permissions);
        this.allowedContexts.addAll(allowedContexts);
    }

    @Override
    public void addPermission(SingularPermission role) {
        permissions.add(role);
    }

    @Override
    public List<Class<? extends IServerContext>> getAllowedContexts() {
        return allowedContexts;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public List<SingularPermission> getPermissions() {
        return permissions;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getApplicantId() {
        return getUsername();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return permissions.parallelStream()
                .map(SingularPermission::getSingularId)
                .map(s -> new SimpleGrantedAuthority(ROLE_PREFIX + s))
                .collect(Collectors.toList());
    }
}
