package org.opensingular.server.commons.wicket.view.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.Map;

public class ParamterHttpSerializerTest {

    String                        bigSampleGoogleURL = "ie=utf-8&oe=utf-8&client=firefox-b&gws_rd=cr&ei=0QzQWKSjLsGowAS56bTgCg&q=uma+pesquisa+com+n%C3%BAmero+bem+grande+de+caracteraskljaskljaskljsakldjaskldjaklsjdlkasjdklasjdklasjdklasjdklasjdklasjkljadlkes+gera+uma+url+gigante&*";
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

    @Test
    public void testeEncodeDecode() {
        LinkedHashMap<String, String> result = ParameterHttpSerializer.decode(bigSampleGoogleURL);
        String                        url    = ParameterHttpSerializer.encode(result);
        Assert.assertEquals(url, bigSampleGoogleURL);

    }

    @Test
    public void testBigGoogleURLCompressed() {
        testURLCompressed(bigSampleGoogleURL);
    }

    @Test
    public void testGoogleURLCompressed() {
        testURLCompressed(sampleGoogleURL);
    }

    private void testURLCompressed(String sampleUrl) {
        String url = ParameterHttpSerializer.encodeAndCompress(ParameterHttpSerializer.decode(sampleUrl));
        System.out.println(sampleUrl);
        System.out.println(url);
        Map<String, String> map = ParameterHttpSerializer.decodeCompressed(url);
        Assert.assertEquals(map, ParameterHttpSerializer.decode(sampleUrl));
    }
}
