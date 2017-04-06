package org.opensingular.server.commons.service;

import org.junit.Assert;
import org.junit.Test;
import org.opensingular.server.commons.exception.SingularServerException;
import org.opensingular.server.commons.service.dto.Email;
import org.opensingular.server.commons.test.SingularServerBaseTest;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.Date;

public class EmailTest extends SingularServerBaseTest {

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
    @Transactional
    public void testEmailNaoExise(){
        Email e = new Email();
        emailPersistenceService.send(e);
    }
}
