
/*
 *
 *  * Copyright (C) 2016 Singular Studios (a.k.a Atom Tecnologia) - www.opensingular.com
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  * http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package org.opensingular.server.connector.sei30.extensao.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for LoginInterno complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="LoginInterno">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="IdOrgaoUsuario" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="IdContextoUsuario" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="SiglaUsuario" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="SenhaUsuario" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LoginInterno", propOrder = {

})
public class LoginInterno {

    @XmlElement(name = "IdOrgaoUsuario")
    protected long idOrgaoUsuario;
    @XmlElement(name = "IdContextoUsuario")
    protected long idContextoUsuario;
    @XmlElement(name = "SiglaUsuario", required = true)
    protected String siglaUsuario;
    @XmlElement(name = "SenhaUsuario", required = true)
    protected String senhaUsuario;

    /**
     * Gets the value of the idOrgaoUsuario property.
     * 
     */
    public long getIdOrgaoUsuario() {
        return idOrgaoUsuario;
    }

    /**
     * Sets the value of the idOrgaoUsuario property.
     * 
     */
    public void setIdOrgaoUsuario(long value) {
        this.idOrgaoUsuario = value;
    }

    /**
     * Gets the value of the idContextoUsuario property.
     * 
     */
    public long getIdContextoUsuario() {
        return idContextoUsuario;
    }

    /**
     * Sets the value of the idContextoUsuario property.
     * 
     */
    public void setIdContextoUsuario(long value) {
        this.idContextoUsuario = value;
    }

    /**
     * Gets the value of the siglaUsuario property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSiglaUsuario() {
        return siglaUsuario;
    }

    /**
     * Sets the value of the siglaUsuario property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSiglaUsuario(String value) {
        this.siglaUsuario = value;
    }

    /**
     * Gets the value of the senhaUsuario property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSenhaUsuario() {
        return senhaUsuario;
    }

    /**
     * Sets the value of the senhaUsuario property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSenhaUsuario(String value) {
        this.senhaUsuario = value;
    }

}
