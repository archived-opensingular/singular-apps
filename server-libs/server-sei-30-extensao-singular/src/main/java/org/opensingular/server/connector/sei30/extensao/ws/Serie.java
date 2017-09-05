
package org.opensingular.server.connector.sei30.extensao.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Serie complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
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
     * Gets the value of the idSerie property.
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
     * Sets the value of the idSerie property.
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
     * Gets the value of the nome property.
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
     * Sets the value of the nome property.
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
     * Gets the value of the aplicabilidade property.
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
     * Sets the value of the aplicabilidade property.
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
