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

package org.opensingular.requirement.module.flow.builder;

import org.apache.wicket.markup.html.WebPage;
import org.opensingular.flow.core.BusinessRoleStrategy;
import org.opensingular.flow.core.FlowInstance;
import org.opensingular.flow.core.ITaskDefinition;
import org.opensingular.flow.core.SBusinessRole;
import org.opensingular.flow.core.SStart;
import org.opensingular.flow.core.STask;
import org.opensingular.flow.core.STaskEnd;
import org.opensingular.flow.core.STaskHuman;
import org.opensingular.flow.core.STaskJava;
import org.opensingular.flow.core.STaskWait;
import org.opensingular.flow.core.STransition;
import org.opensingular.flow.core.builder.BuilderBusinessRole;
import org.opensingular.flow.core.builder.BuilderEnd;
import org.opensingular.flow.core.builder.BuilderHuman;
import org.opensingular.flow.core.builder.BuilderJava;
import org.opensingular.flow.core.builder.BuilderTask;
import org.opensingular.flow.core.builder.BuilderTransitionPredicate;
import org.opensingular.flow.core.builder.BuilderWait;
import org.opensingular.flow.core.builder.FlowBuilder;
import org.opensingular.flow.core.builder.FlowBuilderImpl;
import org.opensingular.lib.commons.base.SingularUtil;
import org.opensingular.requirement.module.flow.SingularRequirementTaskPageStrategy;

import javax.annotation.Nonnull;

/**
 * Construtor de fluxo especializado em fluxo para requerimentos. Apresenta configurações adicionais às encontradas
 * em {@link FlowBuilder}.
 *
 * @author Daniel C. Bordin on 23/03/2017.
 * @see FlowBuilder
 */
public class RequirementFlowBuilder extends
        FlowBuilder<RequirementFlowDefinition<?>, FlowMapRequirement, RequirementFlowBuilder.BuilderTaskRequirement, RequirementFlowBuilder.BuilderJavaTaskRequirement, RequirementFlowBuilder.BuilderHumanTaskRequirement, RequirementFlowBuilder.BuilderWaitTaskRequirement, RequirementFlowBuilder.BuilderEndTaskRequirement, RequirementFlowBuilder.BuilderStartRequirement, RequirementFlowBuilder.BuilderTransitionRequirement, RequirementFlowBuilder.BuilderTransitionRequirementPredicate, RequirementFlowBuilder.BuilderRoleRequirement, ITaskDefinition> {

    private RequirementFlowBuilder(RequirementFlowDefinition<?> flowDefinition) {
        super(flowDefinition);
    }

    /**
     * Cria um novo {@link RequirementFlowBuilder} para a definição de fluxo em questão.
     */
    public static RequirementFlowBuilder of(RequirementFlowDefinition<?> flowDefinition) {
        return new RequirementFlowBuilder(flowDefinition);
    }

    @Override
    protected BuilderTaskRequirement newTask(STask<?> task) {
        return new BuilderTaskRequirement(this, task);
    }

    @Override
    protected BuilderJavaTaskRequirement newJavaTask(STaskJava taskJava) {
        return new BuilderJavaTaskRequirement(this, taskJava);
    }

    @Override
    protected BuilderHumanTaskRequirement newHumanTask(STaskHuman taskHuman) {
        return new BuilderHumanTaskRequirement(this, (STaskHumanRequirement) taskHuman);
    }

    @Override
    protected BuilderWaitTaskRequirement newWaitTask(STaskWait taskWait) {
        return new BuilderWaitTaskRequirement(this, taskWait);
    }

    @Override
    protected BuilderEndTaskRequirement newEndTask(STaskEnd taskFim) {
        return new BuilderEndTaskRequirement(this, taskFim);
    }

    @Override
    protected BuilderStartRequirement newStartTask(SStart start) {
        return new BuilderStartRequirement(start);
    }

    @Override
    protected BuilderTransitionRequirement newTransition(STransition transition) {
        return new BuilderTransitionRequirement(this, transition);
    }

    @Override
    protected BuilderTransitionRequirementPredicate newAutomaticTransition(STransition transition) {
        return new BuilderTransitionRequirementPredicate(this, transition);
    }

    @Override
    protected BuilderRoleRequirement newBusinessRole(SBusinessRole businessRole) {
        return new BuilderRoleRequirement(businessRole);
    }

    @Override
    protected FlowMapRequirement newFlowMap(RequirementFlowDefinition<?> flowDefinition) {
        return new FlowMapRequirement(flowDefinition);
    }


    @Override
    public BuilderRoleRequirement addBusinessRole(String description,
                                                  BusinessRoleStrategy<? extends FlowInstance> businessRoleStrategy,
                                                  boolean automaticUserAllocation) {
        return addBusinessRole(description, SingularUtil.convertToJavaIdentity(description, true, false),
                businessRoleStrategy, automaticUserAllocation);
    }

    /**
     * Builder (configurador) de {@link STask} especializado em requerimentos. Apresenta comportamentos
     * adicionais específicos de requerimentos.
     */
    public abstract static class ImplBuilderTaskRequirement<SELF extends ImplBuilderTaskRequirement<SELF, TASK>, TASK
            extends STask<?>>
            extends FlowBuilderImpl.ImplBuilderTask<SELF, TASK> implements BuilderTask {

        ImplBuilderTaskRequirement(RequirementFlowBuilder fluxoBuilder, TASK task) {
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
        public BuilderTransitionRequirement go(String actionName, ITaskDefinition taskRefDestiny) {
            return (BuilderTransitionRequirement) super.go(actionName, taskRefDestiny);
        }

        /**
         * Cria uma nova transição da task atual para a task destino informada
         */
        @Override
        public BuilderTransitionRequirement go(ITaskDefinition taskRefDestiny) {
            return (BuilderTransitionRequirement) super.go(taskRefDestiny);
        }

    }

    /**
     * Builder (configurador) de {@link STask} especializado em requerimentos. Apresenta comportamentos
     * adicionais específicos de requerimentos.
     */
    public static class BuilderTaskRequirement extends ImplBuilderTaskRequirement<BuilderTaskRequirement, STask<?>> {

        BuilderTaskRequirement(RequirementFlowBuilder flowBuilder, STask<?> task) {
            super(flowBuilder, task);
        }
    }

    /**
     * Builder (configurador) de {@link STaskJava} especializado em requerimentos. Apresenta comportamentos
     * adicionais específicos de requerimentos.
     */
    public static class BuilderJavaTaskRequirement extends ImplBuilderTaskRequirement<BuilderJavaTaskRequirement, STaskJava>
            implements BuilderJava<BuilderJavaTaskRequirement> {

        BuilderJavaTaskRequirement(RequirementFlowBuilder flowBuilder, STaskJava task) {
            super(flowBuilder, task);
        }

    }

    /**
     * Builder (configurador) de {@link STaskHuman} especializado em requerimentos.
     * Apresenta comportamentos adicionais específicos de requerimentos.
     * <p>Trabalha com {@link STaskHumanRequirement}.</p>
     */
    public static class BuilderHumanTaskRequirement
            extends ImplBuilderTaskRequirement<BuilderHumanTaskRequirement, STaskHuman>
            implements BuilderHuman<BuilderHumanTaskRequirement> {

        BuilderHumanTaskRequirement(RequirementFlowBuilder flowBuilder, STaskHumanRequirement task) {
            super(flowBuilder, task);
        }

        @Nonnull
        public BuilderHumanTaskRequirement withExecutionPage(@Nonnull Class<? extends WebPage> pageClass) {
            getTask().setExecutionPage(SingularRequirementTaskPageStrategy.of(pageClass));
            return self();
        }

        @Override
        public STaskHumanRequirement getTask() {
            return (STaskHumanRequirement) super.getTask();
        }
    }

    /**
     * Builder (configurador) de {@link STaskWait} especializado em requerimentos.
     * Apresenta comportamentos adicionais específicos de requerimentos.
     */
    public static class BuilderWaitTaskRequirement extends ImplBuilderTaskRequirement<BuilderWaitTaskRequirement, STaskWait>
            implements BuilderWait<BuilderWaitTaskRequirement> {

        BuilderWaitTaskRequirement(RequirementFlowBuilder flowBuilder, STaskWait task) {
            super(flowBuilder, task);
        }

        @Nonnull
        public BuilderWaitTaskRequirement withExecutionPage(@Nonnull Class<? extends WebPage> pageClass) {
            getTask().setExecutionPage(SingularRequirementTaskPageStrategy.of(pageClass));
            return self();
        }

    }

    /**
     * Builder (configurador) de {@link STaskEnd} especializado em requerimentos.
     * Apresenta comportamentos adicionais específicos de requerimentos.
     */
    public static class BuilderEndTaskRequirement extends ImplBuilderTaskRequirement<BuilderEndTaskRequirement, STaskEnd>
            implements BuilderEnd<BuilderEndTaskRequirement> {

        BuilderEndTaskRequirement(RequirementFlowBuilder fluxoBuilder, STaskEnd task) {
            super(fluxoBuilder, task);
        }
    }

    /**
     * Builder (configurador) de {@link SStart} especializado em requerimentos.
     * Apresenta comportamentos adicionais específicos de requerimentos.
     */
    public static class BuilderStartRequirement extends FlowBuilderImpl.ImplBuilderStart<BuilderStartRequirement> {

        BuilderStartRequirement(SStart start) {
            super(start);
        }
    }

    /**
     * Builder (configurador) de {@link STransition} especializado em requerimentos.
     * Apresenta comportamentos adicionais específicos de requerimentos.
     * <p>Trabalha com {@link STransitionRequirement}.</p>
     */
    public static class BuilderTransitionRequirement
            extends FlowBuilderImpl.ImplBuilderTransition<BuilderTransitionRequirement> {

        @SuppressWarnings("rawtypes")
        BuilderTransitionRequirement(FlowBuilder fluxoBuilder, STransition transition) {
            super(fluxoBuilder, transition);
        }

        @Override
        public STransitionRequirement getTransition() {
            return (STransitionRequirement) super.getTransition();
        }

    }

    /**
     * Builder (configurador) de {@link STransition} especializado em requerimentos.
     * Apresenta comportamentos adicionais específicos de requerimentos.
     * <p>Trabalha com {@link STransitionRequirement}.</p>
     */
    public static class BuilderTransitionRequirementPredicate
            extends FlowBuilderImpl.ImplBuilderTransitionPredicate<BuilderTransitionRequirementPredicate> implements BuilderTransitionPredicate<BuilderTransitionRequirementPredicate> {

        @SuppressWarnings("rawtypes")
        BuilderTransitionRequirementPredicate(FlowBuilder fluxoBuilder, STransition transition) {
            super(fluxoBuilder, transition);
        }

        @Override
        public STransitionRequirement getTransition() {
            return (STransitionRequirement) super.getTransition();
        }


    }

    /**
     * Builder (configurador) de {@link SBusinessRole} especializado em requerimentos.
     * Apresenta comportamentos adicionais específicos de requerimentos.
     */
    public static class BuilderRoleRequirement extends FlowBuilderImpl.ImplBuilderBusinessRole<BuilderRoleRequirement>
            implements BuilderBusinessRole<BuilderRoleRequirement> {

        BuilderRoleRequirement(SBusinessRole role) {
            super(role);
        }
    }

}
