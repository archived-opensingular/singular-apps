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

/**
 * Exceções do Singular server e seus módulos
 */
public class SingularServerException extends SingularException {

    public SingularServerException(String msg) {
        super(msg);
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

}
