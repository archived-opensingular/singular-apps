
package org.opensingular.requirement.connector.sei31.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de UnidadeProcedimentoAberto complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="UnidadeProcedimentoAberto">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="Unidade" type="{Sei}Unidade"/>
 *         &lt;element name="UsuarioAtribuicao" type="{Sei}Usuario"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UnidadeProcedimentoAberto", propOrder = {

})
public class UnidadeProcedimentoAberto {

    @XmlElement(name = "Unidade", required = true)
    protected Unidade unidade;
    @XmlElement(name = "UsuarioAtribuicao", required = true)
    protected Usuario usuarioAtribuicao;

    /**
     * Obtém o valor da propriedade unidade.
     * 
     * @return
     *     possible object is
     *     {@link Unidade }
     *     
     */
    public Unidade getUnidade() {
        return unidade;
    }

    /**
     * Define o valor da propriedade unidade.
     * 
     * @param value
     *     allowed object is
     *     {@link Unidade }
     *     
     */
    public void setUnidade(Unidade value) {
        this.unidade = value;
    }

    /**
     * Obtém o valor da propriedade usuarioAtribuicao.
     * 
     * @return
     *     possible object is
     *     {@link Usuario }
     *     
     */
    public Usuario getUsuarioAtribuicao() {
        return usuarioAtribuicao;
    }

    /**
     * Define o valor da propriedade usuarioAtribuicao.
     * 
     * @param value
     *     allowed object is
     *     {@link Usuario }
     *     
     */
    public void setUsuarioAtribuicao(Usuario value) {
        this.usuarioAtribuicao = value;
    }

}
