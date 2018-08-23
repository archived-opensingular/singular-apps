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

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.lib.wicket.util.template.admin.SingularAdminTemplate;

/**
 * Login page used for Singular.
 */
public class LoginPage extends SingularAdminTemplate implements Loggable {

    private boolean hasError = false;

    public LoginPage() {
        this(null);
    }

    public LoginPage(PageParameters parameters) {
        super(parameters);
        hasError = parameters != null && parameters.get("error") != null;
        add(new LoginForm("loginForm"));
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

    private class LoginForm extends Form<Void> {
        private Model<String> username = new Model<>();
        private Model<String> password = new Model<>();
        private WebMarkupContainer feedBackMessage;

        LoginForm(String id) {
            super(id);
            feedBackMessage = new WebMarkupContainer("feedback");
            feedBackMessage.setVisible(hasError);
            add(feedBackMessage);
            add(new RequiredTextField<>("username", username).setLabel(new Model<>("Login")));
            add(new PasswordTextField("password", password).setLabel(new Model<>("Senha")));
        }
    }

}
