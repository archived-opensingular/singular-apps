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

import org.apache.wicket.markup.html.WebPage;
import org.opensingular.flow.core.FlowInstance;
import org.opensingular.flow.core.ITaskDefinition;
import org.opensingular.flow.core.ITaskPageStrategy;
import org.opensingular.flow.core.SBusinessRole;
import org.opensingular.flow.core.SStart;
import org.opensingular.flow.core.STask;
import org.opensingular.flow.core.STaskEnd;
import org.opensingular.flow.core.STaskHuman;
import org.opensingular.flow.core.STaskJava;
import org.opensingular.flow.core.STaskWait;
import org.opensingular.flow.core.STransition;
import org.opensingular.flow.core.UserRoleSettingStrategy;
import org.opensingular.flow.core.builder.BuilderBusinessRole;
import org.opensingular.flow.core.builder.BuilderEnd;
import org.opensingular.flow.core.builder.BuilderHuman;
import org.opensingular.flow.core.builder.BuilderJava;
import org.opensingular.flow.core.builder.BuilderTask;
import org.opensingular.flow.core.builder.BuilderWait;
import org.opensingular.flow.core.builder.FlowBuilder;
import org.opensingular.flow.core.builder.FlowBuilderImpl;
import org.opensingular.lib.commons.base.SingularUtil;
import org.opensingular.server.commons.flow.SingularRequirementTaskPageStrategy;

import javax.annotation.Nonnull;

/**
 * Construtor de fluxo especializado em fluxo para requerimentos. Apresenta configurações adicionais às encontradas
 * em {@link FlowBuilder}.
 *
 * @author Daniel C. Bordin on 23/03/2017.
 * @see FlowBuilder
 */
public class RequirementFlowBuilder extends
        FlowBuilder<RequirementFlowDefinition<?>, FlowMapPetition, RequirementFlowBuilder.BuilderTaskPetition,
                RequirementFlowBuilder.BuilderJavaTaskPetition, RequirementFlowBuilder.BuilderHumanTaskPetition,
                RequirementFlowBuilder.BuilderWaitTaskPetition, RequirementFlowBuilder.BuilderEndTaskPetition,
                RequirementFlowBuilder.BuilderStartPetition, RequirementFlowBuilder.BuilderTransitionPetition,
                RequirementFlowBuilder.BuilderRolePetition, ITaskDefinition> {

    private RequirementFlowBuilder(RequirementFlowDefinition<?> processDefinition) {
        super(processDefinition);
    }

    /**
     * Cria um novo FlowBuilderPetition para a definição de processo em questão.
     */
    public static RequirementFlowBuilder of(RequirementFlowDefinition<?> processDefinition) {
        return new RequirementFlowBuilder(processDefinition);
    }

    @Override
    protected BuilderTaskPetition newTask(STask<?> task) {
        return new BuilderTaskPetition(this, task);
    }

    @Override
    protected BuilderJavaTaskPetition newJavaTask(STaskJava taskJava) {
        return new BuilderJavaTaskPetition(this, taskJava);
    }

    @Override
    protected BuilderHumanTaskPetition newHumanTask(STaskHuman taskHuman) {
        return new BuilderHumanTaskPetition(this, (STaskHumanPetition) taskHuman);
    }

    @Override
    protected BuilderWaitTaskPetition newWaitTask(STaskWait taskWait) {
        return new BuilderWaitTaskPetition(this, taskWait);
    }

    @Override
    protected BuilderEndTaskPetition newEndTask(STaskEnd taskFim) {
        return new BuilderEndTaskPetition(this, taskFim);
    }

    @Override
    protected BuilderStartPetition newStartTask(SStart start) {
        return new BuilderStartPetition(start);
    }

    @Override
    protected BuilderTransitionPetition newTransition(STransition transicao) {
        return new BuilderTransitionPetition(this, transicao);
    }

    @Override
    protected BuilderRolePetition newProcessRole(SBusinessRole papel) {
        return new BuilderRolePetition(papel);
    }

    @Override
    protected FlowMapPetition newFlowMap(RequirementFlowDefinition<?> definicaoProcesso) {
        return new FlowMapPetition(definicaoProcesso);
    }


    @Override
    public BuilderRolePetition addRoleDefinition(String description,
                                                 UserRoleSettingStrategy<? extends FlowInstance> userRoleSettingStrategy,
                                                 boolean automaticUserAllocation) {
        return addRoleDefinition(description, SingularUtil.convertToJavaIdentity(description, true, false),
                userRoleSettingStrategy, automaticUserAllocation);
    }

    /**
     * Builder (configurador) de {@link STask} especializado em requerimentos. Apresenta comportamentos
     * adicionais específicos de requerimentos.
     */
    public abstract static class ImplBuilderTaskPetition<SELF extends ImplBuilderTaskPetition<SELF, TASK>, TASK
            extends STask<?>>
            extends FlowBuilderImpl.ImplBuilderTask<SELF, TASK> implements BuilderTask {

        ImplBuilderTaskPetition(RequirementFlowBuilder fluxoBuilder, TASK task) {
            super(fluxoBuilder, task);
        }

        @Override
        protected RequirementFlowBuilder getFlowBuilder() {
            return (RequirementFlowBuilder) super.getFlowBuilder();
        }

        /**
         * Cria uma nova transição da task atual para a task destino informada
         * com o nome informado.
         */
        @Override
        public BuilderTransitionPetition go(String actionName, ITaskDefinition taskRefDestiny) {
            return (BuilderTransitionPetition) super.go(actionName, taskRefDestiny);
        }

        /**
         * Cria uma nova transição da task atual para a task destino informada
         */
        @Override
        public BuilderTransitionPetition go(ITaskDefinition taskRefDestiny) {
            return (BuilderTransitionPetition) super.go(taskRefDestiny);
        }

    }

    /**
     * Builder (configurador) de {@link STask} especializado em requerimentos. Apresenta comportamentos
     * adicionais específicos de requerimentos.
     */
    public static class BuilderTaskPetition extends ImplBuilderTaskPetition<BuilderTaskPetition, STask<?>> {

        BuilderTaskPetition(RequirementFlowBuilder flowBuilder, STask<?> task) {
            super(flowBuilder, task);
        }
    }

    /**
     * Builder (configurador) de {@link STaskJava} especializado em requerimentos. Apresenta comportamentos
     * adicionais específicos de requerimentos.
     */
    public static class BuilderJavaTaskPetition extends ImplBuilderTaskPetition<BuilderJavaTaskPetition, STaskJava>
            implements BuilderJava<BuilderJavaTaskPetition> {

        BuilderJavaTaskPetition(RequirementFlowBuilder flowBuilder, STaskJava task) {
            super(flowBuilder, task);
        }

    }

    /**
     * Builder (configurador) de {@link STaskHuman} especializado em requerimentos.
     * Apresenta comportamentos adicionais específicos de requerimentos.
     * <p>Trabalha com {@link STaskHumanPetition}.</p>
     */
    public static class BuilderHumanTaskPetition
            extends ImplBuilderTaskPetition<BuilderHumanTaskPetition, STaskHuman>
            implements BuilderHuman<BuilderHumanTaskPetition> {

        BuilderHumanTaskPetition(RequirementFlowBuilder flowBuilder, STaskHumanPetition task) {
            super(flowBuilder, task);
        }

        @Nonnull
        public BuilderHumanTaskPetition withExecutionPage(@Nonnull Class<? extends WebPage> pageClass) {
            getTask().setExecutionPage(SingularRequirementTaskPageStrategy.of(pageClass));
            return self();
        }

        @Override
        public STaskHumanPetition getTask() {
            return (STaskHumanPetition) super.getTask();
        }
    }

    /**
     * Builder (configurador) de {@link STaskWait} especializado em requerimentos.
     * Apresenta comportamentos adicionais específicos de requerimentos.
     */
    public static class BuilderWaitTaskPetition extends ImplBuilderTaskPetition<BuilderWaitTaskPetition, STaskWait>
            implements BuilderWait<BuilderWaitTaskPetition> {

        BuilderWaitTaskPetition(RequirementFlowBuilder flowBuilder, STaskWait task) {
            super(flowBuilder, task);
        }

        @Nonnull
        public BuilderWaitTaskPetition withExecutionPage(@Nonnull Class<? extends WebPage> pageClass) {
            getTask().setExecutionPage(SingularRequirementTaskPageStrategy.of(pageClass));
            return self();
        }

    }

    /**
     * Builder (configurador) de {@link STaskEnd} especializado em requerimentos.
     * Apresenta comportamentos adicionais específicos de requerimentos.
     */
    public static class BuilderEndTaskPetition extends ImplBuilderTaskPetition<BuilderEndTaskPetition, STaskEnd>
            implements BuilderEnd<BuilderEndTaskPetition> {

        BuilderEndTaskPetition(RequirementFlowBuilder fluxoBuilder, STaskEnd task) {
            super(fluxoBuilder, task);
        }
    }

    /**
     * Builder (configurador) de {@link SStart} especializado em requerimentos.
     * Apresenta comportamentos adicionais específicos de requerimentos.
     */
    public static class BuilderStartPetition extends FlowBuilderImpl.ImplBuilderStart<BuilderStartPetition> {

        BuilderStartPetition(SStart start) {
            super(start);
        }
    }

    /**
     * Builder (configurador) de {@link STransition} especializado em requerimentos.
     * Apresenta comportamentos adicionais específicos de requerimentos.
     * <p>Trabalha com {@link STransitionPetition}.</p>
     */
    public static class BuilderTransitionPetition
            extends FlowBuilderImpl.ImplBuilderTransition<BuilderTransitionPetition> {

        @SuppressWarnings("rawtypes")
        BuilderTransitionPetition(FlowBuilder fluxoBuilder, STransition transicao) {
            super(fluxoBuilder, transicao);
        }

        @Override
        public STransitionPetition getTransition() {
            return (STransitionPetition) super.getTransition();
        }

    }

    /**
     * Builder (configurador) de {@link SBusinessRole} especializado em requerimentos.
     * Apresenta comportamentos adicionais específicos de requerimentos.
     */
    public static class BuilderRolePetition extends FlowBuilderImpl.ImplBuilderBusinessRole<BuilderRolePetition>
            implements BuilderBusinessRole<BuilderRolePetition> {

        BuilderRolePetition(SBusinessRole papel) {
            super(papel);
        }
    }

}
