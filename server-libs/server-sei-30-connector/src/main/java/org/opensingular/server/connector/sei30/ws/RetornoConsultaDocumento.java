
package org.opensingular.server.connector.sei30.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de RetornoConsultaDocumento complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="RetornoConsultaDocumento">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="IdProcedimento" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ProcedimentoFormatado" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="IdDocumento" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="DocumentoFormatado" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="LinkAcesso" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Serie" type="{Sei}Serie"/>
 *         &lt;element name="Numero" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Data" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="UnidadeElaboradora" type="{Sei}Unidade"/>
 *         &lt;element name="AndamentoGeracao" type="{Sei}Andamento"/>
 *         &lt;element name="Assinaturas" type="{Sei}ArrayOfAssinatura"/>
 *         &lt;element name="Publicacao" type="{Sei}Publicacao"/>
 *         &lt;element name="Campos" type="{Sei}ArrayOfCampo"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RetornoConsultaDocumento", propOrder = {

})
public class RetornoConsultaDocumento {

    @XmlElement(name = "IdProcedimento", required = true)
    protected String idProcedimento;
    @XmlElement(name = "ProcedimentoFormatado", required = true)
    protected String procedimentoFormatado;
    @XmlElement(name = "IdDocumento", required = true)
    protected String idDocumento;
    @XmlElement(name = "DocumentoFormatado", required = true)
    protected String documentoFormatado;
    @XmlElement(name = "LinkAcesso", required = true)
    protected String linkAcesso;
    @XmlElement(name = "Serie", required = true)
    protected Serie serie;
    @XmlElement(name = "Numero", required = true)
    protected String numero;
    @XmlElement(name = "Data", required = true)
    protected String data;
    @XmlElement(name = "UnidadeElaboradora", required = true)
    protected Unidade unidadeElaboradora;
    @XmlElement(name = "AndamentoGeracao", required = true)
    protected Andamento andamentoGeracao;
    @XmlElement(name = "Assinaturas", required = true)
    protected ArrayOfAssinatura assinaturas;
    @XmlElement(name = "Publicacao", required = true)
    protected Publicacao publicacao;
    @XmlElement(name = "Campos", required = true)
    protected ArrayOfCampo campos;

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
     * Obtém o valor da propriedade procedimentoFormatado.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProcedimentoFormatado() {
        return procedimentoFormatado;
    }

    /**
     * Define o valor da propriedade procedimentoFormatado.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProcedimentoFormatado(String value) {
        this.procedimentoFormatado = value;
    }

    /**
     * Obtém o valor da propriedade idDocumento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdDocumento() {
        return idDocumento;
    }

    /**
     * Define o valor da propriedade idDocumento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdDocumento(String value) {
        this.idDocumento = value;
    }

    /**
     * Obtém o valor da propriedade documentoFormatado.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDocumentoFormatado() {
        return documentoFormatado;
    }

    /**
     * Define o valor da propriedade documentoFormatado.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDocumentoFormatado(String value) {
        this.documentoFormatado = value;
    }

    /**
     * Obtém o valor da propriedade linkAcesso.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLinkAcesso() {
        return linkAcesso;
    }

    /**
     * Define o valor da propriedade linkAcesso.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLinkAcesso(String value) {
        this.linkAcesso = value;
    }

    /**
     * Obtém o valor da propriedade serie.
     * 
     * @return
     *     possible object is
     *     {@link Serie }
     *     
     */
    public Serie getSerie() {
        return serie;
    }

    /**
     * Define o valor da propriedade serie.
     * 
     * @param value
     *     allowed object is
     *     {@link Serie }
     *     
     */
    public void setSerie(Serie value) {
        this.serie = value;
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
     * Obtém o valor da propriedade unidadeElaboradora.
     * 
     * @return
     *     possible object is
     *     {@link Unidade }
     *     
     */
    public Unidade getUnidadeElaboradora() {
        return unidadeElaboradora;
    }

    /**
     * Define o valor da propriedade unidadeElaboradora.
     * 
     * @param value
     *     allowed object is
     *     {@link Unidade }
     *     
     */
    public void setUnidadeElaboradora(Unidade value) {
        this.unidadeElaboradora = value;
    }

    /**
     * Obtém o valor da propriedade andamentoGeracao.
     * 
     * @return
     *     possible object is
     *     {@link Andamento }
     *     
     */
    public Andamento getAndamentoGeracao() {
        return andamentoGeracao;
    }

    /**
     * Define o valor da propriedade andamentoGeracao.
     * 
     * @param value
     *     allowed object is
     *     {@link Andamento }
     *     
     */
    public void setAndamentoGeracao(Andamento value) {
        this.andamentoGeracao = value;
    }

    /**
     * Obtém o valor da propriedade assinaturas.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfAssinatura }
     *     
     */
    public ArrayOfAssinatura getAssinaturas() {
        return assinaturas;
    }

    /**
     * Define o valor da propriedade assinaturas.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfAssinatura }
     *     
     */
    public void setAssinaturas(ArrayOfAssinatura value) {
        this.assinaturas = value;
    }

    /**
     * Obtém o valor da propriedade publicacao.
     * 
     * @return
     *     possible object is
     *     {@link Publicacao }
     *     
     */
    public Publicacao getPublicacao() {
        return publicacao;
    }

    /**
     * Define o valor da propriedade publicacao.
     * 
     * @param value
     *     allowed object is
     *     {@link Publicacao }
     *     
     */
    public void setPublicacao(Publicacao value) {
        this.publicacao = value;
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

}
