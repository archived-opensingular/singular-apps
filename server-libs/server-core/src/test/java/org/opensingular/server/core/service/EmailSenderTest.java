package org.opensingular.server.core.service;

import org.junit.Assert;
import org.junit.Test;
import org.opensingular.server.commons.persistence.entity.email.EmailAddresseeEntity;
import org.opensingular.server.commons.service.dto.Email;
import org.opensingular.server.core.test.SingularServerBaseTest;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Date;

public class EmailSenderTest extends SingularServerBaseTest {

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


        Assert.assertFalse(emailSender.send(addressee));

    }

    public void testEmailAddresseEntity(){
        Date date = new Date();
        EmailAddresseeEntity entity = createMockEmailAddresseeEntity(date);

        entity.setSentDate(date);
        Assert.assertEquals(date, entity.getSentDate());

        Assert.assertEquals(new Long(1), entity.getCod());

        Assert.assertEquals("mirante.teste@gmail.com", entity.getAddress());

    }

    private EmailAddresseeEntity createMockEmailAddresseeEntity(Date date) {
        EmailAddresseeEntity emailEntity = new EmailAddresseeEntity();
        emailEntity.setCod((long)1);
        Assert.assertEquals(new Long(1), emailEntity.getCod());

        Assert.assertNull(emailEntity.getAddress());
        Assert.assertNull(emailEntity.getAddresseType());

        emailEntity.setAddress("mirante.teste@gmail.com");
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

        return email;
    }
}
