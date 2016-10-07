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

package org.opensingular.server.commons.flow;


import org.opensingular.flow.core.ProcessDefinition;
import org.opensingular.form.SIComposite;
import org.opensingular.server.commons.wicket.view.form.FormPageConfig;

import java.io.Serializable;
import java.util.Optional;

@FunctionalInterface
public interface LazyFlowDefinitionResolver extends Serializable {

    Optional<Class<? extends ProcessDefinition>> resolve(FormPageConfig cfg, SIComposite iRoot);

}