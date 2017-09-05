
package org.opensingular.server.connector.sei30.extensao.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Assinante complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Assinante">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="IdAssinante" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="CargoFuncao" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Assinante", propOrder = {

})
public class Assinante {

    @XmlElement(name = "IdAssinante", required = true)
    protected String idAssinante;
    @XmlElement(name = "CargoFuncao", required = true)
    protected String cargoFuncao;

    /**
     * Gets the value of the idAssinante property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdAssinante() {
        return idAssinante;
    }

    /**
     * Sets the value of the idAssinante property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdAssinante(String value) {
        this.idAssinante = value;
    }

    /**
     * Gets the value of the cargoFuncao property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCargoFuncao() {
        return cargoFuncao;
    }

    /**
     * Sets the value of the cargoFuncao property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCargoFuncao(String value) {
        this.cargoFuncao = value;
    }

}
