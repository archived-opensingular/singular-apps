package org.opensingular.server.connector.sei30;

import org.opensingular.server.connector.sei30.model.SimNao;
import org.opensingular.server.connector.sei30.model.TipoBlocoEnum;
import org.opensingular.server.connector.sei30.model.UnidadeSei;
import org.opensingular.server.connector.sei30.ws.ArquivoExtensao;
import org.opensingular.server.connector.sei30.ws.ArrayOfArquivoExtensao;
import org.opensingular.server.connector.sei30.ws.ArrayOfDocumento;
import org.opensingular.server.connector.sei30.ws.ArrayOfDocumentoFormatado;
import org.opensingular.server.connector.sei30.ws.ArrayOfIdUnidade;
import org.opensingular.server.connector.sei30.ws.ArrayOfProcedimentoRelacionado;
import org.opensingular.server.connector.sei30.ws.ArrayOfSerie;
import org.opensingular.server.connector.sei30.ws.ArrayOfTipoProcedimento;
import org.opensingular.server.connector.sei30.ws.ArrayOfUsuario;
import org.opensingular.server.connector.sei30.ws.Documento;
import org.opensingular.server.connector.sei30.ws.Procedimento;
import org.opensingular.server.connector.sei30.ws.RetornoConsultaBloco;
import org.opensingular.server.connector.sei30.ws.RetornoConsultaDocumento;
import org.opensingular.server.connector.sei30.ws.RetornoConsultaProcedimento;
import org.opensingular.server.connector.sei30.ws.RetornoGeracaoProcedimento;
import org.opensingular.server.connector.sei30.ws.RetornoInclusaoDocumento;
import org.opensingular.server.connector.sei30.ws.SeiPortType;
import org.opensingular.server.connector.sei30.ws.SeiService;
import org.opensingular.server.connector.sei30.ws.Serie;
import org.opensingular.server.connector.sei30.ws.TipoProcedimento;
import org.opensingular.server.connector.sei30.ws.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.ws.soap.MTOMFeature;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.opensingular.server.connector.sei30.ConstantesSEI.*;
import static org.opensingular.server.connector.sei30.model.SimNao.NAO;


/**
 * Classe SEIWS.
 * O objetivo desta classe é enriquecer a API para chamadas
 * ao SEI, visto que esta classe não é gerada, podem ser criados
 * diversos métodos para facilitar o desenvolvimento.
 */
public class SEIWS implements SEIPortType {

    private final SeiPortType seiPortType;
    private final String siglaSistema;
    private final String identificacaoServico;
    private final UnidadeSei unidadeSei;
    private static final Logger logger = LoggerFactory.getLogger(SEIWS.class);

    /**
     * Instancia um novo objeto SEIWS delegate.
     * 
     * @param siglaSistema
     *            o(a) sigla sistema.
     * @param identificacaoServico
     *            o(a) identificacao servico.
     * @param unidadeSei
     *            o(a) unidade.
     */
    public SEIWS(String siglaSistema, String identificacaoServico, UnidadeSei unidadeSei) {
        this.seiPortType = getSeiService();
        this.siglaSistema = siglaSistema;
        this.identificacaoServico = identificacaoServico;
        this.unidadeSei = unidadeSei;
    }

    private SeiPortType getSeiService() {
        return new SeiService(new MTOMFeature()).getSeiPortService();
    }

    /**
     * Método que simplifica a criação de um procedimento (processo)
     * de forma que todos os demais valores tenham um padrão.
     *
     * @param procedimento procedimento a ser inserido
     * @return o retorno da geração do procedimento
     */
    @Override
    public RetornoGeracaoProcedimento gerarProcedimento(Procedimento procedimento) {
        return gerarProcedimento(procedimento, DOCUMENTOS_EMPTY, PROCEDIMENTO_RELACIONADOS_EMPTY, ID_UNIDADE_EMPTY, NAO, NAO, "", null, null);
    }

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
     *            o(a) sinalizador para manter aberto unidade.
     * @param sinEnviarEmailNotificacao
     *            o(a) sinalizador para enviar email notificacao.
     * @param dataRetornoProgramado
     *            o(a) data retorno programado.
     * @param idMarcador
     *            o id do marcador
     * @param textoMarcador
     *            o texto do marcador
     * @return o valor de retorno geracao procedimento
     */
    @Override
    public RetornoGeracaoProcedimento gerarProcedimento(Procedimento procedimento, ArrayOfDocumento documentos,
                                                        ArrayOfProcedimentoRelacionado procedimentosRelacionados,
                                                        ArrayOfIdUnidade unidadesEnvio, SimNao sinManterAbertoUnidade,
                                                        SimNao sinEnviarEmailNotificacao, String dataRetornoProgramado,
                                                        String idMarcador, String textoMarcador) {

        return seiPortType.gerarProcedimento(siglaSistema, identificacaoServico, unidadeSei.getId(), procedimento, documentos,
                procedimentosRelacionados, unidadesEnvio, sinManterAbertoUnidade.getCodigo(),
                sinEnviarEmailNotificacao.getCodigo(), dataRetornoProgramado, "0", SimNao.NAO.getCodigo(),
                idMarcador, textoMarcador);
    }

    /**
     * Reabrir processo.
     * 
     * @param protocoloProcedimento
     *            o(a) protocolo procedimento.
     * @return o valor de boolean
     */
    @Override
    public Boolean reabrirProcesso(String protocoloProcedimento) {
        String retorno = seiPortType.reabrirProcesso(siglaSistema, identificacaoServico, unidadeSei.getId(), protocoloProcedimento);
        return converterRetornoBooleano(retorno);
    }

    /**
     * Listar usuarios.
     * 
     * @param idUsuario
     *            o(a) id usuario.
     * @return o valor de array of usuario
     */
    @Override
    public List<Usuario> listarUsuarios(String idUsuario) {
        ArrayOfUsuario arrayOfUsuario = seiPortType.listarUsuarios(siglaSistema, identificacaoServico, unidadeSei.getId(), idUsuario);
        if (arrayOfUsuario == null) {
            return Collections.emptyList();
        }
        return arrayOfUsuario.getItem();
    }

    /**
     * Faz a pesquisa de procedimento (processo) retornando apenas os dados
     * básicos. Para uma pesquisa mais abrangente utilizar
     * {@link #consultarProcedimento(String, SimNao, SimNao, SimNao, SimNao, SimNao, SimNao, SimNao, SimNao, SimNao)}
     * 
     * @param protocoloProcedimento
     *            o(a) protocolo procedimento.
     * @return o valor de retorno consulta procedimento
     */
    @Override
    public RetornoConsultaProcedimento consultarProcedimentoBasico(String protocoloProcedimento) {
        return consultarProcedimento(protocoloProcedimento, NAO, NAO, NAO, NAO, NAO, NAO, NAO, NAO, NAO);
    }

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
    @Override
    public RetornoConsultaProcedimento consultarProcedimento(String protocoloProcedimento, SimNao sinRetornarAssuntos,
                                                             SimNao sinRetornarInteressados, SimNao sinRetornarObservacoes,
                                                             SimNao sinRetornarAndamentoGeracao, SimNao sinRetornarAndamentoConclusao,
                                                             SimNao sinRetornarUltimoAndamento, SimNao sinRetornarUnidadesProcedimentoAberto,
                                                             SimNao sinRetornarProcedimentosRelacionados, SimNao sinRetornarProcedimentosAnexados) {

        return seiPortType.consultarProcedimento(siglaSistema, identificacaoServico, unidadeSei.getId(), protocoloProcedimento, sinRetornarAssuntos.getCodigo(),
                sinRetornarInteressados.getCodigo(), sinRetornarObservacoes.getCodigo(), sinRetornarAndamentoGeracao.getCodigo(),
                sinRetornarAndamentoConclusao.getCodigo(), sinRetornarUltimoAndamento.getCodigo(), sinRetornarUnidadesProcedimentoAberto.getCodigo(),
                sinRetornarProcedimentosRelacionados.getCodigo(), sinRetornarProcedimentosAnexados.getCodigo());
    }

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
    @Override
    public Boolean atribuirProcesso(String protocoloProcedimento, String idUsuario, SimNao sinReabrir) {
        String retorno = seiPortType.atribuirProcesso(siglaSistema, identificacaoServico, unidadeSei.getId(), protocoloProcedimento,
                idUsuario, sinReabrir.getCodigo());
        return converterRetornoBooleano(retorno);
    }

    /**
     * Incluir documento bloco.
     * 
     * @param idBloco
     *            o(a) id bloco.
     * @param protocoloDocumento
     *            o(a) protocolo documento.
     * @return o valor de boolean
     */
    @Override
    public Boolean incluirDocumentoBloco(String idBloco, String protocoloDocumento) {
        String retorno = seiPortType.incluirDocumentoBloco(siglaSistema, identificacaoServico, unidadeSei.getId(), idBloco, protocoloDocumento, null);
        return converterRetornoBooleano(retorno);
    }

    /**
     * Concluir processo.
     * 
     * @param protocoloProcedimento
     *            o(a) protocolo procedimento.
     * @return o valor de boolean
     */
    @Override
    public Boolean concluirProcesso(String protocoloProcedimento) {
        String retorno = seiPortType.concluirProcesso(siglaSistema, identificacaoServico, unidadeSei.getId(), protocoloProcedimento);
        return converterRetornoBooleano(retorno);
    }

    /**
     * Cancelar disponibilizacao bloco.
     * 
     * @param idBloco
     *            o(a) id bloco.
     * @return o valor de boolean
     */
    @Override
    public Boolean cancelarDisponibilizacaoBloco(String idBloco) {
        String retorno = seiPortType.cancelarDisponibilizacaoBloco(siglaSistema, identificacaoServico, unidadeSei.getId(), idBloco);
        return converterRetornoBooleano(retorno);
    }

    /**
     * Listar unidades.
     * 
     * @param siglaSistema1
     *            o(a) sigla sistema.
     * @param identificacaoServico1
     *            o(a) identificacao servico.
     * @param idTipoProcedimento
     *            o(a) id tipo procedimento.
     * @param idSerie
     *            o(a) id serie.
     * @return o valor de array of unidade
     */
    @Override
    public List<org.opensingular.server.connector.sei30.ws.Unidade> listarUnidades(String siglaSistema1, String identificacaoServico1, String idTipoProcedimento, String idSerie) {
        return seiPortType.listarUnidades(siglaSistema1, identificacaoServico1, idTipoProcedimento, idSerie).getItem();
    }

    /**
     * Listar series.
     * 
     * @param idTipoProcedimento
     *            o(a) id tipo procedimento.
     * @return o valor de array of serie
     */
    @Override
    public List<Serie> listarSeries(String idTipoProcedimento) {
        ArrayOfSerie arrayOfSerie = seiPortType.listarSeries(siglaSistema, identificacaoServico, unidadeSei.getId(), idTipoProcedimento);
        if (arrayOfSerie == null) {
            return Collections.emptyList();
        }
        return arrayOfSerie.getItem();
    }

    /**
     * Excluir bloco.
     * 
     * @param idBloco
     *            o(a) id bloco.
     * @return o valor de boolean
     */
    @Override
    public Boolean excluirBloco(String idBloco) {
        String retorno = seiPortType.excluirBloco(siglaSistema, identificacaoServico, unidadeSei.getId(), idBloco);
        return converterRetornoBooleano(retorno);
    }

    /**
     * Disponibilizar bloco.
     * 
     * @param idBloco
     *            o(a) id bloco.
     * @return o valor de boolean
     */
    @Override
    public Boolean disponibilizarBloco(String idBloco) {
        String retorno = seiPortType.disponibilizarBloco(siglaSistema, identificacaoServico, unidadeSei.getId(), idBloco);
        return converterRetornoBooleano(retorno);
    }

    /**
     * Incluir processo bloco.
     * 
     * @param idBloco
     *            o(a) id bloco.
     * @param protocoloProcedimento
     *            o(a) protocolo procedimento.
     * @return o valor de boolean
     */
    @Override
    public Boolean incluirProcessoBloco(String idBloco, String protocoloProcedimento) {
        String retorno = seiPortType.incluirProcessoBloco(siglaSistema, identificacaoServico, unidadeSei.getId(), idBloco, protocoloProcedimento, null);
        return converterRetornoBooleano(retorno);
    }

    /**
     * Incluir documento.
     * 
     * @param documento
     *            o(a) documento.
     * @return o valor de retorno inclusao documento
     */
    @Override
    public RetornoInclusaoDocumento incluirDocumento(Documento documento) {

        logger.info("=========================================================");
        logger.info("INCLUINDO DOCUMENTO NO SEI AS "+new Date());
        if(documento.getIdSerie() != null) {
            logger.info("ID SERIE: " + documento.getIdSerie());
        }
        logger.info("=========================================================");

        return seiPortType.incluirDocumento(siglaSistema, identificacaoServico, unidadeSei.getId(), documento);
    }

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
    @Override
    public String gerarBloco(TipoBlocoEnum tipoBlocoEnum, String descricao, ArrayOfIdUnidade unidadesDisponibilizacao,
                             ArrayOfDocumentoFormatado documentos, SimNao sinDisponibilizar) {
        return seiPortType.gerarBloco(siglaSistema, identificacaoServico, unidadeSei.getId(), tipoBlocoEnum.getSigla(), descricao,
                unidadesDisponibilizacao, documentos, sinDisponibilizar.getCodigo());
    }

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
    @Override
    public String gerarBloco(TipoBlocoEnum tipoBlocoEnum, String descricao) {
        return gerarBloco(tipoBlocoEnum, descricao, ID_UNIDADE_EMPTY, ConstantesSEI.DOCUMENTOS_FORMATADOS_EMPTY, SimNao.NAO);
    }

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
     *            o(a) sin retornar campos.
     * @return o valor de retorno consulta documento
     */
    @Override
    public RetornoConsultaDocumento consultarDocumento(String protocoloDocumento, SimNao sinRetornarAndamentoGeracao,
                                                       SimNao sinRetornarAssinaturas, SimNao sinRetornarPublicacao, SimNao sinRetornarCampos) {
        return seiPortType.consultarDocumento(siglaSistema, identificacaoServico, unidadeSei.getId(), protocoloDocumento,
                sinRetornarAndamentoGeracao.getCodigo(), sinRetornarAssinaturas.getCodigo(),
                sinRetornarPublicacao.getCodigo(), sinRetornarCampos.getCodigo());
    }

    /**
     * Consultar documento da forma mais simples, caso seja necessária
     * uma consulta mais completa utilizar {@link #consultarDocumento(String, SimNao, SimNao, SimNao, SimNao)} .
     * 
     * @param protocoloDocumento
     *            o(a) protocolo documento.
     * @return o valor de retorno consulta documento
     */
    @Override
    public RetornoConsultaDocumento consultarDocumento(String protocoloDocumento) {
        return consultarDocumento(protocoloDocumento, SimNao.NAO, SimNao.NAO, SimNao.NAO, SimNao.NAO);
    }

    /**
     * Consultar documento retornando dados de publicacao.
     *
     * @param protocoloDocumento o(a) protocolo documento
     * @return o(a) retorno consulta documento
     */
    @Override
    public RetornoConsultaDocumento consultarDocumentoPublicacao(String protocoloDocumento) {
        return consultarDocumento(protocoloDocumento, SimNao.NAO, SimNao.NAO, SimNao.SIM, SimNao.NAO);
    }

    @Override
    public RetornoConsultaDocumento consultarDocumentoAssinatura(String protocoloDocumento) {
        return consultarDocumento(protocoloDocumento, SimNao.NAO, SimNao.SIM, SimNao.NAO, SimNao.NAO);
    }

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
    @Override
    public Boolean enviarProcesso(String protocoloProcedimento, ArrayOfIdUnidade unidadesDestino,
                                  SimNao sinManterAbertoUnidade, SimNao sinRemoverAnotacao, SimNao sinEnviarEmailNotificacao,
                                  String dataRetornoProgramado) {
        String retorno = seiPortType.enviarProcesso(siglaSistema, identificacaoServico, unidadeSei.getId(), protocoloProcedimento,
                unidadesDestino, sinManterAbertoUnidade.getCodigo(), sinRemoverAnotacao.getCodigo(),
                sinEnviarEmailNotificacao.getCodigo(), dataRetornoProgramado, "0", SimNao.NAO.getCodigo(), SimNao.NAO.getCodigo());
        return converterRetornoBooleano(retorno);
    }

    /**
     * Retirar documento bloco.
     * 
     * @param idBloco
     *            o(a) id bloco.
     * @param protocoloDocumento
     *            o(a) protocolo documento.
     * @return o valor de boolean
     */
    @Override
    public Boolean retirarDocumentoBloco(String idBloco, String protocoloDocumento) {
        String retorno = seiPortType.retirarDocumentoBloco(siglaSistema, identificacaoServico, unidadeSei.getId(),
                idBloco, protocoloDocumento);
        return converterRetornoBooleano(retorno);
    }

    /**
     * Retirar processo bloco.
     * 
     * @param idBloco
     *            o(a) id bloco.
     * @param protocoloProcedimento
     *            o(a) protocolo procedimento.
     * @return o valor de boolean
     */
    @Override
    public Boolean retirarProcessoBloco(String idBloco, String protocoloProcedimento) {
        String retorno = seiPortType.retirarProcessoBloco(siglaSistema, identificacaoServico, unidadeSei.getId(),
                idBloco, protocoloProcedimento);
        return converterRetornoBooleano(retorno);
    }

    /**
     * Listar extensoes permitidas.
     * 
     * @param idArquivoExtensao
     *            o(a) id arquivo extensao.
     * @return o valor de array of arquivo extensao
     */
    @Override
    public List<ArquivoExtensao> listarExtensoesPermitidas(String idArquivoExtensao) {
        ArrayOfArquivoExtensao arrayOfArquivoExtensao = seiPortType.listarExtensoesPermitidas(siglaSistema, identificacaoServico, unidadeSei.getId(), idArquivoExtensao);
        if (arrayOfArquivoExtensao == null) {
            return Collections.emptyList();
        }
        return arrayOfArquivoExtensao.getItem();
    }

    /**
     * Listar tipos procedimento.
     * 
     * @param idSerie
     *            o(a) id serie.
     * @return o valor de array of tipo procedimento
     */
    @Override
    public List<TipoProcedimento> listarTiposProcedimento(String idSerie) {
        ArrayOfTipoProcedimento arrayOfTipoProcedimento = seiPortType.listarTiposProcedimento(siglaSistema, identificacaoServico, unidadeSei.getId(), idSerie);
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
    private Boolean converterRetornoBooleano(String valor) {
        if (valor == null) {
            return null;
        }
        // Retorna false para 0 e true para
        // qualquer coisa que não seja 0.
        return !valor.trim().equalsIgnoreCase("0");
    }

    @Override
	public RetornoConsultaBloco consultarBloco(String idBloco) {
        return seiPortType.consultarBloco(siglaSistema, identificacaoServico, unidadeSei.getId(), idBloco, SimNao.SIM.getCodigo());
	}

	@Override
	public String cancelarDocumento(String protocoloDocumento, String motivo) {
		return seiPortType.cancelarDocumento(siglaSistema, identificacaoServico, unidadeSei.getId(), protocoloDocumento, motivo);

	}    
}
