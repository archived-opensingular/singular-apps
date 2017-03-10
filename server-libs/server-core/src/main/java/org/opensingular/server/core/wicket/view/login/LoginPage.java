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

package org.opensingular.server.core.wicket.view.login;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.flow.RedirectToUrlException;
import org.opensingular.server.commons.spring.security.SecurityAuthPaths;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.devutils.stateless.StatelessComponent;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.opensingular.server.commons.spring.security.SecurityAuthPathsFactory;
import org.wicketstuff.annotation.mount.MountPath;

import static org.opensingular.lib.wicket.util.util.WicketUtils.$b;

@StatelessComponent
@MountPath("login")
public class LoginPage extends WebPage {

    public LoginPage(PageParameters pageParameters) {
        super(pageParameters);
        setStatelessHint(true);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.render(CssHeaderItem.forReference(new CssResourceReference(LoginPage.class, "LoginPage.css")));
        response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(LoginPage.class, "LoginPage.js")));
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        add(new Link<Void>("baseurlAnchor") {
            @Override
            public void onClick() {
                throw new RedirectToUrlException(RequestCycle.get().getRequest().getFilterPath());
            }
        });

        SecurityAuthPathsFactory securityAuthPathsFactory = new SecurityAuthPathsFactory();
        SecurityAuthPaths        securityAuthPaths        = securityAuthPathsFactory.get();

        WebMarkupContainer loginForm = new WebMarkupContainer("form");
        loginForm.add($b.attr("action", securityAuthPaths.getLoginPath()));
        add(loginForm);

        WebMarkupContainer loginError = new WebMarkupContainer("loginErrorC");
        loginError.setVisible(getPageParameters().get("error").toBoolean(false));
        loginError.add(new Label("loginError", getString("label.login.error")));
        loginForm.add(loginError);

        loginForm.add(new WebMarkupContainer("username").add($b.attr("placeholder", getString("label.login.page.username"))));

        loginForm.add(new WebMarkupContainer("password").add($b.attr("placeholder", getString("label.login.page.password"))));

        WebMarkupContainer ownerLink = new WebMarkupContainer("ownerLink");
        ownerLink.add(new AttributeModifier("href", new ResourceModel("footer.product.owner.addr")));
        ownerLink.add(new AttributeModifier("title", new ResourceModel("footer.product.owner.title")));
        add(ownerLink);
    }


}
