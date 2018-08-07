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

package org.opensingular.requirement.module.service;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.opensingular.form.SInstance;
import org.opensingular.requirement.module.service.dto.RequirementSenderFeedback;

/**
 * Implements the logic responsible to transform a draft requirement into a effective requirement that will follow the
 * defined flow of the requirement. So this is the actual method that start de requirement.
 *
 * @see DefaultRequirementSender
 */
public interface RequirementSender {

    @Nonnull
    RequirementSenderFeedback send(@Nonnull RequirementInstance requirement, SInstance instance, @Nullable String codSubmitterActor);

}