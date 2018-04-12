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
package org.opensingular.requirement.commons.admin.healthsystem.validation.database;

import org.opensingular.lib.support.spring.util.ApplicationContextProvider;
import org.opensingular.requirement.commons.exception.SingularServerException;

/**
 * Created by vitor.rego on 17/02/2017.
 */
public enum ValidatorFactory {
    ORACLE("Oracle"){
        @Override
        public IValidatorDatabase getValidator() {
            return ApplicationContextProvider.get().getBean(ValidatorOracle.class);
        }
    };

    public abstract IValidatorDatabase getValidator();

    private String description;

    ValidatorFactory(String description){
        this.description = description;
    }

    public static IValidatorDatabase getValidator(String driverDialect) {
        for (ValidatorFactory value: ValidatorFactory.values()) {
            if(driverDialect.contains(value.description)){
                return value.getValidator();
            }
        }
        throw new SingularServerException("Validator não encontrado");
    }
}
