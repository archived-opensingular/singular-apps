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

package org.opensingular.requirement.module.flow;


import java.io.Serializable;
import java.util.Optional;

import org.opensingular.flow.core.FlowDefinition;
import org.opensingular.form.SIComposite;
import org.opensingular.requirement.module.wicket.view.form.FormPageExecutionContext;

/**
 * Programmatically resolves the {@link FlowDefinition} for a given input data.
 * When the users submits its initial application the Singular Server calls the proper
 * {@link FlowResolver} in order to select which {@link FlowDefinition} it should start
 */
@FunctionalInterface
public interface FlowResolver extends Serializable {

    /**
     * return an optional {@link FlowDefinition}
     * @param cfg
     *
     * @param iRoot
     *  form input data
     * @return
     */
    Optional<Class<? extends FlowDefinition>> resolve(FormPageExecutionContext cfg, SIComposite iRoot);

}