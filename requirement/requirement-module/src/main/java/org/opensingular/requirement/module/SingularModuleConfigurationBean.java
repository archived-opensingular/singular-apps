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
import org.opensingular.requirement.module.config.IServerContext;
import org.opensingular.requirement.module.service.dto.BoxDefinitionData;
import org.opensingular.requirement.module.service.dto.ItemBox;
import org.opensingular.requirement.module.workspace.BoxDefinition;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.web.context.ServletContextAware;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.opensingular.requirement.module.SingularModuleConfiguration.SERVLET_ATTRIBUTE_SGL_MODULE_CONFIG;

/**
 * Configuration bean from which the current module
 * requirements configurations are made available.
 */
public class SingularModuleConfigurationBean implements ServletContextAware {
    @Inject
    private BeanFactory beanFactory;

    @Inject
    private BoxControllerFactory boxControllerFactory;

    private SingularModuleConfiguration singularModuleConfiguration;
    private IServerContext[] contexts;
    private Map<String, Object> attrs = new HashMap<>();

    /**
     * Cache for the already created controllers
     */
    private Map<String, BoxController> controllers = new HashMap<>();

    private List<String> publicUrls;

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.singularModuleConfiguration = (SingularModuleConfiguration) servletContext.getAttribute(SERVLET_ATTRIBUTE_SGL_MODULE_CONFIG);
        this.contexts = singularModuleConfiguration.getContexts().toArray(new IServerContext[]{});
        this.publicUrls = new ArrayList<>(singularModuleConfiguration.getPublicUrls());
        for (IServerContext ctx : contexts) {
            this.publicUrls.addAll(ctx.getPublicUrls());
        }
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

    public SingularRequirement getRequirementById(Long id) {
        return singularModuleConfiguration.getRequirementById(id);
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

    public List<SingularRequirement> getRequirements() {
        return singularModuleConfiguration.getRequirements();
    }

    public IServerContext[] getContexts() {
        return contexts;
    }

    public Object setAttribute(String name, Object value) {
        return attrs.put(name, value);
    }

    public Object getAttribute(String name) {
        return attrs.get(name);
    }

    public List<Class<? extends SType<?>>> getFormTypes() {
        if (singularModuleConfiguration.getFormTypes() == null) {
            return Collections.emptyList();
        } else {
            return Collections.unmodifiableList(singularModuleConfiguration.getFormTypes());
        }
    }

    public String getModuleCod() {
        return singularModuleConfiguration.getModule().abbreviation();
    }

    public String[] getDefinitionsPackages() {
        return singularModuleConfiguration.getDefinitionsPackages();
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

    public List<String> getPublicUrls() {
        return publicUrls;
    }
}