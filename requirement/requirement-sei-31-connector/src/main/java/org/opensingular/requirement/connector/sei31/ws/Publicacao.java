
package org.opensingular.requirement.connector.sei31.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de Publicacao complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="Publicacao">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="IdPublicacao" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="IdDocumento" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="StaMotivo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Resumo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="IdVeiculoPublicacao" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NomeVeiculo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="StaTipoVeiculo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Numero" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="DataDisponibilizacao" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="DataPublicacao" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Estado" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ImprensaNacional" type="{Sei}PublicacaoImprensaNacional"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Publicacao", propOrder = {

})
public class Publicacao {

    @XmlElement(name = "IdPublicacao")
    protected String idPublicacao;
    @XmlElement(name = "IdDocumento")
    protected String idDocumento;
    @XmlElement(name = "StaMotivo")
    protected String staMotivo;
    @XmlElement(name = "Resumo")
    protected String resumo;
    @XmlElement(name = "IdVeiculoPublicacao")
    protected String idVeiculoPublicacao;
    @XmlElement(name = "NomeVeiculo", required = true)
    protected String nomeVeiculo;
    @XmlElement(name = "StaTipoVeiculo")
    protected String staTipoVeiculo;
    @XmlElement(name = "Numero", required = true)
    protected String numero;
    @XmlElement(name = "DataDisponibilizacao", required = true)
    protected String dataDisponibilizacao;
    @XmlElement(name = "DataPublicacao", required = true)
    protected String dataPublicacao;
    @XmlElement(name = "Estado", required = true)
    protected String estado;
    @XmlElement(name = "ImprensaNacional", required = true)
    protected PublicacaoImprensaNacional imprensaNacional;

    /**
     * Obtém o valor da propriedade idPublicacao.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdPublicacao() {
        return idPublicacao;
    }

    /**
     * Define o valor da propriedade idPublicacao.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdPublicacao(String value) {
        this.idPublicacao = value;
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
     * Obtém o valor da propriedade staMotivo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStaMotivo() {
        return staMotivo;
    }

    /**
     * Define o valor da propriedade staMotivo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStaMotivo(String value) {
        this.staMotivo = value;
    }

    /**
     * Obtém o valor da propriedade resumo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResumo() {
        return resumo;
    }

    /**
     * Define o valor da propriedade resumo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResumo(String value) {
        this.resumo = value;
    }

    /**
     * Obtém o valor da propriedade idVeiculoPublicacao.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdVeiculoPublicacao() {
        return idVeiculoPublicacao;
    }

    /**
     * Define o valor da propriedade idVeiculoPublicacao.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdVeiculoPublicacao(String value) {
        this.idVeiculoPublicacao = value;
    }

    /**
     * Obtém o valor da propriedade nomeVeiculo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNomeVeiculo() {
        return nomeVeiculo;
    }

    /**
     * Define o valor da propriedade nomeVeiculo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNomeVeiculo(String value) {
        this.nomeVeiculo = value;
    }

    /**
     * Obtém o valor da propriedade staTipoVeiculo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStaTipoVeiculo() {
        return staTipoVeiculo;
    }

    /**
     * Define o valor da propriedade staTipoVeiculo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStaTipoVeiculo(String value) {
        this.staTipoVeiculo = value;
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
     * Obtém o valor da propriedade dataDisponibilizacao.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDataDisponibilizacao() {
        return dataDisponibilizacao;
    }

    /**
     * Define o valor da propriedade dataDisponibilizacao.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataDisponibilizacao(String value) {
        this.dataDisponibilizacao = value;
    }

    /**
     * Obtém o valor da propriedade dataPublicacao.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDataPublicacao() {
        return dataPublicacao;
    }

    /**
     * Define o valor da propriedade dataPublicacao.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataPublicacao(String value) {
        this.dataPublicacao = value;
    }

    /**
     * Obtém o valor da propriedade estado.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEstado() {
        return estado;
    }

    /**
     * Define o valor da propriedade estado.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEstado(String value) {
        this.estado = value;
    }

    /**
     * Obtém o valor da propriedade imprensaNacional.
     * 
     * @return
     *     possible object is
     *     {@link PublicacaoImprensaNacional }
     *     
     */
    public PublicacaoImprensaNacional getImprensaNacional() {
        return imprensaNacional;
    }

    /**
     * Define o valor da propriedade imprensaNacional.
     * 
     * @param value
     *     allowed object is
     *     {@link PublicacaoImprensaNacional }
     *     
     */
    public void setImprensaNacional(PublicacaoImprensaNacional value) {
        this.imprensaNacional = value;
    }

}
