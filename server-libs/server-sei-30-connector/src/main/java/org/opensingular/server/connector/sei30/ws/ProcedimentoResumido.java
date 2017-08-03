
package org.opensingular.server.connector.sei30.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de ProcedimentoResumido complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="ProcedimentoResumido">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="IdProcedimento" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ProcedimentoFormatado" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="TipoProcedimento" type="{Sei}TipoProcedimento"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ProcedimentoResumido", propOrder = {

})
public class ProcedimentoResumido {

    @XmlElement(name = "IdProcedimento", required = true)
    protected String idProcedimento;
    @XmlElement(name = "ProcedimentoFormatado", required = true)
    protected String procedimentoFormatado;
    @XmlElement(name = "TipoProcedimento", required = true)
    protected TipoProcedimento tipoProcedimento;

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
     * Obtém o valor da propriedade tipoProcedimento.
     * 
     * @return
     *     possible object is
     *     {@link TipoProcedimento }
     *     
     */
    public TipoProcedimento getTipoProcedimento() {
        return tipoProcedimento;
    }

    /**
     * Define o valor da propriedade tipoProcedimento.
     * 
     * @param value
     *     allowed object is
     *     {@link TipoProcedimento }
     *     
     */
    public void setTipoProcedimento(TipoProcedimento value) {
        this.tipoProcedimento = value;
    }

}
