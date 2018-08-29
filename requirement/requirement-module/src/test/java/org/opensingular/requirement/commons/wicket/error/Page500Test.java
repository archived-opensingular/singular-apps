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

package org.opensingular.requirement.commons.wicket.error;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.mock.MockApplication;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.util.tester.WicketTestCase;
import org.junit.Test;
import org.opensingular.lib.wicket.util.template.admin.SingularAdminApp;
import org.opensingular.requirement.module.wicket.error.Page500;
import org.opensingular.requirement.module.wicket.view.template.Footer;

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