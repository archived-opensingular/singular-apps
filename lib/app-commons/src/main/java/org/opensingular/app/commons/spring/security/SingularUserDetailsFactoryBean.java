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

package org.opensingular.app.commons.spring.security;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class SingularUserDetailsFactoryBean<T extends UserDetails> implements FactoryBean<T> {

    private Class<T> userDetailsClazz;

    public SingularUserDetailsFactoryBean(Class<T> userDetailsClazz) {
        this.userDetailsClazz = userDetailsClazz;
    }

    @Override
    public T getObject() throws Exception {
        if (SecurityContextHolder.getContext().getAuthentication() != null
                && SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetails) {
            return (T) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }
        return null;
    }

    @Override
    public Class<T> getObjectType() {
        return userDetailsClazz;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}
