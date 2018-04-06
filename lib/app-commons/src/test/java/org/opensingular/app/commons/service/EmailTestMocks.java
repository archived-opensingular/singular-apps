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
import org.opensingular.app.commons.mail.persistence.entity.email.EmailAddresseeEntity;
import org.opensingular.app.commons.mail.persistence.entity.enums.AddresseType;
import org.opensingular.app.commons.mail.service.dto.Email;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

public class EmailTestMocks {

    public static EmailAddresseeEntity createMockEmailAddresseeEntity(Date date) {
        EmailAddresseeEntity emailEntity = new EmailAddresseeEntity();
        emailEntity.setCod((long) 1);
        Assert.assertEquals(new Long(1), emailEntity.getCod());

        Assert.assertNull(emailEntity.getAddress());
        Assert.assertNull(emailEntity.getAddresseType());

        emailEntity.setAddress("opensingular@gmail.com");
        emailEntity.setAddresseType(AddresseType.TO);


        return emailEntity;
    }

    public static Email createMockEmail() {
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
            f = File.createTempFile("nada", "de nada");
        } catch (IOException e) {
            LoggerFactory.getLogger(EmailTestMocks.class).error("erro ao criar arquivo temporario");
        }
        f.deleteOnExit();
        email.addAttachment(f, "lada");

        return email;
    }
}
