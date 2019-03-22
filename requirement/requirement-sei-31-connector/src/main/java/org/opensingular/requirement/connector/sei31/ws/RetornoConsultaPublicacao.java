
package org.opensingular.requirement.connector.sei31.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de RetornoConsultaPublicacao complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="RetornoConsultaPublicacao">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="Publicacao" type="{Sei}Publicacao"/>
 *         &lt;element name="Andamento" type="{Sei}Andamento"/>
 *         &lt;element name="Assinaturas" type="{Sei}ArrayOfAssinatura"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RetornoConsultaPublicacao", propOrder = {

})
public class RetornoConsultaPublicacao {

    @XmlElement(name = "Publicacao", required = true)
    protected Publicacao publicacao;
    @XmlElement(name = "Andamento", required = true)
    protected Andamento andamento;
    @XmlElement(name = "Assinaturas", required = true)
    protected ArrayOfAssinatura assinaturas;

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
     * Obtém o valor da propriedade andamento.
     * 
     * @return
     *     possible object is
     *     {@link Andamento }
     *     
     */
    public Andamento getAndamento() {
        return andamento;
    }

    /**
     * Define o valor da propriedade andamento.
     * 
     * @param value
     *     allowed object is
     *     {@link Andamento }
     *     
     */
    public void setAndamento(Andamento value) {
        this.andamento = value;
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

}
