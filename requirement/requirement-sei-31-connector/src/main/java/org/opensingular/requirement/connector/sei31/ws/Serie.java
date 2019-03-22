
package org.opensingular.requirement.connector.sei31.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de Serie complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="Serie">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="IdSerie" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Nome" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Aplicabilidade" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Serie", propOrder = {

})
public class Serie {

    @XmlElement(name = "IdSerie", required = true)
    protected String idSerie;
    @XmlElement(name = "Nome", required = true)
    protected String nome;
    @XmlElement(name = "Aplicabilidade")
    protected String aplicabilidade;

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
     * Obtém o valor da propriedade nome.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNome() {
        return nome;
    }

    /**
     * Define o valor da propriedade nome.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNome(String value) {
        this.nome = value;
    }

    /**
     * Obtém o valor da propriedade aplicabilidade.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAplicabilidade() {
        return aplicabilidade;
    }

    /**
     * Define o valor da propriedade aplicabilidade.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAplicabilidade(String value) {
        this.aplicabilidade = value;
    }

}
