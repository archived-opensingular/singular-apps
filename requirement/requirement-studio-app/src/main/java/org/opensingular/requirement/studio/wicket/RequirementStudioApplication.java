/*
 * Copyright (C) 2016 Singular Studios (a.k.a Atom Tecnologia) - www.opensingular.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.opensingular.requirement.studio.wicket;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.Page;
import org.apache.wicket.Session;
import org.apache.wicket.markup.head.filter.JavaScriptFilteredIntoFooterHeaderResponse;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.opensingular.lib.wicket.util.template.SingularTemplate;
import org.opensingular.lib.wicket.util.template.admin.SingularAdminTemplate;
import org.opensingular.requirement.module.wicket.SingularRequirementApplication;
import org.opensingular.requirement.module.wicket.error.Page403;
import org.opensingular.requirement.module.wicket.error.Page410;
import org.opensingular.studio.core.view.StudioFooter;
import org.opensingular.studio.core.view.StudioHeader;
import org.opensingular.studio.core.view.StudioPage;
import org.wicketstuff.annotation.scan.AnnotatedMountScanner;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

public class RequirementStudioApplication extends SingularRequirementApplication {

    @Override
    public Class<? extends Page> getHomePage() {
        return StudioPage.class;
    }

    @Override
    public Session newSession(Request request, Response response) {
        Session session = super.newSession(request, response);
        session.setLocale(new Locale("pt", "BR"));
        return session;
    }

    @Override
    public void init() {
        super.init();
        getMarkupSettings().setStripWicketTags(true);
        getMarkupSettings().setStripComments(true);

        getApplicationSettings().setAccessDeniedPage(Page403.class);
        getApplicationSettings().setPageExpiredErrorPage(Page410.class);

        getMarkupSettings().setDefaultMarkupEncoding(StandardCharsets.UTF_8.name());
        setHeaderResponseDecorator(r -> new JavaScriptFilteredIntoFooterHeaderResponse(r, SingularTemplate.JAVASCRIPT_CONTAINER));
        getComponentInstantiationListeners().add(new SpringComponentInjector(this));
        new AnnotatedMountScanner().scanPackage("org.opensingular.studio").mount(this);
        getComponentOnConfigureListeners().add(component -> {
            boolean outputId = !component.getRenderBodyOnly();
            component.setOutputMarkupId(outputId).setOutputMarkupPlaceholderTag(outputId);
        });
    }

    @Override
    public MarkupContainer buildPageHeader(String id,
                                           boolean withMenu,
                                           SingularAdminTemplate adminTemplate) {
        return new StudioHeader(id);
    }

    @Override
    public MarkupContainer buildPageFooter(String id) {
        return new StudioFooter(id);
    }
}