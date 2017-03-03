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

package org.opensingular.server.commons.wicket;

import org.opensingular.lib.commons.base.SingularProperties;
import org.opensingular.server.commons.wicket.error.Page410;
import org.opensingular.server.commons.wicket.listener.SingularServerContextListener;
import org.opensingular.lib.wicket.util.application.SkinnableApplication;
import org.opensingular.lib.wicket.util.page.error.Error403Page;
import org.apache.wicket.RuntimeConfigurationType;
import org.apache.wicket.Session;
import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.apache.wicket.util.lang.Bytes;
import org.apache.wicket.util.time.Duration;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.wicketstuff.annotation.mount.MountPath;
import org.wicketstuff.annotation.scan.AnnotatedMountList;
import org.wicketstuff.annotation.scan.AnnotatedMountScanner;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class SingularApplication extends AuthenticatedWebApplication
        implements ApplicationContextAware, SkinnableApplication {

    public static final String BASE_FOLDER = "/tmp/fileUploader";

    private ApplicationContext applicationContext;

    public static SingularApplication get() {
        return (SingularApplication) WebApplication.get();
    }

    @Override
    public void init() {
        super.init();

        getRequestCycleSettings().setTimeout(Duration.minutes(5));
        getRequestCycleListeners().add(new SingularServerContextListener());

        Locale.setDefault(new Locale("pt", "BR"));

        getApplicationSettings().setAccessDeniedPage(Error403Page.class);
        getApplicationSettings().setPageExpiredErrorPage(Page410.class);

        // Don't forget to check your Application server for this
        getApplicationSettings().setDefaultMaximumUploadSize(Bytes.megabytes(10));

        getMarkupSettings().setStripWicketTags(true);
        getMarkupSettings().setStripComments(true);
        getMarkupSettings().setDefaultMarkupEncoding(StandardCharsets.UTF_8.name());
        getComponentOnConfigureListeners().add(component -> {
            boolean outputId = !component.getRenderBodyOnly();
            component.setOutputMarkupId(outputId).setOutputMarkupPlaceholderTag(outputId);
        });

        if (applicationContext != null) {
            getComponentInstantiationListeners().add(new SpringComponentInjector(this, applicationContext, true));
        } else {
            getComponentInstantiationListeners().add(new SpringComponentInjector(this));
            applicationContext = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
        }

        mountPages();
        getDebugSettings().setComponentPathAttributeName("wicketpath");
        WicketSerializationDebugUtil.configurePageSerializationDebugIfInDevelopmentMode(this, this.getClass());
    }

    @Override
    public Session newSession(Request request, Response response) {
        return new SingularSession(request, response);
    }

    @Override
    protected Class<? extends AbstractAuthenticatedWebSession> getWebSessionClass() {
        return SingularSession.class;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected Class<? extends WebPage> getSignInPageClass() {
        return (Class<? extends WebPage>) getHomePage();
    }

    @Override
    public RuntimeConfigurationType getConfigurationType() {
        if (SingularProperties.get().isFalse(SingularProperties.SINGULAR_DEV_MODE)) {
            return RuntimeConfigurationType.DEPLOYMENT;
        } else {
            return RuntimeConfigurationType.DEVELOPMENT;
        }
    }


    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        this.applicationContext = ctx;
    }

    private void mountPages() {
        List<Class<?>> classes = SingularClassPathScanner
                .INSTANCE
                .findClassesAnnotatedWith(MountPath.class)
                .stream()
                .collect(Collectors.toList());

        Map<String, Class<?>> mountPaths = new HashMap<>();
        for (Class<?> clazz : classes) {
            List<String> paths = new ArrayList<>();
            paths.add(clazz.getAnnotation(MountPath.class).value());
            paths.addAll(Arrays.asList(clazz.getAnnotation(MountPath.class).alt()));
            for (String path : paths) {
                if (mountPaths.containsKey(path)) {
                    throw SingularServerException
                            .rethrow(
                                    String
                                            .format("Duas ou mais classes possuem o mesmo valor ou valor alternativo de @MountPath. Classes %s  e %s",
                                                    clazz.getName(),
                                                    mountPaths.get(path).getName()));
                }
            }
        }

        new SingularAnnotatedMountScanner()
                .scanList(classes)
                .mount(this);


    }


    private static class SingularAnnotatedMountScanner extends AnnotatedMountScanner {
        @Override
        public AnnotatedMountList scanList(List<Class<?>> mounts) {
            return super.scanList(mounts);
        }
    }

}
