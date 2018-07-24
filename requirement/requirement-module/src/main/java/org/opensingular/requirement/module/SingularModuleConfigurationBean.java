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

package org.opensingular.requirement.module;


import org.opensingular.form.SType;
import org.opensingular.lib.commons.scan.SingularClassPathScanner;
import org.opensingular.requirement.module.config.*;
import org.opensingular.requirement.module.flow.builder.RequirementFlowDefinition;
import org.opensingular.requirement.module.service.dto.BoxDefinitionData;
import org.opensingular.requirement.module.service.dto.ItemBox;
import org.opensingular.requirement.module.workspace.BoxDefinition;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.web.context.ServletContextAware;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletContext;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.opensingular.requirement.module.SingularModuleConfiguration.SERVLET_ATTRIBUTE_SGL_MODULE_CONFIG;

/**
 * Configuration bean from which the current module
 * requirements configurations are made available.
 */
@Named
public class SingularModuleConfigurationBean implements ServletContextAware {
    private final BeanFactory beanFactory;
    private final BoxControllerFactory boxControllerFactory;

    private SingularModuleConfiguration singularModuleConfiguration;
    private IServerContext[] contexts;
    private String springMVCServletMapping;
    private Map<String, Object> attrs = new HashMap<>();
    private List<Class<? extends SType<?>>> formTypes;
    private String moduleCod;
    private String[] definitionsPackages;
    private String[] defaultPublicUrls;

    /**
     * Cache for the already created controllers
     */
    private Map<String, BoxController> controllers = new HashMap<>();

    @Inject
    public SingularModuleConfigurationBean(BeanFactory beanFactory, BoxControllerFactory boxControllerFactory) {
        this.beanFactory = beanFactory;
        this.boxControllerFactory = boxControllerFactory;
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        singularModuleConfiguration = (SingularModuleConfiguration) servletContext.getAttribute(SERVLET_ATTRIBUTE_SGL_MODULE_CONFIG);
        WebInitializer webInitializer = (WebInitializer) servletContext.getAttribute(SingularWebApplicationInitializer.SERVLET_ATTRIBUTE_WEB_CONFIGURATION);
        SpringHibernateInitializer springHibernateInitializer = (SpringHibernateInitializer) servletContext.getAttribute(SingularWebApplicationInitializer.SERVLET_ATTRIBUTE_SPRING_HIBERNATE_CONFIGURATION);
        FormInitializer formInitializer = (FormInitializer) servletContext.getAttribute(SingularWebApplicationInitializer.SERVLET_ATTRIBUTE_FORM_CONFIGURATION_CONFIGURATION);
        FlowInitializer flowInitializer = (FlowInitializer) servletContext.getAttribute(SingularWebApplicationInitializer.SERVLET_ATTRIBUTE_FLOW_CONFIGURATION_CONFIGURATION);

        this.contexts = singularModuleConfiguration.getContexts().toArray(new IServerContext[]{});

        List<String> publicUrls = new ArrayList<>(Arrays.asList(webInitializer.getDefaultPublicUrls()));
        for (IServerContext ctx : contexts) {
            publicUrls.addAll(ctx.getPublicUrls());
        }

        this.defaultPublicUrls = publicUrls.toArray(new String[]{});
        this.springMVCServletMapping = springHibernateInitializer.springMVCServletMapping();
        Optional.ofNullable(formInitializer)
                .ifPresent(fi -> this.formTypes = fi.getTypes());

        Optional.ofNullable(flowInitializer)
                .ifPresent(fi -> this.moduleCod = flowInitializer.moduleCod());

        Set<Class<? extends RequirementFlowDefinition>> processes = SingularClassPathScanner.get().findSubclassesOf(RequirementFlowDefinition.class);
        initDefinitionsPackages(processes.stream());
    }

    public SingularModule getModule() {
        return singularModuleConfiguration.getModule();
    }

    public Set<BoxInfo> getBoxByContext(IServerContext context) {
        return singularModuleConfiguration.getBoxByContext(context);
    }

    public Optional<BoxInfo> getBoxByBoxId(String boxId) {
        return singularModuleConfiguration.getBoxByBoxId(boxId);
    }

    public RequirementDefinition getRequirementByKey(String key) {
        return singularModuleConfiguration.getRequirementByKey(key);
    }

    public List<BoxDefinitionData> buildItemBoxes(IServerContext context) {
        return getBoxByContext(context)
                .stream()
                .map(box -> buildBoxDefinitionData(box, context))
                .collect(Collectors.toList());
    }

    public BoxDefinitionData buildBoxDefinitionData(BoxInfo boxInfo, IServerContext context) {
        BoxDefinition factory = beanFactory.getBean(boxInfo.getBoxDefinitionClass());
        ItemBox itemBox = factory.build(context);
        itemBox.setFieldsDatatable(factory.getDatatableFields());
        itemBox.setId(boxInfo.getBoxId());
        return new BoxDefinitionData(itemBox, boxInfo.getRequirements());
    }

    public List<RequirementDefinition> getRequirements() {
        return singularModuleConfiguration.getRequirements();
    }

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

    private void initDefinitionsPackages(Stream<Class<? extends RequirementFlowDefinition>> stream) {
        definitionsPackages = stream.map(c -> c.getPackage().getName()).collect(Collectors.toSet()).toArray(new String[0]);
    }

    public IServerContext findContextByName(String name) {
        return Stream.of(contexts).filter(i -> i.getName().equals(name)).findFirst().orElse(null);
    }

    public Optional<BoxController> getBoxControllerByBoxId(String boxId) {
        if (!controllers.containsKey(boxId)) {
            getBoxByBoxId(boxId).ifPresent(boxInfo1 -> controllers.put(boxId, boxControllerFactory.create(boxInfo1)));
        }
        return Optional.ofNullable(controllers.get(boxId));
    }
}