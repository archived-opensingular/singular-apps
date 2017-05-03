package org.opensingular.server.commons.util.url;

import org.apache.wicket.request.Url;
import org.junit.Test;

import static org.junit.Assert.*;

public class UrlToolkitTest {

    @Test
    public void buildUrlWithoutSlash() {
        UrlToolkit urlBuilder = new UrlToolkitBuilder().build(Url.parse("http://localhost:8080"));
        String     url   = urlBuilder.concatServerAdressWithContext("/singular");

        assertEquals("http://localhost:8080/singular", url);
    }

    @Test
    public void buildUrlBothWithSlash() {
        UrlToolkit urlBuilder = new UrlToolkitBuilder().build(Url.parse("http://localhost:8080/"));
        String     url   = urlBuilder.concatServerAdressWithContext("/singular");

        assertEquals("http://localhost:8080/singular", url);
    }

    @Test
    public void buildUrlContextWithoutSlash() {
        UrlToolkit urlBuilder = new UrlToolkitBuilder().build(Url.parse("http://localhost:8080/"));
        String     url   = urlBuilder.concatServerAdressWithContext("singular");

        assertEquals("http://localhost:8080/singular", url);
    }
}