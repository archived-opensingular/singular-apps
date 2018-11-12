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

package org.opensingular.requirement.module.flow;

import org.opensingular.form.document.SDocument;
import org.opensingular.lib.commons.context.RefService;
import org.opensingular.lib.commons.lambda.IConsumer;
import org.opensingular.lib.support.spring.util.ApplicationContextProvider;
import org.opensingular.requirement.module.service.RequirementService;
import org.opensingular.requirement.module.service.ServerSInstanceFlowAwareService;

public class ProcessServiceSetup implements IConsumer<SDocument> {


    private Long requirementId;


    public ProcessServiceSetup(Long requirementId) {
        this.requirementId = requirementId;
    }

    @Override
    public void accept(SDocument document) {
        RefService<ServerSInstanceFlowAwareService> ref = RefService.of(
                (ServerSInstanceFlowAwareService) () -> ApplicationContextProvider
                        .get()
                        .getBean(RequirementService.class)
                        .loadRequirementInstance(requirementId)
                        .getFlowInstance());
        document.bindLocalService("processService", ServerSInstanceFlowAwareService.class, ref);
    }


}
