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

import org.opensingular.flow.core.FlowDefinition;
import org.opensingular.flow.core.FlowInstance;
import org.opensingular.flow.core.FlowMap;
import org.opensingular.flow.core.SingularFlowException;
import org.opensingular.flow.core.property.MetaDataKey;
import org.opensingular.flow.core.variable.VarDefinitionMap;

import javax.annotation.Nonnull;

/**
 * Representa uma definição de fluxo especializada em requerimentos. Apresenta comportamentos e configurações
 * adicionais específicos de requerimentos.
 *
 * @author Daniel C. Bordin on 22/03/2017.
 */
public abstract class RequirementFlowDefinition<I extends FlowInstance> extends FlowDefinition<I> {

    public static final MetaDataKey<Boolean> HIDE_FROM_HISTORY = MetaDataKey.of("hideFromHistory", Boolean.class,
            Boolean.FALSE);

    protected RequirementFlowDefinition(Class<I> instanceClass) {
        super(instanceClass);
        declareVariables(getVariables());
    }

    /**
     * Delega a criação dos passos do fluxo para {@link #buildFlow(RequirementFlowBuilder)}.
     * @see FlowDefinition#createFlowMap()
     */
    @Override
    @Nonnull
    protected final FlowMap createFlowMap() {
        try {
            RequirementFlowBuilder flowBuilder = RequirementFlowBuilder.of(this);
            buildFlow(flowBuilder);
            onAfterBuildFlow(flowBuilder);
            FlowMap flowMap = flowBuilder.build();
            if (flowMap == null) {
                throw new SingularFlowException(
                        "O método " + getClass().getSimpleName() + ".buildFlow(flowBuilder) retornou null");
            }
            return flowMap;
        } catch (Exception e) {
            SingularFlowException ex = new SingularFlowException("Erro criando fluxo do processo '" + getName() + '\'', e);
            ex.add(this);
            throw ex;
        }
    }

    /**
     * Utility method for variables declarations
     * @param variables
     */
    protected void declareVariables(VarDefinitionMap<?> variables){
    }

    /**
     * Método a ser implementado com a criação do passos e transições de uma definição de fluxo.
     */
    protected abstract void buildFlow(@Nonnull RequirementFlowBuilder flow);


    /**
     * Método chamado logo apos a execução de {@link #buildFlow(RequirementFlowBuilder)} para uso como ponto de
     * extensão.
     */
    @Nonnull
    protected void onAfterBuildFlow(@Nonnull RequirementFlowBuilder flow) {
    }
}
