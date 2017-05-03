package org.opensingular.server.commons.exception;

import org.junit.Assert;
import org.junit.Test;

public class SingularServerExceptionTest {

    @Test(expected = PetitionWithoutDefinitionException.class)
    public void petitionWithoutDefinitionExceptionTest(){
        throw new PetitionWithoutDefinitionException();
    }

    @Test(expected = SingularServerException.class)
    public void singularServerExceptionWithThrowableTest(){
        throw new SingularServerException(new NullPointerException());
    }

    @Test(expected = SingularServerException.class)
    public void singularServerExceptionWithThrowableAndMsgTest(){
        throw new SingularServerException("exception", new NullPointerException());
    }

    @Test
    public void rethrowTest(){
        Assert.assertNotNull(SingularServerException.rethrow(new NullPointerException()));
        Assert.assertNotNull(SingularServerException.rethrow("exception", new SingularServerException("error")));
        Assert.assertNotNull(SingularServerException.rethrow(null, new SingularServerException("error")));
        Assert.assertNotNull(SingularServerException.rethrow("error", null));
        Assert.assertNotNull(SingularServerException.rethrow("exception", new NullPointerException()));
    }
}
