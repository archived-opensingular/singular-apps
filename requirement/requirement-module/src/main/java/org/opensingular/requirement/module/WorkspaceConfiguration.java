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

import org.opensingular.lib.commons.lambda.IFunction;
import org.opensingular.requirement.module.builder.SingularRequirementBuilder;
import org.opensingular.requirement.module.service.dto.RequirementData;
import org.opensingular.requirement.module.workspace.BoxDefinition;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Configuration object for module {@link BoxDefinition} registrations.
 */
public class WorkspaceConfiguration {
    private final List<BoxInfo> boxInfos = new ArrayList<>();
    private final RequirementConfiguration requirementConfiguration;
    private final AnnotationConfigWebApplicationContext applicationContext;

    private BoxInfo latest;

    public WorkspaceConfiguration(RequirementConfiguration requirementConfiguration, AnnotationConfigWebApplicationContext applicationContext) {
        this.requirementConfiguration = requirementConfiguration;
        this.applicationContext = applicationContext;
    }

    public WorkspaceConfiguration addBox(Class<? extends BoxDefinition> itemBoxClass) {
        applicationContext.register(itemBoxClass);
        BoxInfo boxInfo = new DefaultBoxInfo(itemBoxClass);
        boxInfos.add(boxInfo);
        latest = boxInfo;
        return this;
    }

    /**
     * API_VIEW, acredito que o melhor lugar é dentro da definição da caixa?
     */
    @Deprecated
    public WorkspaceConfiguration newFor(RequirementProvider... requirementProvider) {
        Arrays.stream(requirementProvider)
                .map(requirementConfiguration::getRequirementRef).forEach(latest::addSingularRequirementRef);
        return this;

    }

    /**
     * API_VIEW, acredito que o melhor lugar é dentro da definição da caixa?
     */
    @Deprecated
    public WorkspaceConfiguration newFor(SingularRequirement... requirement) {
        Arrays.stream(requirement)
                .map(requirementConfiguration::getRequirementRef).forEach(latest::addSingularRequirementRef);
        return this;
    }

    public interface RequirementProvider extends IFunction<SingularRequirementBuilder, SingularRequirement> {
    }

    public List<BoxInfo> getBoxInfos() {
        return boxInfos;
    }
}