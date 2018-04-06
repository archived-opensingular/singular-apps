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

import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.opensingular.app.commons.mail.persistence.entity.email.EmailAddresseeEntity;
import org.opensingular.app.commons.mail.service.dto.Email;
import org.opensingular.app.commons.mail.service.email.EmailSender;
import org.opensingular.lib.commons.util.Loggable;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;

import static org.opensingular.app.commons.service.EmailTestMocks.*;

public class TestAliasFrom implements Loggable {

    public static final String TEST_MOCK = "TestMock";

    private EmailSender mockSender() {
        EmailSender sender = Mockito.spy(EmailSender.class);
        Mockito.doNothing().when(sender).send(Mockito.any(MimeMessage.class));
        sender.setHost("nada");
        sender.setUsername("nada");
        return sender;
    }


    @Test
    public void testSendEmailWithAlias() throws MessagingException {
        EmailAddresseeEntity entity    = createMockEmailAddresseeEntity(new Date());
        Email                email     = createMockEmail();
        Email.Addressee      addressee = new Email.Addressee(email, entity);
        EmailSender          sender    = mockSender();

        email.setAliasFrom(TEST_MOCK);

        sender.send(addressee);

        ArgumentCaptor<MimeMessage> argument = ArgumentCaptor.forClass(MimeMessage.class);
        Mockito.verify(sender).send(argument.capture());


        Assert.assertTrue(TEST_MOCK.equals(((InternetAddress) argument.getValue().getFrom()[0]).getPersonal()));
    }
}
