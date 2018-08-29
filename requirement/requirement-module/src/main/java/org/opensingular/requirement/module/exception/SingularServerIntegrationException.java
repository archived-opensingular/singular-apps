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

package org.opensingular.requirement.module.exception;

public class SingularServerIntegrationException extends SingularServerException {

    protected SingularServerIntegrationException(String serviceName, Throwable e) {
        super(String.format("O %s não está funcionando corretamente. Não foi possível realizar a operação.", serviceName), e);
    }

    protected SingularServerIntegrationException(String serviceName, String specificError, Throwable e) {
        super(String.format("O %s não está funcionando corretamente. Não foi possível realizar a operação. %s", serviceName, specificError), e);
    }

    public static SingularServerIntegrationException rethrow(Throwable e) {
        return rethrow(null, e);
    }

    public static SingularServerIntegrationException rethrow(String message) {
        return rethrow(message, null);
    }

    public static SingularServerIntegrationException rethrow(String message, Throwable e) {
        if (e instanceof SingularServerIntegrationException) {
            return (SingularServerIntegrationException) e;
        } else {
            return new SingularServerIntegrationException(message, e);
        }
    }

    public static SingularServerIntegrationException rethrow(String message, String specificError, Throwable e) {
        if (e instanceof SingularServerIntegrationException) {
            return (SingularServerIntegrationException) e;
        } else {
            return new SingularServerIntegrationException(message, specificError, e);
        }
    }

}