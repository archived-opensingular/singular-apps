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

import org.opensingular.flow.core.ITaskDefinition;
import org.opensingular.flow.core.ProcessInstance;
import org.opensingular.flow.core.SProcessRole;
import org.opensingular.flow.core.SStart;
import org.opensingular.flow.core.STask;
import org.opensingular.flow.core.STaskEnd;
import org.opensingular.flow.core.STaskJava;
import org.opensingular.flow.core.STaskHuman;
import org.opensingular.flow.core.STaskWait;
import org.opensingular.flow.core.STransition;
import org.opensingular.flow.core.UserRoleSettingStrategy;
import org.opensingular.flow.core.builder.BuilderEnd;
import org.opensingular.flow.core.builder.BuilderJava;
import org.opensingular.flow.core.builder.BuilderHuman;
import org.opensingular.flow.core.builder.BuilderProcessRole;
import org.opensingular.flow.core.builder.BuilderTask;
import org.opensingular.flow.core.builder.BuilderWait;
import org.opensingular.flow.core.builder.FlowBuilder;
import org.opensingular.flow.core.builder.FlowBuilderImpl;
import org.opensingular.lib.commons.base.SingularUtil;

/**
 * Construtor de fluxo especializado em fluxo para requerimentos. Apresenta configurações adicionais às encontradas
 * em {@link FlowBuilder}.
 *
 * @author Daniel C. Bordin on 23/03/2017.
 * @see FlowBuilder
 */
public class FlowBuilderPetition extends
        FlowBuilder<PetitionProcessDefinition<?>, FlowMapPetition, FlowBuilderPetition.BuilderTaskPetition,
                FlowBuilderPetition.BuilderJavaTaskPetition, FlowBuilderPetition.BuilderHumanTaskPetition,
                FlowBuilderPetition.BuilderWaitTaskPetition, FlowBuilderPetition.BuilderEndTaskPetition,
                FlowBuilderPetition.BuilderStartPetition, FlowBuilderPetition.BuilderTransitionPetition,
                FlowBuilderPetition.BuilderRolePetition, ITaskDefinition> {

    private FlowBuilderPetition(PetitionProcessDefinition<?> processDefinition) {
        super(processDefinition);
    }

    /** Cria um novo FlowBuilderPetition para a definição de processo em questão. */
    public static FlowBuilderPetition of(PetitionProcessDefinition<?> processDefinition) {
        return new FlowBuilderPetition(processDefinition);
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
    protected BuilderStartPetition newStart(SStart start) {
        return new BuilderStartPetition(start);
    }

    @Override
    protected BuilderTransitionPetition newTransition(STransition transicao) {
        return new BuilderTransitionPetition(this, transicao);
    }

    @Override
    protected BuilderRolePetition newProcessRole(SProcessRole papel) {
        return new BuilderRolePetition(papel);
    }

    @Override
    protected FlowMapPetition newFlowMap(PetitionProcessDefinition<?> definicaoProcesso) {
        return new FlowMapPetition(definicaoProcesso);
    }


    @Override
    public BuilderRolePetition addRoleDefinition(String description,
            UserRoleSettingStrategy<? extends ProcessInstance> userRoleSettingStrategy,
            boolean automaticUserAllocation) {
        return addRoleDefinition(description, SingularUtil.convertToJavaIdentity(description, true, false),
                userRoleSettingStrategy, automaticUserAllocation);
    }

    //    public BuilderHumanTaskPetition addHumanTask(ITaskDefinition definition, DireitoProjeto direitoNecessario) {
    //        return addHumanTask(definition, EstrategiaAcessoTaskSimples.of(direitoNecessario));
    //    }

    /**
     * Builder (configurador) de {@link STask} especializado em requerimentos. Apresenta comportamentos
     * adicionais específicos de requerimentos.
     */
    public abstract static class ImplBuilderTaskPetition<SELF extends ImplBuilderTaskPetition<SELF, TASK>, TASK
            extends STask<?>>
            extends FlowBuilderImpl.ImplBuilderTask<SELF, TASK> implements BuilderTask {

        ImplBuilderTaskPetition(FlowBuilderPetition fluxoBuilder, TASK task) {
            super(fluxoBuilder, task);
        }

        @Override
        protected FlowBuilderPetition getFlowBuilder() {
            return (FlowBuilderPetition) super.getFlowBuilder();
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

        BuilderTaskPetition(FlowBuilderPetition flowBuilder, STask<?> task) {
            super(flowBuilder, task);
        }
    }

    /**
     * Builder (configurador) de {@link STaskJava} especializado em requerimentos. Apresenta comportamentos
     * adicionais específicos de requerimentos.
     */
    public static class BuilderJavaTaskPetition extends ImplBuilderTaskPetition<BuilderJavaTaskPetition, STaskJava>
            implements BuilderJava<BuilderJavaTaskPetition> {

        BuilderJavaTaskPetition(FlowBuilderPetition flowBuilder, STaskJava task) {
            super(flowBuilder, task);
        }

        //        public <T extends ProcessInstance> BuilderJavaTaskPetition callByBlocoSemanalSegunda
        // (ImplTaskBlock<T> implBloco,
        //                int hora, int minuto) {
        //            return batchCall(implBloco, ScheduleDataBuilder.buildWeekly(hora, minuto, 1));
        //        }
        //
        //        public <T extends ProcessInstance> BuilderJavaTaskPetition callByBlocoSemanalTerca(ImplTaskBlock<T>
        // implBloco,
        //                int hora, int minuto) {
        //            return batchCall(implBloco, ScheduleDataBuilder.buildWeekly(hora, minuto, 2));
        //        }
        //
        //        public <T extends ProcessInstance> BuilderJavaTaskPetition callByBlocoSemanalQuarta
        // (ImplTaskBlock<T> implBloco,
        //                int hora, int minuto) {
        //            return batchCall(implBloco, ScheduleDataBuilder.buildWeekly(hora, minuto, 3));
        //        }
        //
        //        public <T extends ProcessInstance> BuilderJavaTaskPetition callByBlocoSemanalQuinta
        // (ImplTaskBlock<T> implBloco,
        //                int hora, int minuto) {
        //            return batchCall(implBloco, ScheduleDataBuilder.buildWeekly(hora, minuto, 4));
        //        }
        //
        //        public <T extends ProcessInstance> BuilderJavaTaskPetition callByBlocoSemanalSexta(ImplTaskBlock<T>
        // implBloco,
        //                int hora, int minuto) {
        //            return batchCall(implBloco, ScheduleDataBuilder.buildWeekly(hora, minuto, 5));
        //        }
        //
        //        public <T extends ProcessInstance> BuilderJavaTaskPetition callByBlocoSemanalSabado
        // (ImplTaskBlock<T> implBloco,
        //                int hora, int minuto) {
        //            return batchCall(implBloco, ScheduleDataBuilder.buildWeekly(hora, minuto, 6));
        //        }
        //
        //        public <T extends ProcessInstance> BuilderJavaTaskPetition callByBlocoSemanalDomingo
        // (ImplTaskBlock<T> implBloco,
        //                int hora, int minuto) {
        //            return batchCall(implBloco, ScheduleDataBuilder.buildWeekly(hora, minuto, 0));
        //        }
        //
        //        public <T extends ProcessInstance> BuilderJavaTaskPetition callByBlocoMensal(ImplTaskBlock<T>
        // implBloco,
        //                int diaMes, int hora, int minuto) {
        //            return batchCall(implBloco, ScheduleDataBuilder.buildMonthly(diaMes, hora, minuto));
        //        }
        //
        //        public <T extends ProcessInstance> BuilderJavaTaskPetition callByBlocoDiario(ImplTaskBlock<T>
        // implBloco,
        //                int hora, int minuto) {
        //            return batchCall(implBloco, ScheduleDataBuilder.buildDaily(hora, minuto));
        //        }
    }

    /**
     * Builder (configurador) de {@link STaskHuman} especializado em requerimentos.
     * Apresenta comportamentos adicionais específicos de requerimentos.
     * <p>Trabalha com {@link STaskHumanPetition}.</p>
     */
    public static class BuilderHumanTaskPetition
            extends ImplBuilderTaskPetition<BuilderHumanTaskPetition, STaskHuman>
            implements BuilderHuman<BuilderHumanTaskPetition> {

        BuilderHumanTaskPetition(FlowBuilderPetition flowBuilder, STaskHumanPetition task) {
            super(flowBuilder, task);
        }

        //        public BuilderHumanTaskPetition mapearClasseTela(Class<?> classeTela) {
        //            return withExecutionPage(TelaExecucao.of(classeTela));
        //        }

        //        public BuilderHumanTaskPetition mapearTela(Class<? extends WebActionInstanciaMBPM> classeTela) {
        //            return mapearClasseTela(classeTela);
        //        }

        //        public BuilderHumanTaskPetition mapearView(Class<? extends RelatorioAlocpro> classeTela) {
        //            return mapearClasseTela(classeTela);
        //        }

        //        public BuilderHumanTaskPetition mapearTelaModeloFormularioEditar() {
        //            return mapearClasseTela(ExecucaoDemandaFormularioEditarPage.class);
        //        }

        //        public BuilderHumanTaskPetition mapearTelaVisualizarSolicitacao() {
        //            return mapearClasseTela(ExecucaoDemandaVisualizarPage.class);
        //        }

        //        public BuilderHumanTaskPetition comPrazoDiasUteis(final int qtdDias) {
        //            return withTargetDate((processInstance, taskInstance) -> DiaSemanaUtil
        //                    .somarDiasUteis(taskInstance.getBeginDate(), qtdDias));
        //        }

        //        public BuilderHumanTaskPetition comAlerta(EstrategiaAlertaTarefa estrategiaAlerta) {
        //            getTask().setAlerta(estrategiaAlerta);
        //            return self();
        //        }

        //        public BuilderHumanTaskPetition setApareceNoPainelAtividades(Boolean valor) {
        //            getTask().setMetaDataValue(PetitionProcessDefinition.PROP_EXIBIR_PAINEL_ATVS, valor);
        //            return self();
        //        }

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

        BuilderWaitTaskPetition(FlowBuilderPetition flowBuilder, STaskWait task) {
            super(flowBuilder, task);
        }

        //        public BuilderWaitTaskPetition mapearClasseTela(Class<?> classeTela) {
        //            return withExecutionPage(TelaExecucao.of(classeTela));
        //        }

        //        public BuilderWaitTaskPetition mapearTela(Class<? extends WebActionInstanciaMBPM> classeTela) {
        //            return mapearClasseTela(classeTela);
        //        }

        //        public BuilderWaitTaskPetition mapearView(Class<? extends RelatorioAlocpro> classeTela) {
        //            return mapearClasseTela(classeTela);
        //        }

        //        public BuilderWaitTaskPetition mapearTelaVariaveisDefinicao() {
        //            return mapearClasseTela(ExecucaoDemandaVariaveisDefinicaoPage.class);
        //        }

        //        public BuilderWaitTaskPetition mapearTelaVisualizarSolicitacao() {
        //            return mapearClasseTela(ExecucaoDemandaVisualizarPage.class);
        //        }
    }

    /**
     * Builder (configurador) de {@link STaskEnd} especializado em requerimentos.
     * Apresenta comportamentos adicionais específicos de requerimentos.
     */
    public static class BuilderEndTaskPetition extends ImplBuilderTaskPetition<BuilderEndTaskPetition, STaskEnd>
            implements BuilderEnd<BuilderEndTaskPetition> {

        BuilderEndTaskPetition(FlowBuilderPetition fluxoBuilder, STaskEnd task) {
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

        //        public BuilderTransitionPetition comMensagemConfirmacao(String mensagem) {
        //            return setMetaDataValue(PetitionProcessDefinition.PROP_MSG_CONFIRMACAO_TRANSICAO, mensagem);
        //        }

        //        public BuilderTransitionPetition setFormularioTransicao(
        //                Class<? extends IFormularioTransicao<?>> formularioTransicao) {
        //            getTransition().setFormularioTransicao(formularioTransicao);
        //            return this;
        //        }

        //        public BuilderTransitionPetition showInExecution() {
        //            getTransition().setAccessControl(TransitionAccessStrategyImpl.enabled(true));
        //            return self();
        //        }
    }

    /**
     * Builder (configurador) de {@link SProcessRole} especializado em requerimentos.
     * Apresenta comportamentos adicionais específicos de requerimentos.
     */
    public static class BuilderRolePetition extends FlowBuilderImpl.ImplBuilderProcessRole<BuilderRolePetition>
            implements BuilderProcessRole<BuilderRolePetition> {

        BuilderRolePetition(SProcessRole papel) {
            super(papel);
        }
    }

}
