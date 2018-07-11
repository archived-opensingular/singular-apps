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

package org.opensingular.requirement.module.service;

import javax.inject.Inject;
import javax.inject.Named;

import org.opensingular.requirement.commons.WorkspaceMetadataMockBean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;

@Primary
@Named
@DependsOn(value = "workspaceMetadataMockBean")
@Scope("session")
public class SingularServerSessionConfigurationMock extends SingularServerSessionConfiguration {

    @Inject
    private WorkspaceMetadataMockBean workspaceMetadataMockBean;


    @Override
    public void init() {
        setConfigMaps(workspaceMetadataMockBean.getMap());
    }

}
