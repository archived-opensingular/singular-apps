
package org.opensingular.requirement.connector.sei31.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de Estado complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="Estado">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="IdEstado" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="IdPais" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Sigla" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Nome" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="CodigoIbge" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Estado", propOrder = {

})
public class Estado {

    @XmlElement(name = "IdEstado", required = true)
    protected String idEstado;
    @XmlElement(name = "IdPais", required = true)
    protected String idPais;
    @XmlElement(name = "Sigla", required = true)
    protected String sigla;
    @XmlElement(name = "Nome", required = true)
    protected String nome;
    @XmlElement(name = "CodigoIbge", required = true)
    protected String codigoIbge;

    /**
     * Obtém o valor da propriedade idEstado.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdEstado() {
        return idEstado;
    }

    /**
     * Define o valor da propriedade idEstado.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdEstado(String value) {
        this.idEstado = value;
    }

    /**
     * Obtém o valor da propriedade idPais.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdPais() {
        return idPais;
    }

    /**
     * Define o valor da propriedade idPais.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdPais(String value) {
        this.idPais = value;
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
     * Obtém o valor da propriedade codigoIbge.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoIbge() {
        return codigoIbge;
    }

    /**
     * Define o valor da propriedade codigoIbge.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoIbge(String value) {
        this.codigoIbge = value;
    }

}
