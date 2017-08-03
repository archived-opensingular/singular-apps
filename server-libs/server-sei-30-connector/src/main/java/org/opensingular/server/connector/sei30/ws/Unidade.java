
package org.opensingular.server.connector.sei30.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de Unidade complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="Unidade">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="IdUnidade" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Sigla" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Descricao" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Unidade", propOrder = {

})
public class Unidade {

    @XmlElement(name = "IdUnidade", required = true)
    protected String idUnidade;
    @XmlElement(name = "Sigla", required = true)
    protected String sigla;
    @XmlElement(name = "Descricao", required = true)
    protected String descricao;

    /**
     * Obtém o valor da propriedade idUnidade.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdUnidade() {
        return idUnidade;
    }

    /**
     * Define o valor da propriedade idUnidade.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdUnidade(String value) {
        this.idUnidade = value;
    }

    /**
     * Obtém o valor da propriedade sigla.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSigla() {
        return sigla;
    }

    /**
     * Define o valor da propriedade sigla.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSigla(String value) {
        this.sigla = value;
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

}
