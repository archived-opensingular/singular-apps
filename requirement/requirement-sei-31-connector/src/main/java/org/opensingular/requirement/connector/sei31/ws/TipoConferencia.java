
package org.opensingular.requirement.connector.sei31.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de TipoConferencia complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="TipoConferencia">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="IdTipoConferencia" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
@XmlType(name = "TipoConferencia", propOrder = {

})
public class TipoConferencia {

    @XmlElement(name = "IdTipoConferencia", required = true)
    protected String idTipoConferencia;
    @XmlElement(name = "Descricao", required = true)
    protected String descricao;

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
