package org.opensingular.server.commons.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.opensingular.form.persistence.entity.FormTypeEntity;
import org.opensingular.server.commons.test.SingularCommonsBaseTest;

import javax.inject.Inject;


public class PetitionServiceTest extends SingularCommonsBaseTest {

    @Inject
    public PetitionService<?,?> petitionService;

    @Inject
    public SessionFactory sessionFactory;

    @Test
    public void testName() throws Exception {
        Session s = sessionFactory.openSession();
        org.hibernate.Transaction t = s.beginTransaction();
        FormTypeEntity formTypeEntity = new FormTypeEntity();
        formTypeEntity.setAbbreviation("nada");
        formTypeEntity.setLabel("nada");
        formTypeEntity.setCacheVersionNumber(1l);
        s.saveOrUpdate(formTypeEntity);
        s.flush();
        t.commit();

    }
}
