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

package org.opensingular.requirement.studio.spring;

import org.opensingular.lib.wicket.util.resource.DefaultIcons;
import org.opensingular.requirement.module.spring.SingularDefaultBeanFactory;
import org.opensingular.requirement.module.workspace.WorkspaceRegistry;
import org.opensingular.studio.core.menu.StudioMenu;
import org.springframework.context.annotation.Bean;

public class RequirementStudioBeanFactory extends SingularDefaultBeanFactory {
    @Bean
    public StudioMenu studioMenu(WorkspaceRegistry workspaceRegistry) {
        StudioMenu.Builder builder = StudioMenu.Builder.newPortalMenu();
        workspaceRegistry.getContexts().forEach(ctx -> {
            if (!ctx.getSettings().isHideFromStudioMenu()) {
                builder.addHTTPEndpoint(DefaultIcons.CUBES, ctx.getName(), ctx.getSettings().getUrlPath());
            }
        });
        return builder.getStudioMenu();
    }
}