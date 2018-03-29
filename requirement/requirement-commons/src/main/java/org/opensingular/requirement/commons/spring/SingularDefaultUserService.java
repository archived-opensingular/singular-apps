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

package org.opensingular.requirement.commons.spring;

import org.apache.wicket.Application;
import org.opensingular.flow.core.SUser;
import org.opensingular.flow.core.service.IUserService;
import org.opensingular.requirement.commons.persistence.dao.flow.ActorDAO;
import org.opensingular.requirement.commons.wicket.SingularSession;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.Objects;
import java.util.Optional;

public class SingularDefaultUserService implements IUserService {


    @Inject
    private ActorDAO actorDAO;

    @Override
    public SUser getUserIfAvailable() {
        String username = null;

        if (Application.exists() && SingularSession.exists()) {
            username = SingularSession.get().getUsername();
        }

        if (username != null) {
            return actorDAO.retrieveByUserCod(username);
        } else {
            return null;
        }

    }

    @Override
    public boolean canBeAllocated(SUser sUser) {
        return true;
    }

    @Override
    public SUser findUserByCod(String username) {
        return actorDAO.retrieveByUserCod(username);
    }

    @Override
    @Transactional
    public SUser saveUserIfNeeded(SUser sUser) {
        return actorDAO.saveUserIfNeeded(sUser);
    }

    @Override
    @Transactional
    public Optional<SUser> saveUserIfNeeded(String codUsuario) {
        Objects.requireNonNull(codUsuario);
        return actorDAO.saveUserIfNeeded(codUsuario);
    }

    @Override
    @Transactional
    public SUser findByCod(Integer cod) {
         return actorDAO.get(cod).orElse(null);
    }
}
