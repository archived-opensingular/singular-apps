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

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.flow.RedirectToUrlException;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.lib.support.spring.util.ApplicationContextProvider;
import org.opensingular.lib.wicket.util.template.admin.SingularAdminTemplate;
import org.opensingular.requirement.module.config.IServerContext;
import org.opensingular.requirement.module.spring.security.AbstractSingularSpringSecurityAdapter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.savedrequest.SavedRequest;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class LoginPage extends SingularAdminTemplate implements Loggable {

    @Inject
    private IServerContext serverContext;

    public LoginPage() {
        this(null);
    }

    public LoginPage(PageParameters parameters) {
        super(parameters);
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

        LoginForm(String id) {
            super(id);
            add(new LoginFeedbackPanel("feedback", LoginForm.this));
            add(new RequiredTextField<>("username", username).setLabel(new Model<>("Login")));
            add(new PasswordTextField("password", password).setLabel(new Model<>("Senha")));
        }

        @Override
        protected void onSubmit() {
            HttpServletRequest servletRequest = (HttpServletRequest) RequestCycle.get().getRequest().getContainerRequest();
            String originalUrl = getOriginalUrl(servletRequest.getSession());
            if (authenticate(username.getObject(), password.getObject())) {
                if (originalUrl != null) {
                    getLogger().info(String.format("redirecting to %s", originalUrl));
                    throw new RedirectToUrlException(originalUrl);
                } else {
                    getLogger().info("redirecting to home page");
                    throw new RedirectToUrlException(servletRequest.getRequestURI());
                }
            } else {
                error("Credenciais inválidas");
            }
        }

        private String getOriginalUrl(HttpSession session) {
            SavedRequest savedRequest = (SavedRequest) session.getAttribute("SPRING_SECURITY_SAVED_REQUEST");
            if (savedRequest != null) {
                return savedRequest.getRedirectUrl();
            } else {
                return null;
            }
        }

        boolean authenticate(String username, String password) {
            try {
                AuthenticationManager authenticationManager = ApplicationContextProvider.get()
                        .getBeansOfType(AbstractSingularSpringSecurityAdapter.class)
                        .values()
                        .stream()
                        .filter(i -> i.getContext().getName().equals(serverContext.getName())).findFirst()
                        .map(i -> {
                            try {
                                return i.authenticationManagerBean();
                            } catch (Exception ex) {
                                return null;
                            }
                        })
                        .orElse(null);
                if (authenticationManager != null) {
                    Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
                    if (auth.isAuthenticated()) {
                        SecurityContextHolder.getContext().setAuthentication(auth);
                        getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                                SecurityContextHolder.getContext());
                        return true;
                    } else {
                        return false;
                    }
                }
            } catch (BadCredentialsException ex) {
                getLogger().warn(ex.getMessage(), ex);
                return false;
            }
            return false;
        }
    }

}
