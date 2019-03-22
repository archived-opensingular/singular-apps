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

package org.opensingular.requirement.connector.sei31;

import org.opensingular.requirement.connector.sei31.model.SimNao;
import org.opensingular.requirement.connector.sei31.model.TipoBlocoEnum;
import org.opensingular.requirement.connector.sei31.model.UnidadeSei;
import org.opensingular.requirement.connector.sei31.ws.ArquivoExtensao;
import org.opensingular.requirement.connector.sei31.ws.ArrayOfDocumento;
import org.opensingular.requirement.connector.sei31.ws.ArrayOfDocumentoFormatado;
import org.opensingular.requirement.connector.sei31.ws.ArrayOfIdUnidade;
import org.opensingular.requirement.connector.sei31.ws.ArrayOfProcedimentoRelacionado;
import org.opensingular.requirement.connector.sei31.ws.Cargo;
import org.opensingular.requirement.connector.sei31.ws.Contato;
import org.opensingular.requirement.connector.sei31.ws.Documento;
import org.opensingular.requirement.connector.sei31.ws.Procedimento;
import org.opensingular.requirement.connector.sei31.ws.RetornoConsultaBloco;
import org.opensingular.requirement.connector.sei31.ws.RetornoConsultaDocumento;
import org.opensingular.requirement.connector.sei31.ws.RetornoConsultaProcedimento;
import org.opensingular.requirement.connector.sei31.ws.RetornoGeracaoProcedimento;
import org.opensingular.requirement.connector.sei31.ws.RetornoInclusaoDocumento;
import org.opensingular.requirement.connector.sei31.ws.Serie;
import org.opensingular.requirement.connector.sei31.ws.TipoProcedimento;
import org.opensingular.requirement.connector.sei31.ws.Unidade;
import org.opensingular.requirement.connector.sei31.ws.Usuario;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Classe SEIWSPortType
 */
public interface SEIPortType {
    /**
     * Método que simplifica a criação de um procedimento (processo)
     * de forma que todos os demais valores tenham um padrão.
     *
     * @param procedimento procedimento a ser inserido
     * @return o retorno da geração do procedimento
     */
    RetornoGeracaoProcedimento gerarProcedimento(UnidadeSei unidade, Procedimento procedimento);

    /**
     * Gerar procedimento.
     *
     * @param procedimento              o(a) procedimento.
     * @param documentos                o(a) documentos.
     * @param procedimentosRelacionados o(a) procedimentos relacionados.
     * @param unidadesEnvio             o(a) unidades envio.
     * @param sinManterAbertoUnidade    o(a) sin manter aberto unidade.
     * @param sinEnviarEmailNotificacao o(a) sin enviar email notificacao.
     * @param dataRetornoProgramado     o(a) data retorno programado.
     * @param idMarcador                o id do marcador
     * @param textoMarcador             o texto do marcador
     * @return o valor de retorno geracao procedimento
     */
    RetornoGeracaoProcedimento gerarProcedimento(UnidadeSei unidade, Procedimento procedimento, ArrayOfDocumento documentos,
                                                 ArrayOfProcedimentoRelacionado procedimentosRelacionados,
                                                 ArrayOfIdUnidade unidadesEnvio, SimNao sinManterAbertoUnidade,
                                                 SimNao sinEnviarEmailNotificacao, String dataRetornoProgramado,
                                                 String idMarcador, String textoMarcador);

    /**
     * Reabrir processo.
     *
     * @param protocoloProcedimento o(a) protocolo procedimento.
     * @return o valor de boolean
     */
    Boolean reabrirProcesso(UnidadeSei unidade, String protocoloProcedimento);

    /**
     * Listar usuarios.
     *
     * @param idUsuario o(a) id usuario.
     * @return o valor de array of usuario
     */
    List<Usuario> listarUsuarios(UnidadeSei unidade, String idUsuario);

    /**
     * Faz a pesquisa de procedimento (processo) retornando apenas os dados
     * básicos. Para uma pesquisa mais abrangente utilizar
     * {@link #consultarProcedimento(UnidadeSei, String, SimNao, SimNao, SimNao, SimNao, SimNao, SimNao, SimNao, SimNao, SimNao)}
     *
     * @param protocoloProcedimento o(UnidadeSei unidade, a) protocolo procedimento.
     * @return o valor de retorno consulta procedimento
     */
    RetornoConsultaProcedimento consultarProcedimentoBasico(UnidadeSei unidade, String protocoloProcedimento);


    /**
     * Consultar procedimento.
     *
     * @param protocoloProcedimento                 o(a) protocolo procedimento.
     * @param sinRetornarAssuntos                   o(a) sin retornar assuntos.
     * @param sinRetornarInteressados               o(a) sin retornar interessados.
     * @param sinRetornarObservacoes                o(a) sin retornar observacoes.
     * @param sinRetornarAndamentoGeracao           o(a) sin retornar andamento geracao.
     * @param sinRetornarAndamentoConclusao         o(a) sin retornar andamento conclusao.
     * @param sinRetornarUltimoAndamento            o(a) sin retornar ultimo andamento.
     * @param sinRetornarUnidadesProcedimentoAberto o(a) sin retornar unidades procedimento aberto.
     * @param sinRetornarProcedimentosRelacionados  o(a) sin retornar procedimentos relacionados.
     * @param sinRetornarProcedimentosAnexados      o(a) sin retornar procedimentos anexados.
     * @return o valor de retorno consulta procedimento
     */
    RetornoConsultaProcedimento consultarProcedimento(UnidadeSei unidade, String protocoloProcedimento, SimNao sinRetornarAssuntos,
                                                      SimNao sinRetornarInteressados, SimNao sinRetornarObservacoes,
                                                      SimNao sinRetornarAndamentoGeracao, SimNao sinRetornarAndamentoConclusao,
                                                      SimNao sinRetornarUltimoAndamento, SimNao sinRetornarUnidadesProcedimentoAberto,
                                                      SimNao sinRetornarProcedimentosRelacionados, SimNao sinRetornarProcedimentosAnexados);


    /**
     * Consultar procedimento.
     *
     * @param protocoloProcedimento
     *            o(a) protocolo procedimento.
     * @param sinRetornarAssuntos
     *            o(a) sin retornar assuntos.
     * @param sinRetornarInteressados
     *            o(a) sin retornar interessados.
     * @param sinRetornarObservacoes
     *            o(a) sin retornar observacoes.
     * @param sinRetornarAndamentoGeracao
     *            o(a) sin retornar andamento geracao.
     * @param sinRetornarAndamentoConclusao
     *            o(a) sin retornar andamento conclusao.
     * @param sinRetornarUltimoAndamento
     *            o(a) sin retornar ultimo andamento.
     * @param sinRetornarUnidadesProcedimentoAberto
     *            o(a) sin retornar unidades procedimento aberto.
     * @param sinRetornarProcedimentosRelacionados
     *            o(a) sin retornar procedimentos relacionados.
     * @param sinRetornarProcedimentosAnexados
     *            o(a) sin retornar procedimentos anexados.
     * @return o valor de retorno consulta procedimento
     */
    RetornoConsultaProcedimento consultarProcedimento(String protocoloProcedimento, SimNao sinRetornarAssuntos,
                                                             SimNao sinRetornarInteressados, SimNao sinRetornarObservacoes,
                                                             SimNao sinRetornarAndamentoGeracao, SimNao sinRetornarAndamentoConclusao,
                                                             SimNao sinRetornarUltimoAndamento, SimNao sinRetornarUnidadesProcedimentoAberto,
                                                             SimNao sinRetornarProcedimentosRelacionados, SimNao sinRetornarProcedimentosAnexados);

    /**
     * Atribuir processo.
     *
     * @param protocoloProcedimento o(a) protocolo procedimento.
     * @param idUsuario             o(a) id usuario.
     * @param sinReabrir            o(a) sin reabrir.
     * @return o valor de boolean
     */
    Boolean atribuirProcesso(UnidadeSei unidade, String protocoloProcedimento, String idUsuario, SimNao sinReabrir);

    /**
     * Incluir documento bloco.
     *
     * @param idBloco            o(a) id bloco.
     * @param protocoloDocumento o(a) protocolo documento.
     * @return o valor de boolean
     */
    Boolean incluirDocumentoBloco(UnidadeSei unidade, String idBloco, String protocoloDocumento);

    /**
     * Concluir processo.
     *
     * @param protocoloProcedimento o(a) protocolo procedimento.
     * @return o valor de boolean
     */
    Boolean concluirProcesso(UnidadeSei unidade, String protocoloProcedimento);

    /**
     * Cancelar disponibilizacao bloco.
     *
     * @param idBloco o(a) id bloco.
     * @return o valor de boolean
     */
    Boolean cancelarDisponibilizacaoBloco(UnidadeSei unidade, String idBloco);

    /**
     * Listar unidades.
     *
     * @param idTipoProcedimento o(a) id tipo procedimento.
     * @param idSerie            o(a) id serie.
     * @return o valor de array of unidade
     */
    List<Unidade> listarUnidades(String idTipoProcedimento, String idSerie);

    /**
     * Listar unidades.
     *
     * @return o valor de array of unidade
     */
    List<Unidade> listarUnidades();

    /**
     * Listar series.
     *
     * @param idTipoProcedimento o(a) id tipo procedimento.
     * @return o valor de array of serie
     */
    List<Serie> listarSeries(UnidadeSei unidade, String idTipoProcedimento);

    /**
     * Listar series.
     *
     * @return o valor de array of serie
     */
    List<Serie> listarSeries();

    /**
     * Excluir bloco.
     *
     * @param idBloco o(a) id bloco.
     * @return o valor de boolean
     */
    Boolean excluirBloco(UnidadeSei unidade, String idBloco);

    /**
     * Disponibilizar bloco.
     *
     * @param idBloco o(a) id bloco.
     * @return o valor de boolean
     */
    Boolean disponibilizarBloco(UnidadeSei unidade, String idBloco);

    /**
     * Incluir processo bloco.
     *
     * @param idBloco               o(a) id bloco.
     * @param protocoloProcedimento o(a) protocolo procedimento.
     * @return o valor de boolean
     */
    Boolean incluirProcessoBloco(UnidadeSei unidade, String idBloco, String protocoloProcedimento);

    /**
     * Incluir documento.
     *
     * @param documento o(a) documento.
     * @return o valor de retorno inclusao documento
     */
    RetornoInclusaoDocumento incluirDocumento(UnidadeSei unidade, Documento documento);

    String adicionarArquivo(UnidadeSei unidade, String nome, String tamanho, String hash, String conteudo);

    /**
     * Gerar bloco.
     *
     * @param tipoBlocoEnum            o(a) tipo bloco enum.
     * @param descricao                o(a) descricao.
     * @param unidadesDisponibilizacao o(a) unidades disponibilizacao.
     * @param documentos               o(a) documentos.
     * @param sinDisponibilizar        o(a) sin disponibilizar.
     * @return o valor de string
     */
    String gerarBloco(UnidadeSei unidade, TipoBlocoEnum tipoBlocoEnum, String descricao, ArrayOfIdUnidade unidadesDisponibilizacao,
                      ArrayOfDocumentoFormatado documentos, SimNao sinDisponibilizar);

    /**
     * Gerar bloco, utiliza dados padrão nos campos opcionais.
     * Não faz a disponibilização de bloco.
     *
     * @param tipoBlocoEnum o(a) tipo bloco enum.
     * @param descricao     o(a) descricao.
     * @return o valor de string
     */
    String gerarBloco(UnidadeSei unidade, TipoBlocoEnum tipoBlocoEnum, String descricao);

    /**
     * Consultar documento, utilize os atributos adicionais
     * para realizar uma consulta com mais informações a respeito
     * do documento.
     *
     * @param protocoloDocumento          o(a) protocolo documento {@link RetornoInclusaoDocumento#getDocumentoFormatado()}.
     * @param sinRetornarAndamentoGeracao o(a) sin retornar andamento geracao.
     * @param sinRetornarAssinaturas      o(a) sin retornar assinaturas.
     * @param sinRetornarPublicacao       o(a) sin retornar publicacao.
     * @param sinRetornarCampos
     * @return o valor de retorno consulta documento
     */
    RetornoConsultaDocumento consultarDocumento(UnidadeSei unidade, String protocoloDocumento, SimNao sinRetornarAndamentoGeracao,
                                                SimNao sinRetornarAssinaturas, SimNao sinRetornarPublicacao, SimNao sinRetornarCampos);

    /**
     * Consultar documento da forma mais simples, caso seja necessária
     * uma consulta mais completa utilizar {@link #consultarDocumento(UnidadeSei, String, SimNao, SimNao, SimNao, SimNao)} .
     *
     * @param protocoloDocumento o(a) protocolo documento.
     * @return o valor de retorno consulta documento
     */
    RetornoConsultaDocumento consultarDocumento(UnidadeSei unidade, String protocoloDocumento);

    /**
     * Consultar documento retornando dados de publicacao.
     *
     * @param protocoloDocumento o(a) protocolo documento
     * @return o(a) retorno consulta documento
     */
    RetornoConsultaDocumento consultarDocumentoPublicacao(UnidadeSei unidade, String protocoloDocumento);

    /**
     * Consultar documento retornando dados de assinatura.
     *
     * @param protocoloDocumento o(a) protocolo documento
     * @return o(a) retorno consulta documento
     */
    RetornoConsultaDocumento consultarDocumentoAssinatura(UnidadeSei unidade, String protocoloDocumento);

    /**
     * Enviar processo.
     *
     * @param protocoloProcedimento     o(a) protocolo procedimento.
     * @param unidadesDestino           o(a) unidades destino.
     * @param sinManterAbertoUnidade    o(a) sin manter aberto unidade.
     * @param sinRemoverAnotacao        o(a) sin remover anotacao.
     * @param sinEnviarEmailNotificacao o(a) sin enviar email notificacao.
     * @param dataRetornoProgramado     o(a) data retorno programado.
     * @return o valor de boolean
     */
    Boolean enviarProcesso(UnidadeSei unidade, String protocoloProcedimento, ArrayOfIdUnidade unidadesDestino,
                           SimNao sinManterAbertoUnidade, SimNao sinRemoverAnotacao, SimNao sinEnviarEmailNotificacao,
                           String dataRetornoProgramado, String diasRetornoProgramado);

    /**
     * Retirar documento bloco.
     *
     * @param idBloco            o(a) id bloco.
     * @param protocoloDocumento o(a) protocolo documento.
     * @return o valor de boolean
     */
    Boolean retirarDocumentoBloco(UnidadeSei unidade, String idBloco, String protocoloDocumento);

    /**
     * Retirar processo bloco.
     *
     * @param idBloco               o(a) id bloco.
     * @param protocoloProcedimento o(a) protocolo procedimento.
     * @return o valor de boolean
     */
    Boolean retirarProcessoBloco(UnidadeSei unidade, String idBloco, String protocoloProcedimento);

    /**
     * Listar extensoes permitidas.
     *
     * @param idArquivoExtensao o(a) id arquivo extensao.
     * @return o valor de array of arquivo extensao
     */
    List<ArquivoExtensao> listarExtensoesPermitidas(UnidadeSei unidade, String idArquivoExtensao);

    /**
     * Listar tipos procedimento.
     *
     * @param idSerie o(a) id serie.
     * @return o valor de array of tipo procedimento
     */
    List<TipoProcedimento> listarTiposProcedimento(UnidadeSei unidade, String idSerie);

    /**
     * Listar tipos procedimento.
     *
     * @return o valor de array of tipo procedimento
     */
    List<TipoProcedimento> listarTiposProcedimento();



    /**
     * Consultar bloco
     *
     * @param idBloco - o(a) id do bloco
     * @return o retorno da consulta de bloco
     */
    public RetornoConsultaBloco consultarBloco(UnidadeSei unidade, String idBloco);

    /**
     * Cancelamento de documentos
     *
     * @param motivo             - o(a) motivo
     * @param protocoloDocumento - o(a) protocolo do documento
     * @return - O retorno do cancelamento do documento
     */
    public String cancelarDocumento(UnidadeSei unidade, String protocoloDocumento, String motivo);


    /**
     * Lista de contatos
     *
     * @param sigla
     * @param idTipoContato
     * @param unidade
     * @return returns ArrayOfContato
     */
    public List<Contato> listarContatos(
            UnidadeSei unidade,
            String idTipoContato,
            String sigla);


    /**
     * Lista de contatos
     *
     * @param cpf
     * @param idTipoContato
     * @param unidade
     * @return returns Contato with CPF
     */
    public Contato findContatoByCPF(@Nullable UnidadeSei unidade, String idTipoContato, String cpf);

    /**
     * Lista de contatos
     *
     */
    List<Cargo> listarCargos();

    /**
     * Lista contatos com base no cpf.
     *
     * @param idTipoContato
     * @param cpf
     * @return
     */
    List<Contato> listarContatosPorCPF(String idTipoContato, String cpf);
}
