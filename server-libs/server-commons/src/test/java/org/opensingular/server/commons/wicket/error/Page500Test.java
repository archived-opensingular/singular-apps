package org.opensingular.server.commons.wicket.error;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.mock.MockApplication;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.util.tester.WicketTestCase;
import org.junit.Test;
import org.opensingular.lib.wicket.util.template.admin.SingularAdminApp;
import org.opensingular.server.commons.wicket.view.template.Footer;

public class Page500Test extends WicketTestCase {

    @Test
    public void testRender() throws Exception {
        Page500 page500 = new Page500(null);
        tester.startPage(page500);
    }

    @Override
    protected WebApplication newApplication() {
        return new AdminApp();
    }

    private class AdminApp extends MockApplication implements SingularAdminApp {
        @Override
        public MarkupContainer buildPageFooter(String id) {
            return new Footer(id);
        }
    }
}