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


import java.util.Arrays;
import java.util.List;

import org.opensingular.form.spring.SingularUserDetails;
import org.opensingular.requirement.module.config.IServerContext;

public interface SingularRequirementUserDetails extends SingularUserDetails {

    default boolean isContext(IServerContext context) {
        return context.equals(getServerContext());
    }

    /**
     * Representantion ID.
     * The same as username by default.
     * @return
     */
    default String getApplicantId(){
        return getUsername();
    }

    IServerContext getServerContext();

    List<SingularPermission> getPermissions();

    void addPermission(SingularPermission role);

    default void addPermissions(SingularPermission... roles) {
        addPermissions(Arrays.asList(roles));
    }

    default void addPermissions(List<SingularPermission> roles) {
        if (roles != null) {
            for (SingularPermission role : roles) {
                addPermission(role);
            }
        }
    }

    default boolean keepLoginThroughContexts() {
        return false;
    }
}
