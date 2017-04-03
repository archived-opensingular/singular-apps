package org.opensingular.server.commons.service;

import org.junit.Test;
import org.opensingular.server.commons.test.SingularServerTestBase;

import javax.inject.Inject;

public class PetitionServiceTest extends SingularServerTestBase {

    @Inject
    public PetitionService<?,?> petitionService;

    @Test
    public void testName() throws Exception {
        petitionService.findPetition(1l);
    }
}
