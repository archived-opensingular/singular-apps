
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

package org.opensingular.requirement.connector.sei30.extensao.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DadosUsuarioExterno complex type.
 * 
 * <p>The following schema fragment specifies the expected         content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DadosUsuarioExterno"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;all&gt;
 *         &lt;element name="NumIdUsuario" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *         &lt;element name="Nome" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="StrSigla" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="Cpf" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/all&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
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
