package org.opensingular.server.connector.sei30;

import org.opensingular.server.connector.sei30.model.SimNao;
import org.opensingular.server.connector.sei30.model.TipoBlocoEnum;
import org.opensingular.server.connector.sei30.ws.ArquivoExtensao;
import org.opensingular.server.connector.sei30.ws.ArrayOfDocumento;
import org.opensingular.server.connector.sei30.ws.ArrayOfDocumentoFormatado;
import org.opensingular.server.connector.sei30.ws.ArrayOfIdUnidade;
import org.opensingular.server.connector.sei30.ws.ArrayOfProcedimentoRelacionado;
import org.opensingular.server.connector.sei30.ws.Documento;
import org.opensingular.server.connector.sei30.ws.Procedimento;
import org.opensingular.server.connector.sei30.ws.RetornoConsultaBloco;
import org.opensingular.server.connector.sei30.ws.RetornoConsultaDocumento;
import org.opensingular.server.connector.sei30.ws.RetornoConsultaProcedimento;
import org.opensingular.server.connector.sei30.ws.RetornoGeracaoProcedimento;
import org.opensingular.server.connector.sei30.ws.RetornoInclusaoDocumento;
import org.opensingular.server.connector.sei30.ws.Serie;
import org.opensingular.server.connector.sei30.ws.TipoProcedimento;
import org.opensingular.server.connector.sei30.ws.Unidade;
import org.opensingular.server.connector.sei30.ws.Usuario;

import javax.activation.DataHandler;
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
    RetornoGeracaoProcedimento gerarProcedimento(Procedimento procedimento);

    /**
     * Gerar procedimento.
     *
     * @param procedimento
     *            o(a) procedimento.
     * @param documentos
     *            o(a) documentos.
     * @param procedimentosRelacionados
     *            o(a) procedimentos relacionados.
     * @param unidadesEnvio
     *            o(a) unidades envio.
     * @param sinManterAbertoUnidade
     *            o(a) sin manter aberto unidade.
     * @param sinEnviarEmailNotificacao
     *            o(a) sin enviar email notificacao.
     * @param dataRetornoProgramado
     *            o(a) data retorno programado.
     * @param idMarcador
     *            o id do marcador
     * @param textoMarcador
     *            o texto do marcador
     * @return o valor de retorno geracao procedimento
     */
    RetornoGeracaoProcedimento gerarProcedimento(Procedimento procedimento, ArrayOfDocumento documentos,
                                                 ArrayOfProcedimentoRelacionado procedimentosRelacionados,
                                                 ArrayOfIdUnidade unidadesEnvio, SimNao sinManterAbertoUnidade,
                                                 SimNao sinEnviarEmailNotificacao, String dataRetornoProgramado,
                                                 String idMarcador, String textoMarcador);

    /**
     * Reabrir processo.
     *
     * @param protocoloProcedimento
     *            o(a) protocolo procedimento.
     * @return o valor de boolean
     */
    Boolean reabrirProcesso(String protocoloProcedimento);

    /**
     * Listar usuarios.
     *
     * @param idUsuario
     *            o(a) id usuario.
     * @return o valor de array of usuario
     */
    List<Usuario> listarUsuarios(String idUsuario);

    /**
     * Faz a pesquisa de procedimento (processo) retornando apenas os dados
     * básicos. Para uma pesquisa mais abrangente utilizar
     * {@link #consultarProcedimento(String, SimNao, SimNao, SimNao, SimNao, SimNao, SimNao, SimNao, SimNao, SimNao)}
     *
     * @param protocoloProcedimento
     *            o(a) protocolo procedimento.
     * @return o valor de retorno consulta procedimento
     */
    RetornoConsultaProcedimento consultarProcedimentoBasico(String protocoloProcedimento);

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
     * @param protocoloProcedimento
     *            o(a) protocolo procedimento.
     * @param idUsuario
     *            o(a) id usuario.
     * @param sinReabrir
     *            o(a) sin reabrir.
     * @return o valor de boolean
     */
    Boolean atribuirProcesso(String protocoloProcedimento, String idUsuario, SimNao sinReabrir);

    /**
     * Incluir documento bloco.
     *
     * @param idBloco
     *            o(a) id bloco.
     * @param protocoloDocumento
     *            o(a) protocolo documento.
     * @return o valor de boolean
     */
    Boolean incluirDocumentoBloco(String idBloco, String protocoloDocumento);

    /**
     * Concluir processo.
     *
     * @param protocoloProcedimento
     *            o(a) protocolo procedimento.
     * @return o valor de boolean
     */
    Boolean concluirProcesso(String protocoloProcedimento);

    /**
     * Cancelar disponibilizacao bloco.
     *
     * @param idBloco
     *            o(a) id bloco.
     * @return o valor de boolean
     */
    Boolean cancelarDisponibilizacaoBloco(String idBloco);

    /**
     * Listar unidades.
     *
     * @param siglaSistema
     *            o(a) sigla sistema.
     * @param identificacaoServico
     *            o(a) identificacao servico.
     * @param idTipoProcedimento
     *            o(a) id tipo procedimento.
     * @param idSerie
     *            o(a) id serie.
     * @return o valor de array of unidade
     */
    List<Unidade> listarUnidades(String siglaSistema, String identificacaoServico, String idTipoProcedimento, String idSerie);

    /**
     * Listar series.
     *
     * @param idTipoProcedimento
     *            o(a) id tipo procedimento.
     * @return o valor de array of serie
     */
    List<Serie> listarSeries(String idTipoProcedimento);

    /**
     * Excluir bloco.
     *
     * @param idBloco
     *            o(a) id bloco.
     * @return o valor de boolean
     */
    Boolean excluirBloco(String idBloco);

    /**
     * Disponibilizar bloco.
     *
     * @param idBloco
     *            o(a) id bloco.
     * @return o valor de boolean
     */
    Boolean disponibilizarBloco(String idBloco);

    /**
     * Incluir processo bloco.
     *
     * @param idBloco
     *            o(a) id bloco.
     * @param protocoloProcedimento
     *            o(a) protocolo procedimento.
     * @return o valor de boolean
     */
    Boolean incluirProcessoBloco(String idBloco, String protocoloProcedimento);

    /**
     * Incluir documento.
     *
     * @param documento
     *            o(a) documento.
     * @return o valor de retorno inclusao documento
     */
    RetornoInclusaoDocumento incluirDocumento(Documento documento);

    String adicionarArquivo(String nome, String tamanho, String hash, String conteudo);

    /**
     * Gerar bloco.
     *
     * @param tipoBlocoEnum
     *            o(a) tipo bloco enum.
     * @param descricao
     *            o(a) descricao.
     * @param unidadesDisponibilizacao
     *            o(a) unidades disponibilizacao.
     * @param documentos
     *            o(a) documentos.
     * @param sinDisponibilizar
     *            o(a) sin disponibilizar.
     * @return o valor de string
     */
    String gerarBloco(TipoBlocoEnum tipoBlocoEnum, String descricao, ArrayOfIdUnidade unidadesDisponibilizacao,
                      ArrayOfDocumentoFormatado documentos, SimNao sinDisponibilizar);

    /**
     * Gerar bloco, utiliza dados padrão nos campos opcionais.
     * Não faz a disponibilização de bloco.
     *
     * @param tipoBlocoEnum
     *            o(a) tipo bloco enum.
     * @param descricao
     *            o(a) descricao.
     * @return o valor de string
     */
    String gerarBloco(TipoBlocoEnum tipoBlocoEnum, String descricao);

    /**
     * Consultar documento, utilize os atributos adicionais
     * para realizar uma consulta com mais informações a respeito
     * do documento.
     *
     * @param protocoloDocumento
     *            o(a) protocolo documento {@link RetornoInclusaoDocumento#getDocumentoFormatado()}.
     * @param sinRetornarAndamentoGeracao
     *            o(a) sin retornar andamento geracao.
     * @param sinRetornarAssinaturas
     *            o(a) sin retornar assinaturas.
     * @param sinRetornarPublicacao
     *            o(a) sin retornar publicacao.
     * @param sinRetornarCampos
     * @return o valor de retorno consulta documento
     */
    RetornoConsultaDocumento consultarDocumento(String protocoloDocumento, SimNao sinRetornarAndamentoGeracao,
                                                SimNao sinRetornarAssinaturas, SimNao sinRetornarPublicacao, SimNao sinRetornarCampos);

    /**
     * Consultar documento da forma mais simples, caso seja necessária
     * uma consulta mais completa utilizar {@link #consultarDocumento(String, SimNao, SimNao, SimNao, SimNao)} .
     *
     * @param protocoloDocumento
     *            o(a) protocolo documento.
     * @return o valor de retorno consulta documento
     */
    RetornoConsultaDocumento consultarDocumento(String protocoloDocumento);

    /**
     * Consultar documento retornando dados de publicacao.
     *
     * @param protocoloDocumento o(a) protocolo documento
     * @return o(a) retorno consulta documento
     */
    RetornoConsultaDocumento consultarDocumentoPublicacao(String protocoloDocumento);

    /**
     * Consultar documento retornando dados de assinatura.
     *
     * @param protocoloDocumento o(a) protocolo documento
     * @return o(a) retorno consulta documento
     */
    RetornoConsultaDocumento consultarDocumentoAssinatura(String protocoloDocumento);

    /**
     * Enviar processo.
     *
     * @param protocoloProcedimento
     *            o(a) protocolo procedimento.
     * @param unidadesDestino
     *            o(a) unidades destino.
     * @param sinManterAbertoUnidade
     *            o(a) sin manter aberto unidade.
     * @param sinRemoverAnotacao
     *            o(a) sin remover anotacao.
     * @param sinEnviarEmailNotificacao
     *            o(a) sin enviar email notificacao.
     * @param dataRetornoProgramado
     *            o(a) data retorno programado.
     * @return o valor de boolean
     */
    Boolean enviarProcesso(String protocoloProcedimento, ArrayOfIdUnidade unidadesDestino,
                           SimNao sinManterAbertoUnidade, SimNao sinRemoverAnotacao, SimNao sinEnviarEmailNotificacao,
                           String dataRetornoProgramado);

    /**
     * Retirar documento bloco.
     *
     * @param idBloco
     *            o(a) id bloco.
     * @param protocoloDocumento
     *            o(a) protocolo documento.
     * @return o valor de boolean
     */
    Boolean retirarDocumentoBloco(String idBloco, String protocoloDocumento);

    /**
     * Retirar processo bloco.
     *
     * @param idBloco
     *            o(a) id bloco.
     * @param protocoloProcedimento
     *            o(a) protocolo procedimento.
     * @return o valor de boolean
     */
    Boolean retirarProcessoBloco(String idBloco, String protocoloProcedimento);

    /**
     * Listar extensoes permitidas.
     *
     * @param idArquivoExtensao
     *            o(a) id arquivo extensao.
     * @return o valor de array of arquivo extensao
     */
    List<ArquivoExtensao> listarExtensoesPermitidas(String idArquivoExtensao);

    /**
     * Listar tipos procedimento.
     *
     * @param idSerie
     *            o(a) id serie.
     * @return o valor de array of tipo procedimento
     */
    List<TipoProcedimento> listarTiposProcedimento(String idSerie);


	/**
	 * Consultar bloco
	 *
	 * @param idBloco
	 *            - o(a) id do bloco
	 * @return o retorno da consulta de bloco
	 * 
	 */
	public RetornoConsultaBloco consultarBloco(String idBloco);

	/**
	 * Cancelamento de documentos
	 * 
	 * @param motivo
	 *            - o(a) motivo
	 * @param protocoloDocumento
	 *            - o(a) protocolo do documento
	 * @return - O retorno do cancelamento do documento
	 */
	public String cancelarDocumento(String protocoloDocumento, String motivo);


}
