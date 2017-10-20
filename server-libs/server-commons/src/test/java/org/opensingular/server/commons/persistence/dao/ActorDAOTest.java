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

package org.opensingular.server.commons.persistence.dao;

import org.junit.Assert;
import org.junit.Test;
import org.opensingular.flow.core.SUser;
import org.opensingular.flow.persistence.entity.Actor;
import org.opensingular.server.commons.persistence.dao.flow.ActorDAO;
import org.opensingular.server.commons.test.SingularCommonsBaseTest;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public class ActorDAOTest extends SingularCommonsBaseTest {

    @Inject
    private ActorDAO actorDAO;

    @Test
    @Transactional
    public void buscaPorCodUsuarioWithNullValueTest(){
        Assert.assertNull(actorDAO.retrieveByUserCod(null));
    }

    @Test
    @Transactional
    public void saveUserIfNeededTest(){
        SUser user = new Actor(13, "codUser", "name", "email@email.com");
        Assert.assertNull(actorDAO.saveUserIfNeeded(user));


        Optional<SUser> sUser = actorDAO.saveUserIfNeeded("codUsuario");
        Assert.assertEquals("codUsuario", sUser.get().getCodUsuario());
    }

    @Test
    @Transactional
    public void listAllocableUsersTest(){
        List<Actor> actors = actorDAO.listAllocableUsers(0);
        Assert.assertEquals(0, actors.size());

        actorDAO.saveUserIfNeeded("codUsuario");
        actorDAO.saveUserIfNeeded("codUsuario2");

        actors = actorDAO.listAllocableUsers(0);
        Assert.assertEquals(2, actors.size());
    }
}
