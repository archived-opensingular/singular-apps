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

package org.opensingular.requirement.module;

import org.opensingular.requirement.module.persistence.entity.form.RequirementApplicant;
import org.opensingular.requirement.module.service.RequirementInstance;
import org.opensingular.requirement.module.service.dto.RequirementSubmissionResponse;

import java.io.Serializable;


/**
 * Intercepts several after and before most steps of sending a new requirement.
 *
 * @param <RI>
 * @param <RSR>
 */
public interface RequirementSendInterceptor<RI extends RequirementInstance, RSR extends RequirementSubmissionResponse> extends Serializable {

    /**
     * First method called to create a new object representing the response.
     * This same returned instance is passed down though other methods during the same send
     *
     * @return
     */
    RSR newInstanceSubmissionResponse();

    /**
     * Receive the default generated applicant based on the logged in user.
     * This can be used to change any data related with the applicant to be
     * associated with the requirement.
     *
     * @param applicant
     * @return
     */
    RequirementApplicant configureApplicant(RequirementApplicant applicant);


    /**
     * Called after applicant configuration.
     * This is the #1 interception
     *
     * @param requirement
     * @param applicant
     * @param response
     */
    void onBeforeSend(RI requirement, RequirementApplicant applicant, RSR response);


    /**
     * Requirement is already configured but the flow is not already started
     * This is the #2 interception
     *
     * @param requirement
     * @param applicant
     * @param response
     */
    void onBeforeStartFlow(RI requirement, RequirementApplicant applicant, RSR response);

    /**
     * Requirement is already configured and the flow already started
     * This is the #3 interception
     *
     * @param requirement
     * @param applicant
     * @param response
     */
    void onAfterStartFlow(RI requirement, RequirementApplicant applicant, RSR response);


    /**
     * Requirement is sent, everything is done, this is the last call.
     * This is the #4 interception
     *
     * @param requirement
     * @param applicant
     * @param response
     */
    void onAfterSend(RI requirement, RequirementApplicant applicant, RSR response);

}
