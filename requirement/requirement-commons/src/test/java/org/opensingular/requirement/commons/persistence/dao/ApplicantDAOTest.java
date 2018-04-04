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

package org.opensingular.requirement.commons.persistence.dao;

import org.junit.Assert;
import org.junit.Test;
import org.opensingular.requirement.commons.persistence.dao.form.ApplicantDAO;
import org.opensingular.requirement.commons.persistence.entity.enums.PersonType;
import org.opensingular.requirement.commons.persistence.entity.form.ApplicantEntity;
import org.opensingular.requirement.commons.test.SingularCommonsBaseTest;


import javax.inject.Inject;
import javax.transaction.Transactional;

public class ApplicantDAOTest extends SingularCommonsBaseTest {

    @Inject
    private ApplicantDAO applicantDAO;

    @Test
    @Transactional
    public void testFindByExternalId(){
        final String externa = "pessoaExterna";

        ApplicantEntity entity = new ApplicantEntity();
        entity.setPersonType(PersonType.FISICA);
        entity.setIdPessoa(externa);
        entity.setCpfCNPJ("12345678900");
        entity.setName("pessoa");

        applicantDAO.save(entity);

        ApplicantEntity applicantByExternalId = applicantDAO.findApplicantByExternalId(externa);

        Assert.assertEquals(entity, applicantByExternalId);
        Assert.assertEquals(entity.getCod(), applicantByExternalId.getCod());
        Assert.assertEquals(entity.getName(), applicantByExternalId.getName());
        Assert.assertEquals(entity.getCpfCNPJ(), applicantByExternalId.getCpfCNPJ());
        Assert.assertEquals(entity.getIdPessoa(), applicantByExternalId.getIdPessoa());
        Assert.assertEquals(entity.getPersonType(), applicantByExternalId.getPersonType());

        Assert.assertNull(applicantDAO.findApplicantByExternalId(""));
    }
}
