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

package org.opensingular.app.commons.service;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.opensingular.app.commons.mail.persistence.entity.email.EmailAddresseeEntity;
import org.opensingular.app.commons.mail.persistence.entity.enums.AddresseType;
import org.opensingular.app.commons.mail.service.dto.Email;
import org.opensingular.app.commons.mail.service.email.EmailSender;
import org.opensingular.app.commons.mail.service.email.EmailSenderScheduledJob;
import org.opensingular.app.commons.test.SpringBaseTest;
import org.opensingular.lib.commons.util.Loggable;

public class EmailSenderTest extends SpringBaseTest implements Loggable {

    @Inject
    private EmailSenderScheduledJob emailSenderJob;

    @Inject
    private EmailSender emailSender;

    @Test
    @Transactional
    public void testJobWithoutEmailToSend(){
            emailSenderJob.setEmailsPerPage(20);

        Object run = emailSenderJob.run();
        Assert.assertEquals("0 sent from total of 0", run);

        Assert.assertEquals("EmailSenderScheduledJob [getScheduleData()="
                        +emailSenderJob.getScheduleData().toString()+", getId()="+emailSenderJob.getId()+"]",
                emailSenderJob.toString());
    }

    @Test
    public void testNotSendEmail(){
        emailSender.setHost(null);
        Assert.assertFalse(emailSender.send((Email.Addressee) null));
    }


    @Test
    @Ignore
    public void testSendEmailReal(){
        //See properties of e-mail in singular.properties.
        EmailAddresseeEntity entity = createMockEmailAddresseeEntity(new Date());
        Email email = createMockEmail();
        email.addAliasFrom("TestMock");
        Email.Addressee addressee = new Email.Addressee(email, entity);
        Assert.assertTrue(emailSender.send(addressee));
    }

    @Test
    public void sendEmailExceptionTest(){
        emailSender.setHost("opensingular.org");

        Date date = new Date();

        Email email = createMockEmail();
        Assert.assertNull(email.getCreationDate());

        EmailAddresseeEntity entity = createMockEmailAddresseeEntity(date);

        Email.Addressee addressee = new Email.Addressee(email, entity);

        addressee.setSentDate(date);
        Assert.assertEquals(date, addressee.getSentDate());
        addressee.setSentDate(null);

        emailSender.setPort(null);
        Assert.assertEquals(-1, emailSender.getPort());

        emailSender.setPort("8080");
        Assert.assertEquals(8080, emailSender.getPort());

        emailSender.setUsername("test@email.com");

        Assert.assertFalse(emailSender.send(addressee));

    }

    @Test
    public void testEmailAddresseEntity(){
        Date date = new Date();
        EmailAddresseeEntity entity = createMockEmailAddresseeEntity(date);

        entity.setSentDate(date);
        Assert.assertEquals(date, entity.getSentDate());

        Assert.assertEquals(new Long(1), entity.getCod());

        Assert.assertEquals("opensingular@gmail.com", entity.getAddress());

    }

    private EmailAddresseeEntity createMockEmailAddresseeEntity(Date date) {
        EmailAddresseeEntity emailEntity = new EmailAddresseeEntity();
        emailEntity.setCod((long)1);
        Assert.assertEquals(new Long(1), emailEntity.getCod());

        Assert.assertNull(emailEntity.getAddress());
        Assert.assertNull(emailEntity.getAddresseType());

        emailEntity.setAddress("opensingular@gmail.com");
        emailEntity.setAddresseType(AddresseType.TO);


        return emailEntity;
    }

    private Email createMockEmail() {
        Email email = new Email();
        email.withSubject("Test");
        email.withContent("Some context to test.");
        email.addTo(Arrays.asList("email1@email.com", "email2@email.com", "email3@email.com"));

        email.addCc(Arrays.asList("email1@email.com", "email2@email.com", "email3@email.com"));
        email.addCc("email1@email.com", "email2@email.com", "email3@email.com");

        email.addBcc(Arrays.asList("email1@email.com", "email2@email.com", "email3@email.com"));
        email.addBcc("email1@email.com", "email2@email.com", "email3@email.com");

        File f = null;
        try {
            f = File.createTempFile("nada","de nada");
        } catch (IOException e) {
            getLogger().error("erro ao criar arquivo temporario");
        }
        f.deleteOnExit();
        email.addAttachment(f, "lada");

        return email;
    }
}
