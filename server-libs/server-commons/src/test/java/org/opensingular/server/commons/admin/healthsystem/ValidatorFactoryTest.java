package org.opensingular.server.commons.admin.healthsystem;

import org.junit.Assert;
import org.junit.Test;
import org.opensingular.server.commons.admin.healthsystem.validation.database.ValidatorFactory;
import org.opensingular.server.commons.test.SingularCommonsBaseTest;

public class ValidatorFactoryTest extends SingularCommonsBaseTest {

    @Test(expected = Exception.class)
    public void getValidatorOracleTest() throws Exception {
        Assert.assertNotNull(ValidatorFactory.getValidator("Oracle"));

        ValidatorFactory.getValidator("NOTEXIST");
    }
}
