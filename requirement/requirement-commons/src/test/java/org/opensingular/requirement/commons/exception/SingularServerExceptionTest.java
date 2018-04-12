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

package org.opensingular.requirement.commons.exception;

import org.junit.Assert;
import org.junit.Test;
import org.opensingular.requirement.commons.exception.RequirementWithoutDefinitionException;
import org.opensingular.requirement.commons.exception.SingularServerException;

public class SingularServerExceptionTest {

    @Test(expected = RequirementWithoutDefinitionException.class)
    public void requirementWithoutDefinitionExceptionTest(){
        throw new RequirementWithoutDefinitionException();
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
