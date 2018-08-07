
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

package org.opensingular.requirement.connector.sei30.extensao.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Assinante complex type.
 * 
 * <p>The following schema fragment specifies the expected         content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Assinante"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;all&gt;
 *         &lt;element name="IdAssinante" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="CargoFuncao" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/all&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Assinante", propOrder = {

})
public class Assinante {

    @XmlElement(name = "IdAssinante", required = true)
    protected String idAssinante;
    @XmlElement(name = "CargoFuncao", required = true)
    protected String cargoFuncao;

    /**
     * Gets the value of the idAssinante property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdAssinante() {
        return idAssinante;
    }

    /**
     * Sets the value of the idAssinante property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdAssinante(String value) {
        this.idAssinante = value;
    }

    /**
     * Gets the value of the cargoFuncao property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCargoFuncao() {
        return cargoFuncao;
    }

    /**
     * Sets the value of the cargoFuncao property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCargoFuncao(String value) {
        this.cargoFuncao = value;
    }

}
