
package org.opensingular.server.connector.sei30.extensao.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DadosUsuarioExterno complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DadosUsuarioExterno">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="NumIdUsuario" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="Nome" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="StrSigla" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Cpf" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DadosUsuarioExterno", propOrder = {

})
public class DadosUsuarioExterno {

    @XmlElement(name = "NumIdUsuario")
    protected long numIdUsuario;
    @XmlElement(name = "Nome", required = true)
    protected String nome;
    @XmlElement(name = "StrSigla", required = true)
    protected String strSigla;
    @XmlElement(name = "Cpf", required = true)
    protected String cpf;

    /**
     * Gets the value of the numIdUsuario property.
     * 
     */
    public long getNumIdUsuario() {
        return numIdUsuario;
    }

    /**
     * Sets the value of the numIdUsuario property.
     * 
     */
    public void setNumIdUsuario(long value) {
        this.numIdUsuario = value;
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
     * Gets the value of the strSigla property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStrSigla() {
        return strSigla;
    }

    /**
     * Sets the value of the strSigla property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStrSigla(String value) {
        this.strSigla = value;
    }

    /**
     * Gets the value of the cpf property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCpf() {
        return cpf;
    }

    /**
     * Sets the value of the cpf property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCpf(String value) {
        this.cpf = value;
    }

}
