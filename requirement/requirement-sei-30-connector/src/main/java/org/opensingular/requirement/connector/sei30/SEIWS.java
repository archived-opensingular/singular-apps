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

package org.opensingular.requirement.connector.sei30;

import org.apache.commons.collections.CollectionUtils;
import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.lib.commons.util.WSClientSafeWrapper;
import org.opensingular.requirement.connector.sei30.model.SimNao;
import org.opensingular.requirement.connector.sei30.model.TipoBlocoEnum;
import org.opensingular.requirement.connector.sei30.model.UnidadeSei;
import org.opensingular.requirement.connector.sei30.ws.ArquivoExtensao;
import org.opensingular.requirement.connector.sei30.ws.ArrayOfArquivoExtensao;
import org.opensingular.requirement.connector.sei30.ws.ArrayOfDocumento;
import org.opensingular.requirement.connector.sei30.ws.ArrayOfDocumentoFormatado;
import org.opensingular.requirement.connector.sei30.ws.ArrayOfIdUnidade;
import org.opensingular.requirement.connector.sei30.ws.ArrayOfProcedimentoRelacionado;
import org.opensingular.requirement.connector.sei30.ws.ArrayOfSerie;
import org.opensingular.requirement.connector.sei30.ws.ArrayOfString;
import org.opensingular.requirement.connector.sei30.ws.ArrayOfTipoProcedimento;
import org.opensingular.requirement.connector.sei30.ws.ArrayOfUsuario;
import org.opensingular.requirement.connector.sei30.ws.Contato;
import org.opensingular.requirement.connector.sei30.ws.Documento;
import org.opensingular.requirement.connector.sei30.ws.Procedimento;
import org.opensingular.requirement.connector.sei30.ws.RetornoConsultaBloco;
import org.opensingular.requirement.connector.sei30.ws.RetornoConsultaDocumento;
import org.opensingular.requirement.connector.sei30.ws.RetornoConsultaProcedimento;
import org.opensingular.requirement.connector.sei30.ws.RetornoGeracaoProcedimento;
import org.opensingular.requirement.connector.sei30.ws.RetornoInclusaoDocumento;
import org.opensingular.requirement.connector.sei30.ws.SeiPortType;
import org.opensingular.requirement.connector.sei30.ws.SeiService;
import org.opensingular.requirement.connector.sei30.ws.Serie;
import org.opensingular.requirement.connector.sei30.ws.TipoProcedimento;
import org.opensingular.requirement.connector.sei30.ws.Unidade;
import org.opensingular.requirement.connector.sei30.ws.Usuario;

import javax.annotation.Nullable;
import javax.xml.ws.BindingProvider;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


/**
 * Classe SEIWS.
 * O objetivo desta classe é enriquecer a API para chamadas
 * ao SEI, visto que esta classe não é gerada, podem ser criados
 * diversos métodos para facilitar o desenvolvimento.
 */
public class SEIWS implements SEIPortType, Loggable {

    private final SeiPortType seiPortType;
    private final String siglaSistema;
    private final String identificacaoServico;

    /**
     * Instancia um novo objeto SEIWS delegate.
     *
     * @param siglaSistema         o(a) sigla sistema.
     * @param identificacaoServico o(a) identificacao servico.
     * @param wsAddress            o(a) ws address.
     */
    public SEIWS(String siglaSistema, String identificacaoServico, String wsAddress) {
        this.seiPortType = getSeiService(wsAddress);
        this.siglaSistema = siglaSistema;
        this.identificacaoServico = identificacaoServico;
    }

    private SeiPortType getSeiService(String wsAddress) {
        SeiPortType seiServicePortType = new SeiService().getSeiPortService();
        BindingProvider bp = (BindingProvider) seiServicePortType;
        bp.getRequestContext().put(
                BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
                WSClientSafeWrapper.getAdressWithoutWsdl(
                        wsAddress
                ));
        return seiServicePortType;
    }

    /**
     * Método que simplifica a criação de um procedimento (processo)
     * de forma que todos os demais valores tenham um padrão.
     *
     * @param procedimento procedimento a ser inserido
     * @return o retorno da geração do procedimento
     */
    @Override
    public RetornoGeracaoProcedimento gerarProcedimento(UnidadeSei unidade, Procedimento procedimento) {
        return gerarProcedimento(unidade, procedimento, ConstantesSEI.DOCUMENTOS_EMPTY, ConstantesSEI.PROCEDIMENTO_RELACIONADOS_EMPTY, ConstantesSEI.ID_UNIDADE_EMPTY, SimNao.NAO, SimNao.NAO, "", "", "");
    }

    /**
     * Gerar procedimento.
     *
     * @param procedimento              o(a) procedimento.
     * @param documentos                o(a) documentos.
     * @param procedimentosRelacionados o(a) procedimentos relacionados.
     * @param unidadesEnvio             o(a) unidades envio.
     * @param sinManterAbertoUnidade    o(a) sinalizador para manter aberto unidade.
     * @param sinEnviarEmailNotificacao o(a) sinalizador para enviar email notificacao.
     * @param dataRetornoProgramado     o(a) data retorno programado.
     * @param idMarcador                o id do marcador
     * @param textoMarcador             o texto do marcador
     * @return o valor de retorno geracao procedimento
     */
    @Override
    public RetornoGeracaoProcedimento gerarProcedimento(UnidadeSei unidade, Procedimento procedimento, ArrayOfDocumento documentos,
                                                        ArrayOfProcedimentoRelacionado procedimentosRelacionados,
                                                        ArrayOfIdUnidade unidadesEnvio, SimNao sinManterAbertoUnidade,
                                                        SimNao sinEnviarEmailNotificacao, String dataRetornoProgramado,
                                                        String idMarcador, String textoMarcador) {

        return seiPortType.gerarProcedimento(siglaSistema, identificacaoServico, unidade.getId(), procedimento, documentos,
                procedimentosRelacionados, unidadesEnvio, sinManterAbertoUnidade.getCodigo(),
                sinEnviarEmailNotificacao.getCodigo(), dataRetornoProgramado, "0", SimNao.NAO.getCodigo(),
                idMarcador, textoMarcador);
    }

    /**
     * Reabrir processo.
     *
     * @param protocoloProcedimento o(a) protocolo procedimento.
     * @return o valor de boolean
     */
    @Override
    public Boolean reabrirProcesso(UnidadeSei unidade, String protocoloProcedimento) {
        String retorno = seiPortType.reabrirProcesso(siglaSistema, identificacaoServico, unidade.getId(), protocoloProcedimento);
        return converterRetornoBooleano(retorno);
    }

    /**
     * Listar usuarios.
     *
     * @param idUsuario o(a) id usuario.
     * @return o valor de array of usuario
     */
    @Override
    public List<Usuario> listarUsuarios(UnidadeSei unidade, String idUsuario) {
        ArrayOfUsuario arrayOfUsuario = seiPortType.listarUsuarios(siglaSistema, identificacaoServico, unidade.getId(), idUsuario);
        if (arrayOfUsuario == null) {
            return Collections.emptyList();
        }
        return arrayOfUsuario.getItem();
    }

    /**
     * Faz a pesquisa de procedimento (processo) retornando apenas os dados
     * básicos. Para uma pesquisa mais abrangente utilizar
     * {@link #consultarProcedimento(UnidadeSei, String, SimNao, SimNao, SimNao, SimNao, SimNao, SimNao, SimNao, SimNao, SimNao)}
     *
     * @param protocoloProcedimento o(a) protocolo procedimento.
     * @return o valor de retorno consulta procedimento
     */
    @Override
    public RetornoConsultaProcedimento consultarProcedimentoBasico(UnidadeSei unidade, String protocoloProcedimento) {
        return consultarProcedimento(unidade, protocoloProcedimento, SimNao.NAO, SimNao.NAO, SimNao.NAO, SimNao.NAO, SimNao.NAO, SimNao.NAO, SimNao.NAO, SimNao.NAO, SimNao.NAO);
    }

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
    @Override
    public RetornoConsultaProcedimento consultarProcedimento(UnidadeSei unidade, String protocoloProcedimento, SimNao sinRetornarAssuntos,
                                                             SimNao sinRetornarInteressados, SimNao sinRetornarObservacoes,
                                                             SimNao sinRetornarAndamentoGeracao, SimNao sinRetornarAndamentoConclusao,
                                                             SimNao sinRetornarUltimoAndamento, SimNao sinRetornarUnidadesProcedimentoAberto,
                                                             SimNao sinRetornarProcedimentosRelacionados, SimNao sinRetornarProcedimentosAnexados) {

        return seiPortType.consultarProcedimento(siglaSistema, identificacaoServico, unidade.getId(), protocoloProcedimento, sinRetornarAssuntos.getCodigo(),
                sinRetornarInteressados.getCodigo(), sinRetornarObservacoes.getCodigo(), sinRetornarAndamentoGeracao.getCodigo(),
                sinRetornarAndamentoConclusao.getCodigo(), sinRetornarUltimoAndamento.getCodigo(), sinRetornarUnidadesProcedimentoAberto.getCodigo(),
                sinRetornarProcedimentosRelacionados.getCodigo(), sinRetornarProcedimentosAnexados.getCodigo());
    }

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
    @Override
    public RetornoConsultaProcedimento consultarProcedimento(String protocoloProcedimento, SimNao sinRetornarAssuntos,
                                                             SimNao sinRetornarInteressados, SimNao sinRetornarObservacoes,
                                                             SimNao sinRetornarAndamentoGeracao, SimNao sinRetornarAndamentoConclusao,
                                                             SimNao sinRetornarUltimoAndamento, SimNao sinRetornarUnidadesProcedimentoAberto,
                                                             SimNao sinRetornarProcedimentosRelacionados, SimNao sinRetornarProcedimentosAnexados) {

        return seiPortType.consultarProcedimento(siglaSistema, identificacaoServico, protocoloProcedimento, sinRetornarAssuntos.getCodigo(),
                sinRetornarInteressados.getCodigo(), sinRetornarObservacoes.getCodigo(), sinRetornarAndamentoGeracao.getCodigo(),
                sinRetornarAndamentoConclusao.getCodigo(), sinRetornarUltimoAndamento.getCodigo(), sinRetornarUnidadesProcedimentoAberto.getCodigo(),
                sinRetornarProcedimentosRelacionados.getCodigo(), sinRetornarProcedimentosAnexados.getCodigo());
    }

    /**
     * Atribuir processo.
     *
     * @param protocoloProcedimento o(a) protocolo procedimento.
     * @param idUsuario             o(a) id usuario.
     * @param sinReabrir            o(a) sin reabrir.
     * @return o valor de boolean
     */
    @Override
    public Boolean atribuirProcesso(UnidadeSei unidade, String protocoloProcedimento, String idUsuario, SimNao sinReabrir) {
        String retorno = seiPortType.atribuirProcesso(siglaSistema, identificacaoServico, unidade.getId(), protocoloProcedimento,
                idUsuario, sinReabrir.getCodigo());
        return converterRetornoBooleano(retorno);
    }

    /**
     * Incluir documento bloco.
     *
     * @param idBloco            o(a) id bloco.
     * @param protocoloDocumento o(a) protocolo documento.
     * @return o valor de boolean
     */
    @Override
    public Boolean incluirDocumentoBloco(UnidadeSei unidade, String idBloco, String protocoloDocumento) {
        String retorno = seiPortType.incluirDocumentoBloco(siglaSistema, identificacaoServico, unidade.getId(), idBloco, protocoloDocumento, null);
        return converterRetornoBooleano(retorno);
    }

    /**
     * Concluir processo.
     *
     * @param protocoloProcedimento o(a) protocolo procedimento.
     * @return o valor de boolean
     */
    @Override
    public Boolean concluirProcesso(UnidadeSei unidade, String protocoloProcedimento) {
        String retorno = seiPortType.concluirProcesso(siglaSistema, identificacaoServico, unidade.getId(), protocoloProcedimento);
        return converterRetornoBooleano(retorno);
    }

    /**
     * Cancelar disponibilizacao bloco.
     *
     * @param idBloco o(a) id bloco.
     * @return o valor de boolean
     */
    @Override
    public Boolean cancelarDisponibilizacaoBloco(UnidadeSei unidade, String idBloco) {
        String retorno = seiPortType.cancelarDisponibilizacaoBloco(siglaSistema, identificacaoServico, unidade.getId(), idBloco);
        return converterRetornoBooleano(retorno);
    }

    /**
     * Listar unidades.
     *
     * @param idTipoProcedimento o(a) id tipo procedimento.
     * @param idSerie            o(a) id serie.
     * @return o valor de array of unidade
     */
    @Override
    public List<Unidade> listarUnidades(String idTipoProcedimento, String idSerie) {
        return seiPortType.listarUnidades(siglaSistema, identificacaoServico, idTipoProcedimento, idSerie).getItem();
    }

    /**
     * Listar unidades.
     *
     * @return o valor de array of unidade
     */
    @Override
    public List<Unidade> listarUnidades() {
        return seiPortType.listarUnidades(siglaSistema, identificacaoServico).getItem();
    }


    /**
     * Listar series.
     *
     * @param idTipoProcedimento o(a) id tipo procedimento.
     * @return o valor de array of serie
     */
    @Override
    public List<Serie> listarSeries(UnidadeSei unidade, String idTipoProcedimento) {
        ArrayOfSerie arrayOfSerie = seiPortType.listarSeries(siglaSistema, identificacaoServico, unidade.getId(), idTipoProcedimento);
        if (arrayOfSerie == null) {
            return Collections.emptyList();
        }
        return arrayOfSerie.getItem();
    }

    /**
     * Listar series.
     *
     * @return o valor de array of serie
     */
    @Override
    public List<Serie> listarSeries() {
        ArrayOfSerie arrayOfSerie = seiPortType.listarSeries(siglaSistema, identificacaoServico);
        if (arrayOfSerie == null) {
            return Collections.emptyList();
        }
        return arrayOfSerie.getItem();
    }

    /**
     * Excluir bloco.
     *
     * @param idBloco o(a) id bloco.
     * @return o valor de boolean
     */
    @Override
    public Boolean excluirBloco(UnidadeSei unidade, String idBloco) {
        String retorno = seiPortType.excluirBloco(siglaSistema, identificacaoServico, unidade.getId(), idBloco);
        return converterRetornoBooleano(retorno);
    }

    /**
     * Disponibilizar bloco.
     *
     * @param idBloco o(a) id bloco.
     * @return o valor de boolean
     */
    @Override
    public Boolean disponibilizarBloco(UnidadeSei unidade, String idBloco) {
        String retorno = seiPortType.disponibilizarBloco(siglaSistema, identificacaoServico, unidade.getId(), idBloco);
        return converterRetornoBooleano(retorno);
    }

    /**
     * Incluir processo bloco.
     *
     * @param idBloco               o(a) id bloco.
     * @param protocoloProcedimento o(a) protocolo procedimento.
     * @return o valor de boolean
     */
    @Override
    public Boolean incluirProcessoBloco(UnidadeSei unidade, String idBloco, String protocoloProcedimento) {
        String retorno = seiPortType.incluirProcessoBloco(siglaSistema, identificacaoServico, unidade.getId(), idBloco, protocoloProcedimento, null);
        return converterRetornoBooleano(retorno);
    }

    /**
     * Incluir documento.
     *
     * @param documento o(a) documento.
     * @return o valor de retorno inclusao documento
     */
    @Override
    public RetornoInclusaoDocumento incluirDocumento(UnidadeSei unidade, Documento documento) {
        return seiPortType.incluirDocumento(siglaSistema, identificacaoServico, unidade.getId(), documento);
    }

    @Override
    public String adicionarArquivo(UnidadeSei unidade, String nome, String tamanho, String hash, String conteudo) {
        return seiPortType.adicionarArquivo(siglaSistema, identificacaoServico, unidade.getId(), nome, tamanho, hash, conteudo);
    }

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
    @Override
    public String gerarBloco(UnidadeSei unidade, TipoBlocoEnum tipoBlocoEnum, String descricao, ArrayOfIdUnidade unidadesDisponibilizacao,
                             ArrayOfDocumentoFormatado documentos, SimNao sinDisponibilizar) {
        return seiPortType.gerarBloco(siglaSistema, identificacaoServico, unidade.getId(), tipoBlocoEnum.getSigla(), descricao,
                unidadesDisponibilizacao, documentos, sinDisponibilizar.getCodigo());
    }

    /**
     * Gerar bloco, utiliza dados padrão nos campos opcionais.
     * Não faz a disponibilização de bloco.
     *
     * @param tipoBlocoEnum o(a) tipo bloco enum.
     * @param descricao     o(a) descricao.
     * @return o valor de string
     */
    @Override
    public String gerarBloco(UnidadeSei unidade, TipoBlocoEnum tipoBlocoEnum, String descricao) {
        return gerarBloco(unidade, tipoBlocoEnum, descricao, ConstantesSEI.ID_UNIDADE_EMPTY, ConstantesSEI.DOCUMENTOS_FORMATADOS_EMPTY, SimNao.NAO);
    }

    /**
     * Consultar documento, utilize os atributos adicionais
     * para realizar uma consulta com mais informações a respeito
     * do documento.
     *
     * @param protocoloDocumento          o(a) protocolo documento {@link RetornoInclusaoDocumento#getDocumentoFormatado()}.
     * @param sinRetornarAndamentoGeracao o(a) sin retornar andamento geracao.
     * @param sinRetornarAssinaturas      o(a) sin retornar assinaturas.
     * @param sinRetornarPublicacao       o(a) sin retornar publicacao.
     * @param sinRetornarCampos           o(a) sin retornar campos.
     * @return o valor de retorno consulta documento
     */
    @Override
    public RetornoConsultaDocumento consultarDocumento(UnidadeSei unidade, String protocoloDocumento, SimNao sinRetornarAndamentoGeracao,
                                                       SimNao sinRetornarAssinaturas, SimNao sinRetornarPublicacao, SimNao sinRetornarCampos) {
        return seiPortType.consultarDocumento(siglaSistema, identificacaoServico, unidade != null ? unidade.getId() : "", protocoloDocumento,
                sinRetornarAndamentoGeracao.getCodigo(), sinRetornarAssinaturas.getCodigo(),
                sinRetornarPublicacao.getCodigo(), sinRetornarCampos.getCodigo());
    }

    /**
     * Consultar documento da forma mais simples, caso seja necessária
     * uma consulta mais completa utilizar {@link #consultarDocumento(UnidadeSei, String, SimNao, SimNao, SimNao, SimNao)} .
     *
     * @param protocoloDocumento o(a) protocolo documento.
     * @return o valor de retorno consulta documento
     */
    @Override
    public RetornoConsultaDocumento consultarDocumento(UnidadeSei unidade, String protocoloDocumento) {
        return consultarDocumento(unidade, protocoloDocumento, SimNao.NAO, SimNao.NAO, SimNao.NAO, SimNao.NAO);
    }

    /**
     * Consultar documento retornando dados de publicacao.
     *
     * @param protocoloDocumento o(a) protocolo documento
     * @return o(a) retorno consulta documento
     */
    @Override
    public RetornoConsultaDocumento consultarDocumentoPublicacao(UnidadeSei unidade, String protocoloDocumento) {
        return consultarDocumento(unidade, protocoloDocumento, SimNao.NAO, SimNao.NAO, SimNao.SIM, SimNao.NAO);
    }

    @Override
    public RetornoConsultaDocumento consultarDocumentoAssinatura(UnidadeSei unidade, String protocoloDocumento) {
        return consultarDocumento(unidade, protocoloDocumento, SimNao.NAO, SimNao.SIM, SimNao.NAO, SimNao.NAO);
    }

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
    @Override
    public Boolean enviarProcesso(UnidadeSei unidade, String protocoloProcedimento, ArrayOfIdUnidade unidadesDestino,
                                  SimNao sinManterAbertoUnidade, SimNao sinRemoverAnotacao, SimNao sinEnviarEmailNotificacao,
                                  String dataRetornoProgramado, String diasRetornoProgramado) {
        String retorno = seiPortType.enviarProcesso(siglaSistema, identificacaoServico, unidade.getId(), protocoloProcedimento,
                unidadesDestino, sinManterAbertoUnidade.getCodigo(), sinRemoverAnotacao.getCodigo(),
                sinEnviarEmailNotificacao.getCodigo(), dataRetornoProgramado, diasRetornoProgramado, SimNao.NAO.getCodigo(), SimNao.NAO.getCodigo());
        return converterRetornoBooleano(retorno);
    }

    /**
     * Retirar documento bloco.
     *
     * @param idBloco            o(a) id bloco.
     * @param protocoloDocumento o(a) protocolo documento.
     * @return o valor de boolean
     */
    @Override
    public Boolean retirarDocumentoBloco(UnidadeSei unidade, String idBloco, String protocoloDocumento) {
        String retorno = seiPortType.retirarDocumentoBloco(siglaSistema, identificacaoServico, unidade.getId(),
                idBloco, protocoloDocumento);
        return converterRetornoBooleano(retorno);
    }

    /**
     * Retirar processo bloco.
     *
     * @param idBloco               o(a) id bloco.
     * @param protocoloProcedimento o(a) protocolo procedimento.
     * @return o valor de boolean
     */
    @Override
    public Boolean retirarProcessoBloco(UnidadeSei unidade, String idBloco, String protocoloProcedimento) {
        String retorno = seiPortType.retirarProcessoBloco(siglaSistema, identificacaoServico, unidade.getId(),
                idBloco, protocoloProcedimento);
        return converterRetornoBooleano(retorno);
    }

    /**
     * Listar extensoes permitidas.
     *
     * @param idArquivoExtensao o(a) id arquivo extensao.
     * @return o valor de array of arquivo extensao
     */
    @Override
    public List<ArquivoExtensao> listarExtensoesPermitidas(UnidadeSei unidade, String idArquivoExtensao) {
        ArrayOfArquivoExtensao arrayOfArquivoExtensao = seiPortType.listarExtensoesPermitidas(siglaSistema, identificacaoServico, unidade.getId(), idArquivoExtensao);
        if (arrayOfArquivoExtensao == null) {
            return Collections.emptyList();
        }
        return arrayOfArquivoExtensao.getItem();
    }

    /**
     * Listar tipos procedimento.
     *
     * @param idSerie o(a) id serie.
     * @return o valor de array of tipo procedimento
     */
    @Override
    public List<TipoProcedimento> listarTiposProcedimento(UnidadeSei unidade, String idSerie) {
        ArrayOfTipoProcedimento arrayOfTipoProcedimento = seiPortType.listarTiposProcedimento(siglaSistema, identificacaoServico, unidade.getId(), idSerie);
        if (arrayOfTipoProcedimento == null) {
            return Collections.emptyList();
        }
        return arrayOfTipoProcedimento.getItem();
    }

    /**
     * Listar tipos procedimento.
     *
     * @return o valor de array of tipo procedimento
     */
    @Override
    public List<TipoProcedimento> listarTiposProcedimento() {
        ArrayOfTipoProcedimento arrayOfTipoProcedimento = seiPortType.listarTiposProcedimento(siglaSistema, identificacaoServico);
        if (arrayOfTipoProcedimento == null) {
            return Collections.emptyList();
        }
        return arrayOfTipoProcedimento.getItem();
    }

    /**
     * Faz a conversão dos retornos de uma string
     * binária (0 ou 1) para um booleano.
     *
     * @param valor valor binário
     * @return false caso 0, true caso diferente de 0 ou null caso o parametro seja null.
     */
    private Boolean converterRetornoBooleano(String valor) {//NOSONAR
        if (valor == null) {
            return null;//NOSONAR
        }
        // Retorna false para 0 e true para
        // qualquer coisa que não seja 0.
        return !"0".equalsIgnoreCase(valor.trim());
    }

    @Override
    public RetornoConsultaBloco consultarBloco(UnidadeSei unidade, String idBloco) {
        return seiPortType.consultarBloco(siglaSistema, identificacaoServico, unidade.getId(), idBloco, SimNao.SIM.getCodigo());
    }

    @Override
    public String cancelarDocumento(UnidadeSei unidade, String protocoloDocumento, String motivo) {
        return seiPortType.cancelarDocumento(siglaSistema, identificacaoServico, unidade.getId(), protocoloDocumento, motivo);

    }

    @Override
    public List<Contato> listarContatos(@Nullable UnidadeSei unidade, String idTipoContato, String sigla) {
        return seiPortType.listarContatos(siglaSistema, identificacaoServico, Optional.ofNullable(unidade).map(UnidadeSei::getId).orElse(""), idTipoContato, "", "", sigla, "", "", "", "", new ArrayOfString()).getItem();
    }


    @Override
    public Contato findContatoByCPF(@Nullable UnidadeSei unidade, String idTipoContato, String cpf) {
        List<Contato> contatoList = seiPortType.listarContatos(siglaSistema, identificacaoServico, Optional.ofNullable(unidade)
                        .map(UnidadeSei::getId).orElse(""), idTipoContato, "", "", "", "", cpf, "", "",
                new ArrayOfString()).getItem();
        if (CollectionUtils.isNotEmpty(contatoList)) {
            if (contatoList.size() > 1) {
                getLogger().warn("Was found more than one contact with the same CPF: {}", cpf);
            }
            return contatoList.get(0);
        }
        return null;
    }


}
