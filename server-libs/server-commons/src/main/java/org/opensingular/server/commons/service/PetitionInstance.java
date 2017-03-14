/*
 * Copyright (C) 2016 Singular Studios (a.k.a Atom Tecnologia) - www.opensingular.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
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

package org.opensingular.server.commons.service;

import org.opensingular.flow.core.ProcessInstance;
import org.opensingular.form.SIComposite;
import org.opensingular.lib.support.spring.util.ApplicationContextProvider;
import org.opensingular.server.commons.persistence.entity.form.PetitionEntity;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 *
 * @author Daniel C. Bordin on 07/03/2017.
 */
public class PetitionInstance {

    @Nonnull
    private final PetitionEntity petitionEntity;

    private ProcessInstance processInstance;

    private SIComposite mainForm;

    public PetitionInstance(PetitionEntity petitionEntity) {
        this.petitionEntity = Objects.requireNonNull(petitionEntity);
    }

    final void setProcessInstance(ProcessInstance processInstance) {
        this.processInstance = processInstance;
    }

    public ProcessInstance getProcessInstance() {
        if (processInstance == null) {
            //processInstance = ApplicationContextProvider.get().getBean(PetitionService.class)
        }
        return processInstance;
    }

    @Nonnull
    public SIComposite getMainForm() {
        if (mainForm == null) {
            mainForm = getPetitionService().getMainFormAsInstance(petitionEntity);
        }
        return mainForm;
    }

    private PetitionService<?,?> getPetitionService() {
        return ApplicationContextProvider.get().getBean(PetitionService.class);
    }
}
