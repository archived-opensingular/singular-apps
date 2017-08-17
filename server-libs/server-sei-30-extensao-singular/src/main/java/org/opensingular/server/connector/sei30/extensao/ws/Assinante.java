
package org.opensingular.server.connector.sei30.extensao.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de Assinante complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
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
     * Obtém o valor da propriedade idAssinante.
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
     * Define o valor da propriedade idAssinante.
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
     * Obtém o valor da propriedade cargoFuncao.
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
     * Define o valor da propriedade cargoFuncao.
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
