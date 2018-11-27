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

package org.opensingular.requirement.connector.sei30.builder;

import org.opensingular.requirement.connector.sei30.model.AssuntoSei;
import org.opensingular.requirement.connector.sei30.model.NivelAcesso;
import org.opensingular.requirement.connector.sei30.model.TipoProcedimento;
import org.opensingular.requirement.connector.sei30.ws.ArrayOfAssunto;
import org.opensingular.requirement.connector.sei30.ws.ArrayOfInteressado;
import org.opensingular.requirement.connector.sei30.ws.Interessado;
import org.opensingular.requirement.connector.sei30.ws.Procedimento;

import java.util.Arrays;

/**
 * Classe ProcedimentoBuilder.
 */
public class ProcedimentoBuilder {

    private Procedimento procedimento;

    /**
     * Instancia um novo objeto procedimento builder.
     */
    public ProcedimentoBuilder() {
        this.procedimento = new Procedimento();
        this.procedimento.setAssuntos(new ArrayOfAssunto());
        this.procedimento.setInteressados(new ArrayOfInteressado());
        this.procedimento.setNivelAcesso(NivelAcesso.PUBLICO.getCodigo());

    }

    /**
     * Atualiza o novo valor de tipo procedimento.
     *
     * @param tipoProcedimento o(a) value.
     * @return o valor de procedimento builder
     */
    public ProcedimentoBuilder setTipoProcedimento(TipoProcedimento tipoProcedimento) {
        procedimento.setIdTipoProcedimento(tipoProcedimento != null ? tipoProcedimento.getId() : null);
        return this;
    }

    /**
     * Atualiza o novo valor de especificacao.
     *
     * @param value o(a) value.
     * @return o valor de procedimento builder
     */
    public ProcedimentoBuilder setEspecificacao(String value) {
        procedimento.setEspecificacao(value);
        return this;
    }

    /**
     * Atualiza o novo valor de assuntos.
     *
     * @param assuntos o(a) assuntos.
     * @return o valor de procedimento builder
     */
    public ProcedimentoBuilder setAssuntos(AssuntoSei... assuntos) {
        procedimento.getAssuntos().getItem().clear();
        if (assuntos != null) {
            for (AssuntoSei assuntoSei : Arrays.asList(assuntos)) {
                procedimento.getAssuntos().getItem().add(assuntoSei.toAssunto());
            }
        }
        return this;
    }

    /**
     * Atualiza o novo valor de interessados.
     *
     * @param interessados o(a) interessados.
     *                     <p> Os interessados são os nomes dos usuários,não é o login, é o nome.
     *                     Caso o interessado não tenha cadastro na base, será realizado um cadastro temporario para o nome do interessado.
     *                     </p>
     * @return o valor de procedimento builder
     */
    public ProcedimentoBuilder setInteressados(String... interessados) {
        procedimento.getInteressados().getItem().clear();
        if (interessados != null) {
            for (String nome : interessados) {
                Interessado e = new Interessado();
                e.setNome(nome);
                procedimento.getInteressados().getItem().add(e);

            }

        }
        return this;
    }

    /**
     * Adiciona um novo interessado.
     *
     * @param sigla a sigla que vai representar o interessado no sei.
     *                     <p> A sigla pode ser um login, CPF ou CNPJ de um interessado
     *                     </p>
     * @param nome o nome do interessado.
     * @return o valor de procedimento builder
     */
    public ProcedimentoBuilder addnteressado(String sigla, String nome) {
        Interessado e = new Interessado();
        e.setSigla(sigla);
        e.setNome(nome);
        procedimento.getInteressados().getItem().add(e);
        return this;
    }

    /**
     * Atualiza o novo valor de observacao.
     *
     * @param value o(a) value.
     * @return o valor de procedimento builder
     */
    public ProcedimentoBuilder setObservacao(String value) {
        procedimento.setObservacao(value);
        return this;
    }

    /**
     * Atualiza o novo valor de nivel acesso.
     *
     * @param nivelAcesso o(a) nivel acesso.
     * @return o valor de procedimento builder
     */
    public ProcedimentoBuilder setNivelAcesso(NivelAcesso nivelAcesso) {
        procedimento.setNivelAcesso(nivelAcesso != null ? nivelAcesso.getCodigo() : NivelAcesso.PUBLICO.getCodigo());
        return this;
    }

    /**
     * Cria o procedimento.
     *
     * @return o valor de procedimento
     */
    public Procedimento createProcedimento() {
        return procedimento;
    }
}
