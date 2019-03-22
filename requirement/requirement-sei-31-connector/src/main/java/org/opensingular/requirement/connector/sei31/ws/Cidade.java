
package org.opensingular.requirement.connector.sei31.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de Cidade complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="Cidade">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="IdCidade" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="IdEstado" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="IdPais" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Nome" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="CodigoIbge" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="SinCapital" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Latitude" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Longitude" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Cidade", propOrder = {

})
public class Cidade {

    @XmlElement(name = "IdCidade", required = true)
    protected String idCidade;
    @XmlElement(name = "IdEstado", required = true)
    protected String idEstado;
    @XmlElement(name = "IdPais", required = true)
    protected String idPais;
    @XmlElement(name = "Nome", required = true)
    protected String nome;
    @XmlElement(name = "CodigoIbge", required = true)
    protected String codigoIbge;
    @XmlElement(name = "SinCapital", required = true)
    protected String sinCapital;
    @XmlElement(name = "Latitude", required = true)
    protected String latitude;
    @XmlElement(name = "Longitude", required = true)
    protected String longitude;

    /**
     * Obtém o valor da propriedade idCidade.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdCidade() {
        return idCidade;
    }

    /**
     * Define o valor da propriedade idCidade.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdCidade(String value) {
        this.idCidade = value;
    }

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

    /**
     * Obtém o valor da propriedade sinCapital.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSinCapital() {
        return sinCapital;
    }

    /**
     * Define o valor da propriedade sinCapital.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSinCapital(String value) {
        this.sinCapital = value;
    }

    /**
     * Obtém o valor da propriedade latitude.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLatitude() {
        return latitude;
    }

    /**
     * Define o valor da propriedade latitude.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLatitude(String value) {
        this.latitude = value;
    }

    /**
     * Obtém o valor da propriedade longitude.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLongitude() {
        return longitude;
    }

    /**
     * Define o valor da propriedade longitude.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLongitude(String value) {
        this.longitude = value;
    }

}
