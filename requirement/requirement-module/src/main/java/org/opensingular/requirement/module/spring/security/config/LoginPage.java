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

package org.opensingular.requirement.module.spring.security.config;

import org.apache.wicket.Component;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptContentHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.lib.wicket.util.template.admin.SingularAdminTemplate;
import org.opensingular.requirement.module.spring.security.config.cas.SingularUsernamePasswordFilter;

import javax.servlet.http.HttpServletRequest;

/**
 * Login page used for Singular.
 */
public class LoginPage extends SingularAdminTemplate implements Loggable {

    private Model<String> usernameModel     = new Model<>();
    private Model<String> messageErrorModel = new Model<>();

    public LoginPage(PageParameters parameters) {
        super(parameters);
        populateSpringErrorMessage();
        createContainerFormLogin();
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.render(JavaScriptContentHeaderItem.forReference(new JavaScriptResourceReference(LoginPage.class, "LoginPage.js")));
    }

    protected Component createExtraButtonsContainer(String id) {
        return new WebMarkupContainer(id).setVisible(false);
    }

    protected Component createLoginTitle(String id) {
        return new Label(id, "Login");
    }

    protected Component createLogoSubtitle(String id) {
        return new Label(id, "");
    }

    private void createContainerFormLogin() {
        add(createLogoSubtitle("logo-subtitle"));
        WebMarkupContainer container = createContainerLogin("container-login");
        LoginForm          loginForm = createLoginForm("loginForm");
        loginForm.add(createExtraButtonsContainer("extraButtons"));
        loginForm.add(createLoginTitle("login-title"));
        container.add(loginForm);
        add(container);
    }

    protected WebMarkupContainer createContainerLogin(String id) {
        return new WebMarkupContainer(id);
    }

    private LoginForm createLoginForm(String id) {
        return new LoginForm(id);
    }

    @Override
    protected IModel<String> getContentTitle() {
        return null;
    }

    @Override
    protected IModel<String> getContentSubtitle() {
        return null;
    }

    @Override
    protected boolean isWithMenu() {
        return false;
    }

    protected class LoginForm extends Form<Void> {

        LoginForm(String id) {
            super(id);
            WebMarkupContainer feedBackMessage = new WebMarkupContainer("feedback");
            feedBackMessage.add(new Label("messageError", messageErrorModel));
            feedBackMessage.setVisible(messageErrorModel.getObject() != null);
            add(feedBackMessage);

            Model<String> password = new Model<>();
            add(new RequiredTextField<>("username", usernameModel).setLabel(new Model<>("Login")));
            add(new PasswordTextField("password", password).setLabel(new Model<>("Senha")));
        }
    }

    private void populateSpringErrorMessage() {
        HttpServletRequest httpRequest                 = (HttpServletRequest) RequestCycle.get().getRequest().getContainerRequest();
        String             springSecurityLastException = (String) httpRequest.getSession().getAttribute(SingularUsernamePasswordFilter.SINGULAR_AUTHENTICATION_MESSAGE_EXCEPTION);
        usernameModel.setObject((String) httpRequest.getSession().getAttribute(SingularUsernamePasswordFilter.SINGULAR_USERNAME_KEY));
        httpRequest.getSession().removeAttribute(SingularUsernamePasswordFilter.SINGULAR_AUTHENTICATION_MESSAGE_EXCEPTION);
        httpRequest.getSession().removeAttribute(SingularUsernamePasswordFilter.SINGULAR_USERNAME_KEY);
        if (springSecurityLastException != null) {
            messageErrorModel.setObject(springSecurityLastException);
        }
    }

}
