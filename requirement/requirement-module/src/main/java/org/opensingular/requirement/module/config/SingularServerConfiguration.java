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

package org.opensingular.requirement.module.config;


import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.servlet.ServletContext;

import org.opensingular.form.SType;
import org.opensingular.lib.commons.scan.SingularClassPathScanner;
import org.opensingular.requirement.module.flow.builder.RequirementFlowDefinition;
import org.springframework.web.context.ServletContextAware;

/**
 * Spring Bean para guardar parametros de configuração reutilizáveis
 * para a solução do singular
 */
public class SingularServerConfiguration implements ServletContextAware {

    private IServerContext[] contexts;
    private String springMVCServletMapping;
    private Map<String, Object> attrs = new HashMap<>();
    private List<Class<? extends SType<?>>> formTypes;
    private String moduleCod;
    private String[] definitionsPackages;
    private String[] defaultPublicUrls;

    public String[] getDefaultPublicUrls() {
        return defaultPublicUrls;
    }

    public IServerContext[] getContexts() {
        return contexts;
    }

    public String getSpringMVCServletMapping() {
        return springMVCServletMapping;
    }

    public Object setAttribute(String name, Object value) {
        return attrs.put(name, value);
    }

    public Object getAttribute(String name) {
        return attrs.get(name);
    }

    public List<Class<? extends SType<?>>> getFormTypes() {
        if (formTypes == null) {
            return Collections.emptyList();
        } else {
            return Collections.unmodifiableList(formTypes);
        }
    }

    public String getModuleCod() {
        return moduleCod;
    }

    public String[] getDefinitionsPackages() {
        return definitionsPackages;
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        WebInitializer webInitializer = (WebInitializer) servletContext.getAttribute(SingularWebApplicationInitializer.SERVLET_ATTRIBUTE_WEB_CONFIGURATION);
        SpringHibernateInitializer springHibernateInitializer = (SpringHibernateInitializer) servletContext.getAttribute(SingularWebApplicationInitializer.SERVLET_ATTRIBUTE_SPRING_HIBERNATE_CONFIGURATION);
        FormInitializer formInitializer = (FormInitializer) servletContext.getAttribute(SingularWebApplicationInitializer.SERVLET_ATTRIBUTE_FORM_CONFIGURATION_CONFIGURATION);
        FlowInitializer flowInitializer = (FlowInitializer) servletContext.getAttribute(SingularWebApplicationInitializer.SERVLET_ATTRIBUTE_FLOW_CONFIGURATION_CONFIGURATION);
        this.contexts = webInitializer.serverContexts();
        this.defaultPublicUrls = webInitializer.getDefaultPublicUrls();
        this.springMVCServletMapping = springHibernateInitializer.springMVCServletMapping();
        Optional.ofNullable(formInitializer)
                .ifPresent(fi -> this.formTypes = fi.getTypes());

        Optional.ofNullable(flowInitializer)
                .ifPresent(fi -> this.moduleCod = flowInitializer.moduleCod());


        Set<Class<? extends RequirementFlowDefinition>> processes = SingularClassPathScanner.get().findSubclassesOf(RequirementFlowDefinition.class);
        initDefinitionsPackages(processes.stream());


    }

    private void initDefinitionsPackages(Stream<Class<? extends RequirementFlowDefinition>> stream) {
        definitionsPackages = stream.map(c -> c.getPackage().getName()).collect(Collectors.toSet()).toArray(new String[0]);
    }

}
