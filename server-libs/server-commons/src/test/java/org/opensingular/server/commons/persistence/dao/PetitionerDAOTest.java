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
import org.opensingular.server.commons.persistence.dao.form.PetitionerDAO;
import org.opensingular.server.commons.persistence.entity.enums.PersonType;
import org.opensingular.server.commons.persistence.entity.form.PetitionerEntity;
import org.opensingular.server.commons.test.SingularCommonsBaseTest;

import javax.inject.Inject;
import javax.transaction.Transactional;

public class PetitionerDAOTest extends SingularCommonsBaseTest {

    @Inject
    private PetitionerDAO petitionerDAO;

    @Test
    @Transactional
    public void testFindByExternalId(){
        final String externa = "pessoaExterna";

        PetitionerEntity entity = new PetitionerEntity();
        entity.setPersonType(PersonType.FISICA);
        entity.setIdPessoa(externa);
        entity.setCpfCNPJ("12345678900");
        entity.setName("pessoa");

        petitionerDAO.save(entity);

        PetitionerEntity petitionerByExternalId = petitionerDAO.findPetitionerByExternalId(externa);

        Assert.assertEquals(entity, petitionerByExternalId);
        Assert.assertEquals(entity.getCod(), petitionerByExternalId.getCod());
        Assert.assertEquals(entity.getName(), petitionerByExternalId.getName());
        Assert.assertEquals(entity.getCpfCNPJ(), petitionerByExternalId.getCpfCNPJ());
        Assert.assertEquals(entity.getIdPessoa(), petitionerByExternalId.getIdPessoa());
        Assert.assertEquals(entity.getPersonType(), petitionerByExternalId.getPersonType());

        Assert.assertNull(petitionerDAO.findPetitionerByExternalId(""));
    }
}
