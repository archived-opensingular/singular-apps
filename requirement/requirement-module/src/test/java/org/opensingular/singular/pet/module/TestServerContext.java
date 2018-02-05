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


import org.opensingular.server.commons.config.IServerContext;
import org.opensingular.server.commons.config.ServerContext;
import org.apache.commons.validator.routines.UrlValidator;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestServerContext {

    /**
     * Contexto registrado no arquivo properties singular.properties para o petcionamento
     */
    private static final String WORKLIST_CONTEXT = "/pettest";

    private HttpServletRequest getRequest() {
        HttpServletRequest mockedRequest = Mockito.mock(HttpServletRequest.class);
        Mockito.when(mockedRequest.getContextPath()).thenReturn("/singular");
        Mockito.when(mockedRequest.getPathInfo()).thenReturn("/singular" + WORKLIST_CONTEXT + "/caixaentrada");
        return mockedRequest;
    }

    @Test
    public void testContextFromRequest() {
        Assert.assertEquals(ServerContext.WORKLIST, IServerContext.getContextFromRequest(getRequest(), ServerContext.values()));
    }

    @Test
    public void testRegexFromContextPath() {
        Assert.assertEquals(WORKLIST_CONTEXT + "/*", ServerContext.WORKLIST.getContextPath());

        Pattern p = Pattern.compile(ServerContext.WORKLIST.getPathRegex());
        Matcher m = p.matcher(ServerContext.WORKLIST.getContextPath());

        Assert.assertTrue(m.find());
        Assert.assertEquals(0, m.groupCount());
        Assert.assertEquals(ServerContext.WORKLIST.getContextPath(), m.group());
    }


    @Test
    public void testUrlPath() {
        UrlValidator validator = new UrlValidator();

        String path = ServerContext.WORKLIST.getUrlPath() + "/pagina/testeurl";
        String url = "http://127.0.0.1:8080" + path;

        Assert.assertTrue(validator.isValid(url));

        Pattern p = Pattern.compile(ServerContext.WORKLIST.getPathRegex());
        Matcher m = p.matcher(url);

        Assert.assertTrue(m.find());
        Assert.assertEquals(0, m.groupCount());
        Assert.assertEquals(path, m.group());


    }

}
