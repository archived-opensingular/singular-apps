package org.opensingular.server.p.commons.admin.healthsystem;

import org.junit.Assert;
import org.junit.Test;
import org.opensingular.server.commons.admin.healthsystem.validation.database.ValidatorFactory;
import org.opensingular.server.commons.exception.SingularServerException;
import org.opensingular.server.commons.test.SingularCommonsBaseTest;

public class ValidatorFactoryTest extends SingularCommonsBaseTest {

    @Test(expected = SingularServerException.class)
    public void getValidatorOracleTest() {
        Assert.assertNotNull(ValidatorFactory.getValidator("Oracle"));

        ValidatorFactory.getValidator("NOTEXIST");
    }
}
