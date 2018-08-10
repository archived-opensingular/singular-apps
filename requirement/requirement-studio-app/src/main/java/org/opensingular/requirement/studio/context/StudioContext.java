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

package org.opensingular.requirement.studio.context;

import org.opensingular.requirement.module.WorkspaceConfiguration;
import org.opensingular.requirement.module.config.ServerContext;
import org.opensingular.requirement.module.spring.security.AbstractSingularSpringSecurityAdapter;
import org.opensingular.requirement.studio.wicket.RequirementStudioApplication;

public class StudioContext extends ServerContext {
    public static final String NAME = "STUDIO";

    public StudioContext() {
        super(NAME, "/*", "singular.studio");
    }

    @Override
    public Class<? extends RequirementStudioApplication> getWicketApplicationClass() {
        return RequirementStudioApplication.class;
    }

    @Override
    public Class<? extends AbstractSingularSpringSecurityAdapter> getSpringSecurityConfigClass() {
        return null;
    }

    @Override
    public void setup(WorkspaceConfiguration workspaceConfiguration) {

    }
}