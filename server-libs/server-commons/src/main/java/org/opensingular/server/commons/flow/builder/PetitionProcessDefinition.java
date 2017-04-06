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

package org.opensingular.server.commons.flow.builder;

import org.opensingular.flow.core.FlowMap;
import org.opensingular.flow.core.ProcessDefinition;
import org.opensingular.flow.core.ProcessInstance;
import org.opensingular.flow.core.SingularFlowException;

import javax.annotation.Nonnull;

/**
 * Representa uma definição de processo especializada em requerimentos. Apresenta comportamentos e configurações
 * adicionais específicos de requerimentos.
 *
 * @author Daniel C. Bordin on 22/03/2017.
 */
public abstract class PetitionProcessDefinition<I extends ProcessInstance> extends ProcessDefinition<I> {

    protected PetitionProcessDefinition(Class<I> instanceClass) {
        super(instanceClass);
    }

//    protected PetitionProcessDefinition(Class<I> processInstanceClass, VarService varService) {
//        super(processInstanceClass, varService);
//    }

    /**
     * Delega a criação do fluxo do processo para {@link #buildFlow(FlowBuilderPetition)}.
     * @see ProcessDefinition#createFlowMap()
     */
    @Override
    @Nonnull
    protected final FlowMap createFlowMap() {
        try {
            FlowBuilderPetition flowBuilder = FlowBuilderPetition.of(this);
            buildFlow(flowBuilder);
            onAfterBuildFlow(flowBuilder);
            FlowMap flowMap = flowBuilder.build();
            if (flowMap == null) {
                throw new SingularFlowException(
                        "O método " + getClass().getSimpleName() + ".buildFlow(flowBuilder) retornou null");
            }
            configureActions(flowMap);
            return flowMap;
        } catch (Exception e) {
            SingularFlowException ex = new SingularFlowException("Erro criando fluxo do processo '" + getName() + '\'', e);
            ex.add(this);
            throw ex;
        }
    }

    /**
     * Método a ser implementado com a criação do fluxo de processo.
     */
    protected abstract void buildFlow(@Nonnull FlowBuilderPetition flow);

    protected void configureActions(FlowMap flowMap){
    }


    /**
     * Método chamado logo apos a execução de {@link #buildFlow(FlowBuilderPetition)} para uso como ponto de
     * extensão.
     */
    @Nonnull
    protected void onAfterBuildFlow(@Nonnull FlowBuilderPetition flow) {
    }
}
