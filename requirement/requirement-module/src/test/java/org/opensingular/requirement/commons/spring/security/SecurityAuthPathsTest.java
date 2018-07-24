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

package org.opensingular.requirement.commons.spring.security;

import org.apache.wicket.request.Request;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.cycle.RequestCycle;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.opensingular.requirement.module.spring.security.SecurityAuthPaths;
import org.opensingular.requirement.module.util.url.UrlToolkit;
import org.opensingular.requirement.module.util.url.UrlToolkitBuilder;

import static org.junit.Assert.*;


@RunWith(MockitoJUnitRunner.class)
public class SecurityAuthPathsTest {

    @Mock
    UrlToolkitBuilder urlToolkitBuilder;

    @Mock
    UrlToolkit urlToolkit;

    @Mock
    RequestCycle requestCycle;

    @Mock
    Request request;

    @Mock
    Url url;

    SecurityAuthPaths securityAuthPaths;

    @Before
    public void setUp() {
        Mockito.when(requestCycle.getRequest()).thenReturn(request);
        Mockito.when(request.getUrl()).thenReturn(url);
        Mockito.when(urlToolkitBuilder.build(Mockito.eq(url))).thenReturn(urlToolkit);
        Mockito.when(urlToolkit.concatServerAdressWithContext(Mockito.eq("/peticionamento/singular/logout"))).thenReturn("http://localhost:8080/peticionamento/singular/logout");
        Mockito.when(urlToolkit.concatServerAdressWithContext(Mockito.eq("/peticionamento"))).thenReturn("http://localhost:8080/peticionamento");
        securityAuthPaths = new SecurityAuthPaths("/peticionamento", "/singular", urlToolkitBuilder);
    }


    @Test
    public void getLoginPath() throws Exception {
        assertEquals("/peticionamento/singular/login", securityAuthPaths.getLoginPath());
    }

    @Test
    public void getLogoutPath() throws Exception {
        assertEquals("/peticionamento/singular/logout", securityAuthPaths.getLogoutPath(null));
        assertEquals("http://localhost:8080/peticionamento/singular/logout?service=http://localhost:8080/peticionamento", securityAuthPaths.getLogoutPath(requestCycle));
        Mockito.verify(urlToolkit).concatServerAdressWithContext(Mockito.eq("/peticionamento/singular/logout"));
        Mockito.verify(urlToolkit).concatServerAdressWithContext(Mockito.eq("/peticionamento"));
    }

}