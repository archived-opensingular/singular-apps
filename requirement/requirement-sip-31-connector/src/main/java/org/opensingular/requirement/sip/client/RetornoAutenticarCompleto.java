
/*
 * Copyright (C) 2016 Singular Studios (a.k.a Atom Tecnologia) - www.opensingular.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.opensingular.requirement.sip.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RetornoAutenticarCompleto complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RetornoAutenticarCompleto">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="IdSistema" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="IdContexto" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="IdUsuario" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="IdLogin" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="HashAgente" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RetornoAutenticarCompleto", propOrder = {

})
public class RetornoAutenticarCompleto {

    @XmlElement(name = "IdSistema")
    protected long idSistema;
    @XmlElement(name = "IdContexto")
    protected long idContexto;
    @XmlElement(name = "IdUsuario")
    protected long idUsuario;
    @XmlElement(name = "IdLogin", required = true)
    protected String idLogin;
    @XmlElement(name = "HashAgente", required = true)
    protected String hashAgente;

    /**
     * Gets the value of the idSistema property.
     * 
     */
    public long getIdSistema() {
        return idSistema;
    }

    /**
     * Sets the value of the idSistema property.
     * 
     */
    public void setIdSistema(long value) {
        this.idSistema = value;
    }

    /**
     * Gets the value of the idContexto property.
     * 
     */
    public long getIdContexto() {
        return idContexto;
    }

    /**
     * Sets the value of the idContexto property.
     * 
     */
    public void setIdContexto(long value) {
        this.idContexto = value;
    }

    /**
     * Gets the value of the idUsuario property.
     * 
     */
    public long getIdUsuario() {
        return idUsuario;
    }

    /**
     * Sets the value of the idUsuario property.
     * 
     */
    public void setIdUsuario(long value) {
        this.idUsuario = value;
    }

    /**
     * Gets the value of the idLogin property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdLogin() {
        return idLogin;
    }

    /**
     * Sets the value of the idLogin property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdLogin(String value) {
        this.idLogin = value;
    }

    /**
     * Gets the value of the hashAgente property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHashAgente() {
        return hashAgente;
    }

    /**
     * Sets the value of the hashAgente property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHashAgente(String value) {
        this.hashAgente = value;
    }

}
