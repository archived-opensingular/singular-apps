package org.opensingular.server.connector.sei30.builder;

import org.opensingular.lib.commons.util.TempFileUtils;
import org.opensingular.server.commons.exception.SingularServerException;
import org.opensingular.server.connector.sei30.model.NivelAcesso;
import org.opensingular.server.connector.sei30.model.SerieEnum;
import org.opensingular.server.connector.sei30.model.TipoDocumento;
import org.opensingular.server.connector.sei30.ws.ArrayOfDestinatario;
import org.opensingular.server.connector.sei30.ws.ArrayOfInteressado;
import org.opensingular.server.connector.sei30.ws.Destinatario;
import org.opensingular.server.connector.sei30.ws.Documento;
import org.opensingular.server.connector.sei30.ws.Interessado;
import org.opensingular.server.connector.sei30.ws.Remetente;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.activation.DataHandler;
import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Classe DocumentoBuilder.
 */
public class DocumentoBuilder implements Serializable {

    private static final String ERRO_CARREGAR_ARQUIVO = "Erro ao carregar arquivo";

    private Documento documento;

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Instancia um novo objeto documento builder.
     */
    public DocumentoBuilder() {
        this.documento = new Documento();
    }

    /**
     * Instancia um novo objeto documento builder.
     * 
     * @param documento
     *            o(a) documento.
     */
    public DocumentoBuilder(Documento documento) {
        this.documento = documento;
    }
    /**
     * Atualiza o novo valor de tipo.
     * 
     * @param tipoDocumento
     *            o(a) tipo documento.
     * @return o valor de documento builder
     */
    public DocumentoBuilder setTipo(TipoDocumento tipoDocumento) {
        this.documento.setTipo(tipoDocumento.getCodigo());
        return this;
    }

    /**
     * Atualiza o novo valor de id procedimento.
     * 
     * @param value
     *            o(a) value.
     * @return o valor de documento builder
     */
    public DocumentoBuilder setIdProcedimento(String value) {
        this.documento.setIdProcedimento(value);
        return this;
    }

    /**
     * Atualiza o novo valor de tipo serie.
     * 
     * @param value
     *            o(a) value.
     * @return o valor de documento builder
     */
    public DocumentoBuilder setSerie(SerieEnum value) {
        this.documento.setIdSerie(value.getId());
        return this;
    }

    /**
     * Atualiza o novo valor de numero.
     * 
     * @param value
     *            o(a) value.
     * @return o valor de documento builder
     */
    public DocumentoBuilder setNumero(String value) {
        this.documento.setNumero(value);
        return this;
    }

    /**
     * Atualiza o novo valor de data.
     * 
     * @deprecated use setData(Date value)
     * @param value
     *            o(a) value.
     * @return o valor de documento builder
     */
    @Deprecated
    public DocumentoBuilder setData(String value) {

        this.documento.setData(value);
        return this;
    }

    /**
     * Atualiza o novo valor de data.
     * 
     * @param value
     *            o(a) value.
     * @return o valor de documento builder
     */
    public DocumentoBuilder setData(Date value) {
        this.documento.setData(new SimpleDateFormat("dd/MM/yyyy").format(value));
        return this;
    }

    /**
     * Atualiza o novo valor de descricao.
     * 
     * @param value
     *            o(a) value.
     * @return o valor de documento builder
     */
    public DocumentoBuilder setDescricao(String value) {
        this.documento.setDescricao(value);
        return this;
    }

    /**
     * Atualiza o novo valor de remetente.
     * 
     * @param value
     *            o(a) value.
     * @return o valor de documento builder
     */
    public DocumentoBuilder setRemetente(String value) {
        Remetente rementente = new Remetente();
        rementente.setNome(value);
        this.documento.setRemetente(rementente);

        return this;
    }

    /**
     * Atualiza o novo valor de interessados.
     * 
     * @param interessados
     *            o(a) interessados.
     * @return o valor de documento builder
     */
    public DocumentoBuilder setInteressados(String... interessados) {
        ArrayOfInteressado arrayOfInteressado = new ArrayOfInteressado();
        this.documento.setInteressados(arrayOfInteressado);

        if (interessados != null) {
            for (String nome : interessados) {
                addInteressado(nome);
            }

        }
        return this;
    }

    /**
     * Adiciona o interessado.
     * 
     * @param interessado
     *            o(a) interessado.
     * @return o valor de documento builder
     */
    public DocumentoBuilder addInteressado(String interessado) {
        if (this.documento.getInteressados() == null) {
            this.documento.setInteressados(new ArrayOfInteressado());
        }

        Interessado e = new Interessado();
        e.setNome(interessado);
        documento.getInteressados().getItem().add(e);
        return this;
    }

    /**
     * Atualiza o novo valor de destinatarios.
     * 
     * @param destinatarios
     *            o(a) destinatarios.
     * @return o valor de documento builder
     */
    public DocumentoBuilder setDestinatarios(List<Destinatario> destinatarios) {
        ArrayOfDestinatario arrayOfDestinatario = new ArrayOfDestinatario();
        this.documento.setDestinatarios(arrayOfDestinatario);
        this.documento.getDestinatarios().getItem().addAll(destinatarios);
        return this;
    }

    /**
     * Adiciona o destinatario.
     *
     * @param sigla
     *            o(a) sigla.
     * @param nome
     *            o(a) nome.
     * @return o valor de documento builder
     */
    public DocumentoBuilder addDestinatario(String sigla, String nome) {
        if (this.documento.getDestinatarios() == null) {
            this.documento.setDestinatarios(new ArrayOfDestinatario());
        }

        Destinatario destinatario = new Destinatario();
        destinatario.setNome(nome);
        destinatario.setSigla(sigla);
        documento.getDestinatarios().getItem().add(destinatario);
        return this;
    }

    /**
     * Atualiza o novo valor de observacao.
     * 
     * @param value
     *            o(a) value.
     * @return o valor de documento builder
     */
    public DocumentoBuilder setObservacao(String value) {
        this.documento.setObservacao(value);
        return this;
    }

    /**
     * Atualiza o novo valor de nome arquivo.
     * 
     * @param value
     *            o(a) value.
     * @return o valor de documento builder
     */
    public DocumentoBuilder setNomeArquivo(String value) {
        this.documento.setNomeArquivo(value);
        return this;
    }

    /**
     * Atualiza o novo valor de conteudo.
     * 
     * @param value
     *            o(a) value.
     * @return o valor de documento builder
     */
    public DocumentoBuilder setConteudo(String value) {
        try {
            // Ao gerar o cliente do SEI, o tipo do campo conteudo deve
            // ser alterado para DataHandler
            this.documento.setConteudoMTOM(new DataHandler(TempFileUtils.transferToTempFile(value).toURI().toURL()));
        } catch (Exception e) {
            logger.error(ERRO_CARREGAR_ARQUIVO, e);
            throw SingularServerException.rethrow(ERRO_CARREGAR_ARQUIVO, e);
        }
        return this;
    }
    /**
     * Atualiza o novo valor de conteudo.
     * 
     * @param arquivo
     *            o(a) arquivo.
     * @return o valor de documento builder
     */
    public DocumentoBuilder setConteudo(File arquivo) {
        try {
            // Ao gerar o cliente do SEI, o tipo do campo conteudo deve
            // ser alterado para DataHandler
            this.documento.setConteudoMTOM(new DataHandler(arquivo.toURI().toURL()));
        } catch (Exception e) {
            logger.error(ERRO_CARREGAR_ARQUIVO, e);
            throw SingularServerException.rethrow(ERRO_CARREGAR_ARQUIVO, e);
        }
        return this;
    }

    /**
     * Atualiza o novo valor de conteudo.
     * 
     * @param arquivo
     *            o(a) arquivo.
     * @return o valor de documento builder
     */
    public DocumentoBuilder setConteudo(InputStream arquivo) {
        try {
            // Ao gerar o cliente do SEI, o tipo do campo conteudo deve
            // ser alterado para DataHandler
            this.documento.setConteudoMTOM(new DataHandler(TempFileUtils.stream2file(arquivo).toURI().toURL()));
        } catch (Exception e) {
            logger.error(ERRO_CARREGAR_ARQUIVO, e);
            throw SingularServerException.rethrow(ERRO_CARREGAR_ARQUIVO, e);
        }
        return this;
    }

    /**
     * Atualiza o novo valor de conteudo, quando o array de bytes é proveniente
     * de um arquivo binário.
     * 
     * @param binaryData
     *            o(a) binary data.
     * @return o valor de documento builder
     */
    public DocumentoBuilder setConteudoBinario(byte[] binaryData) {
        try {
            // Ao gerar o cliente do SEI, o tipo do campo conteudo deve
            // ser alterado para DataHandler
            this.documento.setConteudoMTOM(new DataHandler(TempFileUtils.createTempFile(binaryData).toURI().toURL()));
        } catch (Exception e) {
            logger.error(ERRO_CARREGAR_ARQUIVO, e);
            throw SingularServerException.rethrow(ERRO_CARREGAR_ARQUIVO, e);
        }
        return this;
    }

    /**
     * Atualiza o novo valor de conteudo, quando o array de bytes é proveniente
     * de um arquivo textual (HTML, por exemplo).
     * 
     * @param binaryData
     *            o(a) binary data.
     * @return o valor de documento builder
     */
    public DocumentoBuilder setConteudoTextual(byte[] binaryData) {
        try {
            // Ao gerar o cliente do SEI, o tipo do campo conteudo deve
            // ser alterado para DataHandler
            this.documento.setConteudoMTOM(new DataHandler(TempFileUtils.decodeToTempFile(new String(binaryData, Charset.forName("UTF-8"))).toURI().toURL()));
        } catch (Exception e) {
            logger.error(ERRO_CARREGAR_ARQUIVO, e);
            throw SingularServerException.rethrow(ERRO_CARREGAR_ARQUIVO, e);
        }
        return this;
    }

    /**
     * Atualiza o novo valor de conteudo mtom.
     * 
     * @param value
     *            o(a) value.
     * @return o valor de documento builder
     */
    public DocumentoBuilder setConteudo(DataHandler value) {
        // Ao gerar o cliente do SEI, o tipo do campo conteudo deve
        // ser alterado para DataHandler
        this.documento.setConteudoMTOM(value);
        return this;
    }

    /**
     * Atualiza o novo valor de nivel acesso.
     * 
     * @param nivelAcesso
     *            o(a) nivel acesso.
     * @return o valor de documento builder
     */
    public DocumentoBuilder setNivelAcesso(NivelAcesso nivelAcesso) {
        this.documento.setNivelAcesso(nivelAcesso.getCodigo());
        return this;
    }

    public DocumentoBuilder setIdArquivo(String idArquivo) {
        this.documento.setIdArquivo(idArquivo);
        return this;
    }

    /**
     * Cria o documento.
     * 
     * @return o valor de documento
     */
    public Documento createDocumento() {
        definirValoresDefault();
        return documento;
    }

    /**
     * Define valores default, em campos opcionais que estejam nulos.
     */
    private void definirValoresDefault() {
        if (this.documento.getDestinatarios() == null) {
            this.documento.setDestinatarios(new ArrayOfDestinatario());
        }

        if (this.documento.getInteressados() == null) {
            this.documento.setInteressados(new ArrayOfInteressado());
        }

        if (this.documento.getNivelAcesso() == null) {
            this.documento.setNivelAcesso(NivelAcesso.PUBLICO.getCodigo());
        }

        if (this.documento.getObservacao() == null) {
            this.documento.setObservacao("");
        }
    }

}
