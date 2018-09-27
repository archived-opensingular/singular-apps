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

package org.opensingular.server.commons.service;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.opensingular.server.commons.exception.SingularServerException;
import org.opensingular.server.commons.persistence.dao.EmailAddresseeDao;
import org.opensingular.server.commons.persistence.dao.EmailDao;
import org.opensingular.server.commons.persistence.entity.email.EmailAddresseeEntity;
import org.opensingular.server.commons.persistence.entity.email.EmailEntity;
import org.opensingular.server.commons.service.dto.Email;
import org.opensingular.server.commons.test.SingularCommonsBaseTest;
import org.springframework.test.annotation.Rollback;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EmailTest extends SingularCommonsBaseTest {

    @Inject
    private EmailPersistenceService emailPersistenceService;

    @Test
    @Transactional
    public void testSendEmail() throws IOException {
        Email e = new Email();
        e.setCreationDate(new Date());
        e.addTo("nada@mada.com");
        e.withSubject("Teste e-mail");
        e.withContent("super corpo de e-mail, ativar!");
        File f = File.createTempFile("nada","bada");
        f.deleteOnExit();
        e.addAttachment(f, "lada");
        Assert.assertTrue(emailPersistenceService.send(e));
    }

    @Test(expected = SingularServerException.class)
    @Rollback
    public void testEmailNaoExiste(){
        Email e = new Email();
        emailPersistenceService.send(e);
    }

    @Test
    @Transactional
    @Rollback
    public void testCountPending(){
        generateMockEmailAddresseEntitties();
        Assert.assertEquals(2, emailPersistenceService.countPendingRecipients());
    }

    @Test
    @Transactional
    @Rollback
    public void testListPending(){
        generateMockEmailAddresseEntitties();
        List<Email.Addressee> list = emailPersistenceService.listPendingRecipients(0, 10, null);

        Assert.assertEquals(2, list.size());
    }

    @Test
    @Rollback
    public void testSentMarked(){
        List<Email.Addressee> list = emailPersistenceService.listPendingRecipients(0, 1, null);

        Email.Addressee addressee = list.get(0);

        emailPersistenceService.markAsSent(addressee);

        Assert.assertNotNull(addressee.getSentDate());
    }

    private void generateMockEmailAddresseEntitties() {
        Email email = emailPersistenceService.createEmail("teste");
        email.withContent("conteudo de teste");
        email.setCreationDate(new Date());
        email.addTo("mock.entity@teste.com", "mock.entity2@teste.com");

        emailPersistenceService.send(email);
    }

    @Test
    public void testEmailDaoAndEmailAddresseeDaoConstructos(){
        Assert.assertNotNull(new EmailDao<>(EmailEntity.class));
        Assert.assertNotNull(new EmailAddresseeDao<>(EmailAddresseeEntity.class));
    }
}
