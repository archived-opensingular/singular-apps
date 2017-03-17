package org.opensingular.server.commons.wicket.view.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.LinkedHashMap;

public class ParamterHttpSerializerTest {

    String                        sampleGoogleURL    = "q=java+xss+javascript+scape+only&ie=utf-8&oe=utf-8&client=firefox-b&gws_rd=cr&ei=yNjLWNLYIcSYwgTn5p_ACg";
    LinkedHashMap<String, String> sampleGoogleURLMap = new LinkedHashMap<>();

    {
        sampleGoogleURLMap.put("q", "java xss javascript scape only");
        sampleGoogleURLMap.put("ie", "utf-8");
        sampleGoogleURLMap.put("oe", "utf-8");
        sampleGoogleURLMap.put("client", "firefox-b");
        sampleGoogleURLMap.put("gws_rd", "cr");
        sampleGoogleURLMap.put("ei", "yNjLWNLYIcSYwgTn5p_ACg");
    }

    @Test
    public void testGoogleURL() {
        LinkedHashMap<String, String> params = ParameterHttpSerializer.decode(sampleGoogleURL);
        Assert.assertEquals(sampleGoogleURLMap, params);
        Assert.assertEquals(sampleGoogleURL, ParameterHttpSerializer.encode(params));
    }

}
