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

package org.opensingular.requirement.commons;
/*
 * Copyright 2002-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.opensingular.lib.commons.context.SingularContextSetup;
import org.opensingular.requirement.commons.test.SingularServletContextMock;
import org.opensingular.requirement.module.config.SingularWebAppInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.MergedContextConfiguration;
import org.springframework.test.context.support.AbstractContextLoader;
import org.springframework.test.context.web.WebMergedContextConfiguration;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.context.support.GenericWebApplicationContext;

/**
 * Abstract, generic extension of {@link AbstractContextLoader} that loads a
 * {@link GenericWebApplicationContext}.
 * <p/>
 * <p>If instances of concrete subclasses are invoked via the
 * {@link org.springframework.test.context.SmartContextLoader SmartContextLoader}
 * SPI, the context will be loaded from the {@link MergedContextConfiguration}
 * provided to {@link #loadContext(MergedContextConfiguration)}. In such cases, a
 * {@code SmartContextLoader} will decide whether to load the context from
 * <em>locations</em> or <em>annotated classes</em>. Note that {@code
 * AbstractGenericWebContextLoader} does not support the {@code
 * loadContext(String... locations)} method from the legacy
 * {@link org.springframework.test.context.ContextLoader ContextLoader} SPI.
 * <p/>
 * <p>Concrete subclasses must provide an appropriate implementation of
 * {@link #loadBeanDefinitions}.
 *
 * @author Sam Brannen
 * @see #loadContext(MergedContextConfiguration)
 * @see #loadContext(String...)
 * @since 3.2
 */
public class AbstractSingularContextLoader extends AbstractContextLoader {

    protected static final Log logger = LogFactory.getLog(AbstractSingularContextLoader.class);


    // --- SmartContextLoader -----------------------------------------------

    /**
     * Load a Spring {@link WebApplicationContext} from the supplied
     * {@link MergedContextConfiguration}.
     * <p/>
     * <p>Implementation details:
     * <p/>
     * <ul>
     * <li>Calls {@link #validateMergedContextConfiguration(WebMergedContextConfiguration)}
     * to allow subclasses to validate the supplied configuration before proceeding.</li>
     * <li>Creates a {@link GenericWebApplicationContext} instance.</li>
     * <li>If the supplied {@code MergedContextConfiguration} references a
     * {@linkplain MergedContextConfiguration#getParent() parent configuration},
     * the corresponding {@link MergedContextConfiguration#getParentApplicationContext()
     * ApplicationContext} will be retrieved and
     * {@linkplain GenericWebApplicationContext#setParent(ApplicationContext) set as the parent}
     * for the context created by this method.</li>
     * <li>Delegates to {@link #configureWebResources} to create the
     * {@link MockServletContext} and set it in the {@code WebApplicationContext}.</li>
     * <li>Calls {@link #prepareContext} to allow for customizing the context
     * before bean definitions are loaded.</li>
     * <li>Delegates to {@link #loadBeanDefinitions} to populate the context
     * from the locations or classes in the supplied {@code MergedContextConfiguration}.</li>
     * <li>Delegates to {@link AnnotationConfigUtils} for
     * {@linkplain AnnotationConfigUtils#registerAnnotationConfigProcessors registering}
     * annotation configuration processors.</li>
     * <li>Calls {@link #customizeContext} to allow for customizing the context
     * before it is refreshed.</li>
     * <li>{@link ConfigurableApplicationContext#refresh Refreshes} the
     * context and registers a JVM shutdown hook for it.</li>
     * </ul>
     *
     * @return a new web application context
     * @see org.springframework.test.context.SmartContextLoader#loadContext(MergedContextConfiguration)
     * @see GenericWebApplicationContext
     */
    @Override
    public final AnnotationConfigWebApplicationContext loadContext(MergedContextConfiguration mergedConfig) throws Exception {
        SingularContextSetup.reset();

        if (!(mergedConfig instanceof WebMergedContextConfiguration)) {
            throw new IllegalArgumentException(String.format(
                    "Cannot load WebApplicationContext from non-web merged context configuration %s. "
                            + "Consider annotating your test class with @WebAppConfiguration.", mergedConfig));
        }
        WebMergedContextConfiguration webMergedConfig = (WebMergedContextConfiguration) mergedConfig;

        if (logger.isDebugEnabled()) {
            logger.debug(String.format("Loading WebApplicationContext for merged context configuration %s.",
                    webMergedConfig));
        }

        validateMergedContextConfiguration(webMergedConfig);

        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();

        ApplicationContext parent = mergedConfig.getParentApplicationContext();
        if (parent != null) {
            context.setParent(parent);
        }
        configureWebResources(context, webMergedConfig);
        prepareContext(context, webMergedConfig);
        loadBeanDefinitions(context, webMergedConfig);
        customizeContext(context, webMergedConfig);
        mockRequest();
        context.refresh();
        context.registerShutdownHook();
        return context;
    }

    private void mockRequest() {
        MockHttpSession session;
        MockHttpServletRequest request;
        session = new MockHttpSession();
        request = new MockHttpServletRequest();
        request.setSession(session);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

    }

    /**
     * Validate the supplied {@link WebMergedContextConfiguration} with respect to
     * what this context loader supports.
     * <p>The default implementation is a <em>no-op</em> but can be overridden by
     * subclasses as appropriate.
     *
     * @param mergedConfig the merged configuration to validate
     * @throws IllegalStateException if the supplied configuration is not valid
     *                               for this context loader
     * @since 4.0.4
     */
    protected void validateMergedContextConfiguration(WebMergedContextConfiguration mergedConfig) {
        /* no-op */
    }

    /**
     * Configures web resources for the supplied web application context (WAC).
     * <p/>
     * <h4>Implementation Details</h4>
     * <p/>
     * <p>If the supplied WAC has no parent or its parent is not a WAC, the
     * supplied WAC will be configured as the Root WAC (see "<em>Root WAC
     * Configuration</em>" below).
     * <p/>
     * <p>Otherwise the context hierarchy of the supplied WAC will be traversed
     * to find the top-most WAC (i.e., the root); and the {@link ServletContext}
     * of the Root WAC will be set as the {@code ServletContext} for the supplied
     * WAC.
     * <p/>
     * <h4>Root WAC Configuration</h4>
     * <p/>
     * <ul>
     * <li>The resource base path is retrieved from the supplied
     * {@code WebMergedContextConfiguration}.</li>
     * <li>A {@link ResourceLoader} is instantiated for the {@link MockServletContext}:
     * if the resource base path is prefixed with "{@code classpath:}", a
     * {@link DefaultResourceLoader} will be used; otherwise, a
     * {@link FileSystemResourceLoader} will be used.</li>
     * <li>A {@code MockServletContext} will be created using the resource base
     * path and resource loader.</li>
     * <li>The supplied {@link GenericWebApplicationContext} is then stored in
     * the {@code MockServletContext} under the
     * {@link WebApplicationContext#ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE} key.</li>
     * <li>Finally, the {@code MockServletContext} is set in the
     * {@code WebApplicationContext}.</li>
     *
     * @param context         the web application context for which to configure the web
     *                        resources
     * @param webMergedConfig the merged context configuration to use to load the
     *                        web application context
     */
    protected void configureWebResources(AnnotationConfigWebApplicationContext context,
                                         WebMergedContextConfiguration webMergedConfig) {

        ApplicationContext parent = context.getParent();

        // if the WAC has no parent or the parent is not a WAC, set the WAC as
        // the Root WAC:
        if (parent == null || (!(parent instanceof WebApplicationContext))) {
            String resourceBasePath = webMergedConfig.getResourceBasePath();
            ResourceLoader resourceLoader = resourceBasePath.startsWith(ResourceLoader.CLASSPATH_URL_PREFIX) ? new DefaultResourceLoader()
                    : new FileSystemResourceLoader();

            ServletContext servletContext = new SingularServletContextMock(resourceBasePath, resourceLoader);
            servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, context);
            context.setServletContext(servletContext);
        } else {
            ServletContext servletContext = null;

            // find the Root WAC
            while (parent != null) {
                if (parent instanceof WebApplicationContext && !(parent.getParent() instanceof WebApplicationContext)) {
                    servletContext = ((WebApplicationContext) parent).getServletContext();
                    break;
                }
                parent = parent.getParent();
            }
            Assert.state(servletContext != null, "Failed to find Root WebApplicationContext in the context hierarchy");
            context.setServletContext(servletContext);
        }
    }


    /**
     * Load bean definitions into the supplied {@link GenericWebApplicationContext context}
     * from the locations or classes in the supplied {@code WebMergedContextConfiguration}.
     * <p/>
     * <p>Concrete subclasses must provide an appropriate implementation.
     *
     * @param context         the context into which the bean definitions should be loaded
     * @param webMergedConfig the merged context configuration to use to load the
     *                        web application context
     * @see #loadContext(MergedContextConfiguration)
     */
    protected void loadBeanDefinitions(AnnotationConfigWebApplicationContext context,
                                       WebMergedContextConfiguration webMergedConfig) {
        Class<?>[] annotatedClasses = webMergedConfig.getClasses();
        if (logger.isDebugEnabled()) {
            logger.debug("Registering annotated classes: " + ObjectUtils.nullSafeToString(annotatedClasses));
        }
        context.register(annotatedClasses);
    }


    /**
     * Customização para iniciar o singular
     *
     * @param context
     * @param webMergedConfig
     */
    protected void customizeContext(AnnotationConfigWebApplicationContext context, WebMergedContextConfiguration webMergedConfig) {
        new SingularWebAppInitializer().setSingularInitializer(new CommonsInitializerMock(context)).onStartup(context.getServletContext());
    }

    // --- ContextLoader -------------------------------------------------------

    /**
     * {@code AbstractGenericWebContextLoader} should be used as a
     * {@link org.springframework.test.context.SmartContextLoader SmartContextLoader},
     * not as a legacy {@link org.springframework.test.context.ContextLoader ContextLoader}.
     * Consequently, this method is not supported.
     *
     * @throws UnsupportedOperationException
     * @see org.springframework.test.context.ContextLoader#loadContext(String[])
     */
    @Override
    public final ApplicationContext loadContext(String... locations) throws Exception {
        throw new UnsupportedOperationException(
                "AbstractGenericWebContextLoader does not support the loadContext(String... locations) method");
    }

    @Override
    protected String getResourceSuffix() {
        return "-context.xml";
    }


}
