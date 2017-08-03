
package org.opensingular.server.connector.sei30.ws;

import javax.activation.DataHandler;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlMimeType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de Documento complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="Documento">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="Tipo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="IdProcedimento" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ProtocoloProcedimento" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="IdSerie" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Numero" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Data" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Descricao" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="IdTipoConferencia" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Remetente" type="{Sei}Remetente" minOccurs="0"/>
 *         &lt;element name="Interessados" type="{Sei}ArrayOfInteressado" minOccurs="0"/>
 *         &lt;element name="Destinatarios" type="{Sei}ArrayOfDestinatario" minOccurs="0"/>
 *         &lt;element name="Observacao" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NomeArquivo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NivelAcesso" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="IdHipoteseLegal" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Conteudo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ConteudoMTOM" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="IdArquivo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Campos" type="{Sei}ArrayOfCampo" minOccurs="0"/>
 *         &lt;element name="SinBloqueado" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Documento", propOrder = {

})
public class Documento {

    @XmlElement(name = "Tipo", required = true)
    protected String tipo;
    @XmlElement(name = "IdProcedimento")
    protected String idProcedimento;
    @XmlElement(name = "ProtocoloProcedimento")
    protected String protocoloProcedimento;
    @XmlElement(name = "IdSerie", required = true)
    protected String idSerie;
    @XmlElement(name = "Numero")
    protected String numero;
    @XmlElement(name = "Data")
    protected String data;
    @XmlElement(name = "Descricao")
    protected String descricao;
    @XmlElement(name = "IdTipoConferencia")
    protected String idTipoConferencia;
    @XmlElement(name = "Remetente")
    protected Remetente remetente;
    @XmlElement(name = "Interessados")
    protected ArrayOfInteressado interessados;
    @XmlElement(name = "Destinatarios")
    protected ArrayOfDestinatario destinatarios;
    @XmlElement(name = "Observacao")
    protected String observacao;
    @XmlElement(name = "NomeArquivo")
    protected String nomeArquivo;
    @XmlElement(name = "NivelAcesso")
    protected String nivelAcesso;
    @XmlElement(name = "IdHipoteseLegal")
    protected String idHipoteseLegal;
    @XmlElement(name = "Conteudo")
    @XmlMimeType("application/octet-stream")
    protected DataHandler conteudo;
    @XmlElement(name = "ConteudoMTOM")
    @XmlMimeType("application/octet-stream")
    protected DataHandler conteudoMTOM;
    @XmlElement(name = "IdArquivo")
    protected String idArquivo;
    @XmlElement(name = "Campos")
    protected ArrayOfCampo campos;
    @XmlElement(name = "SinBloqueado")
    protected String sinBloqueado;

    /**
     * Obtém o valor da propriedade tipo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * Define o valor da propriedade tipo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipo(String value) {
        this.tipo = value;
    }

    /**
     * Obtém o valor da propriedade idProcedimento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdProcedimento() {
        return idProcedimento;
    }

    /**
     * Define o valor da propriedade idProcedimento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdProcedimento(String value) {
        this.idProcedimento = value;
    }

    /**
     * Obtém o valor da propriedade protocoloProcedimento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProtocoloProcedimento() {
        return protocoloProcedimento;
    }

    /**
     * Define o valor da propriedade protocoloProcedimento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProtocoloProcedimento(String value) {
        this.protocoloProcedimento = value;
    }

    /**
     * Obtém o valor da propriedade idSerie.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdSerie() {
        return idSerie;
    }

    /**
     * Define o valor da propriedade idSerie.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdSerie(String value) {
        this.idSerie = value;
    }

    /**
     * Obtém o valor da propriedade numero.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumero() {
        return numero;
    }

    /**
     * Define o valor da propriedade numero.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumero(String value) {
        this.numero = value;
    }

    /**
     * Obtém o valor da propriedade data.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getData() {
        return data;
    }

    /**
     * Define o valor da propriedade data.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setData(String value) {
        this.data = value;
    }

    /**
     * Obtém o valor da propriedade descricao.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * Define o valor da propriedade descricao.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescricao(String value) {
        this.descricao = value;
    }

    /**
     * Obtém o valor da propriedade idTipoConferencia.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdTipoConferencia() {
        return idTipoConferencia;
    }

    /**
     * Define o valor da propriedade idTipoConferencia.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdTipoConferencia(String value) {
        this.idTipoConferencia = value;
    }

    /**
     * Obtém o valor da propriedade remetente.
     * 
     * @return
     *     possible object is
     *     {@link Remetente }
     *     
     */
    public Remetente getRemetente() {
        return remetente;
    }

    /**
     * Define o valor da propriedade remetente.
     * 
     * @param value
     *     allowed object is
     *     {@link Remetente }
     *     
     */
    public void setRemetente(Remetente value) {
        this.remetente = value;
    }

    /**
     * Obtém o valor da propriedade interessados.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfInteressado }
     *     
     */
    public ArrayOfInteressado getInteressados() {
        return interessados;
    }

    /**
     * Define o valor da propriedade interessados.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfInteressado }
     *     
     */
    public void setInteressados(ArrayOfInteressado value) {
        this.interessados = value;
    }

    /**
     * Obtém o valor da propriedade destinatarios.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfDestinatario }
     *     
     */
    public ArrayOfDestinatario getDestinatarios() {
        return destinatarios;
    }

    /**
     * Define o valor da propriedade destinatarios.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfDestinatario }
     *     
     */
    public void setDestinatarios(ArrayOfDestinatario value) {
        this.destinatarios = value;
    }

    /**
     * Obtém o valor da propriedade observacao.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getObservacao() {
        return observacao;
    }

    /**
     * Define o valor da propriedade observacao.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setObservacao(String value) {
        this.observacao = value;
    }

    /**
     * Obtém o valor da propriedade nomeArquivo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNomeArquivo() {
        return nomeArquivo;
    }

    /**
     * Define o valor da propriedade nomeArquivo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNomeArquivo(String value) {
        this.nomeArquivo = value;
    }

    /**
     * Obtém o valor da propriedade nivelAcesso.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNivelAcesso() {
        return nivelAcesso;
    }

    /**
     * Define o valor da propriedade nivelAcesso.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNivelAcesso(String value) {
        this.nivelAcesso = value;
    }

    /**
     * Obtém o valor da propriedade idHipoteseLegal.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdHipoteseLegal() {
        return idHipoteseLegal;
    }

    /**
     * Define o valor da propriedade idHipoteseLegal.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdHipoteseLegal(String value) {
        this.idHipoteseLegal = value;
    }

    /**
     * Obtém o valor da propriedade conteudo.
     * 
     * @return
     *     possible object is
     *     {@link DataHandler }
     *     
     */
    public DataHandler getConteudo() {
        return conteudo;
    }

    /**
     * Define o valor da propriedade conteudo.
     * 
     * @param value
     *     allowed object is
     *     {@link DataHandler }
     *     
     */
    public void setConteudo(DataHandler value) {
        this.conteudo = value;
    }

    /**
     * Obtém o valor da propriedade conteudoMTOM.
     * 
     * @return
     *     possible object is
     *     {@link DataHandler }
     *     
     */
    public DataHandler getConteudoMTOM() {
        return conteudoMTOM;
    }

    /**
     * Define o valor da propriedade conteudoMTOM.
     * 
     * @param value
     *     allowed object is
     *     {@link DataHandler }
     *     
     */
    public void setConteudoMTOM(DataHandler value) {
        this.conteudoMTOM = value;
    }

    /**
     * Obtém o valor da propriedade idArquivo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdArquivo() {
        return idArquivo;
    }

    /**
     * Define o valor da propriedade idArquivo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdArquivo(String value) {
        this.idArquivo = value;
    }

    /**
     * Obtém o valor da propriedade campos.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfCampo }
     *     
     */
    public ArrayOfCampo getCampos() {
        return campos;
    }

    /**
     * Define o valor da propriedade campos.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfCampo }
     *     
     */
    public void setCampos(ArrayOfCampo value) {
        this.campos = value;
    }

    /**
     * Obtém o valor da propriedade sinBloqueado.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSinBloqueado() {
        return sinBloqueado;
    }

    /**
     * Define o valor da propriedade sinBloqueado.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSinBloqueado(String value) {
        this.sinBloqueado = value;
    }

}
