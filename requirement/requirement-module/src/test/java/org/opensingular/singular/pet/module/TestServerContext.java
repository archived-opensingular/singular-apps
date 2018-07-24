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

package org.opensingular.singular.pet.module;


import org.apache.commons.validator.routines.UrlValidator;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.opensingular.requirement.module.config.AbstractSingularInitializer;
import org.opensingular.requirement.module.config.DefaultContexts;
import org.opensingular.requirement.module.config.IServerContext;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class TestServerContext {

    /**
     * Contexto registrado no arquivo properties singular.properties para o petcionamento
     */
    private static final String WORKLIST_CONTEXT = "/worklist";

    IServerContext worklist = new DefaultContexts.WorklistContext();
    IServerContext requirement = new DefaultContexts.RequirementContext();
    
    private HttpServletRequest getRequest() {
        HttpServletRequest mockedRequest = Mockito.mock(HttpServletRequest.class);
        Mockito.when(mockedRequest.getContextPath()).thenReturn("/singular");
        Mockito.when(mockedRequest.getPathInfo()).thenReturn("/singular" + WORKLIST_CONTEXT + "/caixaentrada");
        return mockedRequest;
    }

    @Test
    public void testContextFromRequest() {
     
        Assert.assertEquals(worklist, IServerContext.getContextFromRequest(getRequest(),
                new IServerContext[]{requirement, worklist}));
    }

    @Test
    public void testRegexFromContextPath() {
        Assert.assertEquals(WORKLIST_CONTEXT + "/*", worklist.getContextPath());

        Pattern p = Pattern.compile(worklist.getPathRegex());
        Matcher m = p.matcher(worklist.getContextPath());

        Assert.assertTrue(m.find());
        Assert.assertEquals(0, m.groupCount());
        Assert.assertEquals(worklist.getContextPath(), m.group());
    }


    @Test
    public void testUrlPath() {
        UrlValidator validator = new UrlValidator();

        String path = worklist.getUrlPath() + "/pagina/testeurl";
        String url = "http://127.0.0.1:8080" + path;

        Assert.assertTrue(validator.isValid(url));

        Pattern p = Pattern.compile(worklist.getPathRegex());
        Matcher m = p.matcher(url);

        Assert.assertTrue(m.find());
        Assert.assertEquals(0, m.groupCount());
        Assert.assertEquals(path, m.group());


    }

}
