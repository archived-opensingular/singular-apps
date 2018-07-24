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

package org.opensingular.requirement.module.wicket.view.form;

import org.opensingular.form.SType;

import java.io.Serializable;

public class TransitionConfirmConfig<T extends SType<?>> implements Serializable {

    private Class<T> type;
    private boolean  validatePageForm;

    private TransitionConfirmConfig(Class<T> type, boolean validatePageForm) {
        this.type = type;
        this.validatePageForm = validatePageForm;
    }

    public static <T extends SType<?>> TransitionConfirmConfig<T> newConfigWithoutValidation(Class<T> type){
        return new TransitionConfirmConfig<T>(type, false);
    }

    public static <T extends SType<?>> TransitionConfirmConfig<T> newConfigWithValidation(Class<T> type){
        return new TransitionConfirmConfig<T>(type, true);
    }

    public Class<T> getType() {
        return type;
    }

    public TransitionConfirmConfig setType(Class<T> type) {
        this.type = type;
        return this;
    }

    public boolean isValidatePageForm() {
        return validatePageForm;
    }

    public TransitionConfirmConfig setValidatePageForm(boolean validatePageForm) {
        this.validatePageForm = validatePageForm;
        return this;
    }

}