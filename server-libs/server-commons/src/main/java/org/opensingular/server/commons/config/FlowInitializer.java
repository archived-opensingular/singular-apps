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

package org.opensingular.server.commons.config;

import org.opensingular.flow.core.ProcessDefinitionCache;
import org.opensingular.flow.core.SingularFlowConfigurationBean;
import org.opensingular.flow.core.service.IFlowMetadataREST;
import org.opensingular.server.commons.flow.SingularServerFlowConfigurationBean;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import javax.servlet.ServletContext;
import java.util.Optional;

public abstract class FlowInitializer {


    public abstract Class<? extends IFlowMetadataREST> flowMetadataProvider();


    public Class<? extends SingularFlowConfigurationBean> singularFlowConfiguration() {
        return SingularServerFlowConfigurationBean.class;
    }

    public abstract String[] definitionsBasePackage();

    public abstract String processGroupCod();

    public void init(ServletContext ctx, AnnotationConfigWebApplicationContext applicationContext) {
        ProcessDefinitionCache.invalidateAll();
        applicationContext.register(singularFlowConfiguration());
        Optional
                .ofNullable(flowMetadataProvider())
                .ifPresent(applicationContext::register);
    }
}
