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

package org.opensingular.requirement.module.wicket.listener;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.wicket.core.request.handler.IPageClassRequestHandler;
import org.apache.wicket.core.request.handler.PageProvider;
import org.apache.wicket.core.request.handler.RenderPageRequestHandler;
import org.apache.wicket.protocol.http.PageExpiredException;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.flow.RedirectToUrlException;
import org.apache.wicket.request.http.WebRequest;
import org.opensingular.lib.commons.base.SingularException;
import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.lib.support.spring.util.ApplicationContextProvider;
import org.opensingular.lib.wicket.util.application.SingularCsrfPreventionRequestCycleListener;
import org.opensingular.requirement.module.config.IServerContext;
import org.opensingular.requirement.module.exception.SingularServerException;
import org.opensingular.requirement.module.spring.security.SecurityAuthPaths;
import org.opensingular.requirement.module.spring.security.SecurityAuthPathsFactory;
import org.opensingular.requirement.module.spring.security.SingularRequirementUserDetails;
import org.opensingular.requirement.module.wicket.SingularSession;
import org.opensingular.requirement.module.wicket.error.Page403;
import org.opensingular.requirement.module.wicket.error.Page410;
import org.opensingular.requirement.module.wicket.error.Page500;

/**
 * Listener para impedir que páginas de um contexto do wicket sejam acessadas por uma sessão
 * criada em outro contexto  wicket.
 * <p>
 * Esse Listener também é responsável pela segurança CSRF.
 */
public class SingularRequirementContextListener extends SingularCsrfPreventionRequestCycleListener implements Loggable {

    @Override
    public void onRequestHandlerResolved(RequestCycle cycle, IRequestHandler handler) {
        super.onRequestHandlerResolved(cycle, handler);
        if (SingularSession.get().isAuthtenticated() && isPageRequest(handler)) {
            SingularRequirementUserDetails userDetails = SingularSession.get().getUserDetails();
            if (!userDetails.keepLoginThroughContexts()) {
                IServerContext requestCtx = ApplicationContextProvider.get().getBean(IServerContext.class);
                IServerContext sessionCtx = SingularSession.get().getServerContext();
                if (!sessionCtx.equals(requestCtx)) {
                    resetLogin(cycle);
                }
            }
        }
    }

    private void resetLogin(RequestCycle cycle) {
        SecurityAuthPathsFactory securityAuthPathsFactory = new SecurityAuthPathsFactory();
        SecurityAuthPaths securityAuthPaths = securityAuthPathsFactory.get();
        String redirectURL = securityAuthPaths.getLogoutPath(cycle);
        getLogger().info("Redirecting to {}", redirectURL);
        throw new RedirectToUrlException(redirectURL);
    }

    private void redirect403(RequestCycle cycle) {
        cycle.getOriginalResponse().reset();
        cycle.setResponsePage(new Page403());
    }

    @Override
    public IRequestHandler onException(RequestCycle cycle, Exception ex) {
        SingularException singularException = getFirstSingularException(ex);
        if (singularException instanceof SingularServerException
                && ((WebRequest) RequestCycle.get().getRequest()).isAjax()) {
            return new AjaxErrorRequestHandler(singularException);
        } else if (ex instanceof PageExpiredException || causeIsPageExpiredException(ex)) {
            return new RenderPageRequestHandler(new PageProvider(new Page410()));
        } else {
            return new RenderPageRequestHandler(new PageProvider(new Page500(ex)));
        }
    }

    private boolean causeIsPageExpiredException(Throwable ex) {
        if (ex instanceof PageExpiredException) {
            return true;
        }
        return ex.getCause() != null && causeIsPageExpiredException(ex.getCause());
    }

    private SingularException getFirstSingularException(Exception ex) {
        for (Throwable t : ExceptionUtils.getThrowableList(ex)) {
            if (t instanceof SingularException) {
                return (SingularException) t;
            }
        }

        return null;
    }

    private boolean isPageRequest(IRequestHandler handler) {
        return handler instanceof IPageClassRequestHandler;
    }


}
