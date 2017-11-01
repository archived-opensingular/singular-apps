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

package org.opensingular.server.commons.util;

import org.junit.Assert;
import org.junit.Test;
import org.opensingular.lib.commons.base.SingularProperties;
import org.opensingular.lib.commons.util.WSClientDefaultFactory;

import javax.xml.ws.BindingProvider;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WSClientFactoryTest {

    @Test
    public void testWSClientFactoryTest() throws Exception {
        ServiceTest         serviceMock = mock(ServiceTest.class);
        Map<String, Object> map         = new HashMap<>();
        when(serviceMock.getRequestContext()).thenReturn(map);

        WSClientDefaultFactory<ServiceTest> factory = new WSClientDefaultFactory<>("wswrapper.test.porttype", () -> serviceMock);

        ServiceTest serviceTest = factory.getReference();

        Assert.assertEquals(SingularProperties.get().getProperty("wswrapper.test.porttype"), map.get(BindingProvider.ENDPOINT_ADDRESS_PROPERTY) + "?wsdl");

    }

    public static interface ServiceTest extends BindingProvider {
        public void doService();
    }
}
