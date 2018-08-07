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

package org.opensingular.requirement.module.spring;

import org.opensingular.lib.support.spring.util.AutoScanDisabled;
import org.opensingular.requirement.module.SingularModuleConfiguration;
import org.opensingular.requirement.module.WorkspaceAppInitializerListener;
import org.opensingular.requirement.module.config.IServerContext;
import org.opensingular.requirement.module.spring.security.AbstractSingularSpringSecurityAdapter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;


@AutoScanDisabled
@Configuration
public class SingularBeanPostProcessor implements BeanPostProcessor, ServletContextAware {
    private ServletContext servletContext;

    /**
     * @return o SingularModuleConfiguration extraido do ServletContext
     * @see WorkspaceAppInitializerListener
     */
    @Bean
    public SingularModuleConfiguration singularModuleConfiguration() {
        return (SingularModuleConfiguration) servletContext
                .getAttribute(WorkspaceAppInitializerListener.SERVLET_ATTRIBUTE_SGL_MODULE_CONFIG);
    }

    /**
     * Faz o pos processamento de beans antes de sua inicialização,
     * para as configurações quem herdem de {@link AbstractSingularSpringSecurityAdapter}
     * o método {@link AbstractSingularSpringSecurityAdapter#setContext(IServerContext)} será acionado
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (AbstractSingularSpringSecurityAdapter.class.isAssignableFrom(bean.getClass())) {
            for (IServerContext context : singularModuleConfiguration().getContexts()) {
                if (context.getSpringSecurityConfigClass() != null &&
                        context.getSpringSecurityConfigClass().isAssignableFrom(bean.getClass())) {
                    ((AbstractSingularSpringSecurityAdapter) bean).setContext(context);
                }
            }
        }
        return bean;
    }

    /**
     * Comportamento padrão
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    /**
     * Recebe o ServletContext
     * @see ServletContextAware
     */
    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
}