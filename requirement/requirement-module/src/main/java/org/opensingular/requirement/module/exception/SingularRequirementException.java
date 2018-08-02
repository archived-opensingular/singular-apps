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

package org.opensingular.requirement.module.exception;

import org.opensingular.requirement.module.service.RequirementInstance;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Represents exceptions caused by a manipulation of Requirements.
 *
 * @author Daniel C. Bordin
 * @since 2017-10-21
 */
public class SingularRequirementException extends SingularServerException {

    public SingularRequirementException(@Nonnull String msg) {
        super(msg);
    }

    /** Creates a exception with information about de requirement involved in the exception. */
    public SingularRequirementException(@Nonnull String msg, @Nullable RequirementInstance requirement) {
        super(msg, requirement);
    }

    protected SingularRequirementException(Throwable cause) {
        super(cause);
    }

    public SingularRequirementException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
