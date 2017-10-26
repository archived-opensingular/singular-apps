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

package org.opensingular.server.commons.exception;

import org.opensingular.lib.commons.base.SingularException;
import org.opensingular.server.commons.service.RequirementInstance;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Exceções do Singular server e seus módulos
 */
public class SingularServerException extends SingularException {

    public SingularServerException(@Nonnull String msg) {
        super(msg);
    }

    /** Creates a exception with information about de requirement involved in the exception. */
    public SingularServerException(@Nonnull String msg, @Nullable RequirementInstance requirement) {
        super(msg);
        add(requirement);
    }

    protected SingularServerException(Throwable cause) {
        super(cause);
    }

    protected SingularServerException(String msg, Throwable cause) {
        super(msg, cause);
    }


    public static SingularServerException rethrow(Throwable e) {
        return rethrow(null, e);
    }

    @Deprecated
    public static SingularServerException rethrow(String message) {
        return rethrow(message, null);
    }

    public static SingularServerException rethrow(String message, Throwable e) {
        if (e instanceof SingularServerException) {
            if (message == null) {
                return (SingularServerException) e;
            }
        } else if (message == null){
            return new SingularServerException(e);
        } else if (e == null) {
            return new SingularServerException(message);
        }
        return new SingularServerException(message, e);
    }

    /** Adds information about de requirement involved in the exception. */
    @Nonnull
    public SingularServerException add(@Nullable RequirementInstance requirement) {
        if (requirement != null) {
            add("codRequirement", () -> requirement.getCod());
            add("requirementFlow", () -> requirement.getFlowDefinitionOpt().map(d -> d.getName()).orElse(null));
            add("requirementDescription", () -> requirement.getDescription());
        }
        return this;
    }
}
