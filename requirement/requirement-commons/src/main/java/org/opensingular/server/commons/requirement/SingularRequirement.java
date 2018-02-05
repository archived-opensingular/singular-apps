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

package org.opensingular.server.commons.requirement;

import org.opensingular.form.SType;
import org.opensingular.server.commons.flow.FlowResolver;
import org.opensingular.server.commons.service.DefaultRequirementSender;
import org.opensingular.server.commons.service.RequirementSender;
import org.opensingular.server.commons.wicket.view.form.AbstractFormPage;
import org.opensingular.server.commons.wicket.view.form.FormPage;

/**
 * Singular requirement specification.
 * This class groups the main Singular components needed to orchestrate the requirement.
 */
public interface SingularRequirement {

    /**
     * The requirement descriptive name
     * Must be unique across requirements in the same module
     *
     * @return
     */
    String getName();

    /**
     * A {@link SType} representing the main form for this requirment.
     * The main form is always the initial application form
     *
     * @return
     */
    Class<? extends SType> getMainForm();

    /**
     * Returns an {@link FlowResolver} which is responsible to
     * select the start flow for this requirement based on {@link #getMainForm} SType properly filled.
     *
     * @return
     */
    FlowResolver getFlowResolver();


    /**
     * Returns a custom initial form page.
     * Defaults to {@link FormPage}
     *
     * @return
     */
    default Class<? extends AbstractFormPage<?, ?>> getDefaultExecutionPage() {
        return FormPage.class;
    }


    default Class<? extends RequirementSender> getRequirementSenderBeanClass(){
        return DefaultRequirementSender.class;
    }

}
