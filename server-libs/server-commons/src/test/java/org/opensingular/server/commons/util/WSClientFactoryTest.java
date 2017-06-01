package org.opensingular.server.commons.util;

import org.junit.Assert;
import org.junit.Test;
import org.opensingular.lib.commons.base.SingularProperties;

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
