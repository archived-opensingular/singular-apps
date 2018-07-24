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

import org.opensingular.requirement.module.workspace.BoxDefinition;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;


/**
 * Configuration object for module {@link BoxDefinition} registrations.
 */
public class WorkspaceConfiguration {
    private final Set<BoxInfo> boxInfos = new LinkedHashSet<>();
    private final AnnotationConfigWebApplicationContext applicationContext;

    private BoxInfo latest;

    public WorkspaceConfiguration(AnnotationConfigWebApplicationContext applicationContext) {
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
    @SafeVarargs
    @Deprecated
    public final WorkspaceConfiguration newFor(Class<? extends RequirementDefinition>... requirement) {
        Arrays.stream(requirement).forEach(latest::addSingularRequirementRef);
        return this;
    }

    public Set<BoxInfo> getBoxInfos() {
        return boxInfos;
    }
}