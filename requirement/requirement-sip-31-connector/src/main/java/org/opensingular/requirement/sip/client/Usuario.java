
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
 * <p>Java class for Usuario complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Usuario">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="StaOperacao" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="IdOrigem" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="IdOrgao" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Sigla" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Nome" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Usuario", propOrder = {

})
public class Usuario {

    @XmlElement(name = "StaOperacao", required = true)
    protected String staOperacao;
    @XmlElement(name = "IdOrigem", required = true)
    protected String idOrigem;
    @XmlElement(name = "IdOrgao", required = true)
    protected String idOrgao;
    @XmlElement(name = "Sigla", required = true)
    protected String sigla;
    @XmlElement(name = "Nome", required = true)
    protected String nome;

    /**
     * Gets the value of the staOperacao property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStaOperacao() {
        return staOperacao;
    }

    /**
     * Sets the value of the staOperacao property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStaOperacao(String value) {
        this.staOperacao = value;
    }

    /**
     * Gets the value of the idOrigem property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdOrigem() {
        return idOrigem;
    }

    /**
     * Sets the value of the idOrigem property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdOrigem(String value) {
        this.idOrigem = value;
    }

    /**
     * Gets the value of the idOrgao property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdOrgao() {
        return idOrgao;
    }

    /**
     * Sets the value of the idOrgao property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdOrgao(String value) {
        this.idOrgao = value;
    }

    /**
     * Gets the value of the sigla property.
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
     * Sets the value of the sigla property.
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

}
