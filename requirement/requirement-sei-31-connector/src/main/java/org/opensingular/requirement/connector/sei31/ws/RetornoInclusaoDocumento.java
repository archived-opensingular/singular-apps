
package org.opensingular.requirement.connector.sei31.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de RetornoInclusaoDocumento complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="RetornoInclusaoDocumento">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="IdDocumento" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="DocumentoFormatado" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="LinkAcesso" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RetornoInclusaoDocumento", propOrder = {

})
public class RetornoInclusaoDocumento {

    @XmlElement(name = "IdDocumento", required = true)
    protected String idDocumento;
    @XmlElement(name = "DocumentoFormatado", required = true)
    protected String documentoFormatado;
    @XmlElement(name = "LinkAcesso", required = true)
    protected String linkAcesso;

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

}
