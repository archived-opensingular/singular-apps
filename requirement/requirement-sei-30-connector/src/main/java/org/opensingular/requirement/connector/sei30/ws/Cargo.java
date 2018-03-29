
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

package org.opensingular.requirement.connector.sei30.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de Cargo complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="Cargo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="IdCargo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ExpressaoCargo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ExpressaoTratamento" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ExpressaoVocativo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Cargo", propOrder = {

})
public class Cargo {

    @XmlElement(name = "IdCargo", required = true)
    protected String idCargo;
    @XmlElement(name = "ExpressaoCargo", required = true)
    protected String expressaoCargo;
    @XmlElement(name = "ExpressaoTratamento", required = true)
    protected String expressaoTratamento;
    @XmlElement(name = "ExpressaoVocativo", required = true)
    protected String expressaoVocativo;

    /**
     * Obtém o valor da propriedade idCargo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdCargo() {
        return idCargo;
    }

    /**
     * Define o valor da propriedade idCargo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdCargo(String value) {
        this.idCargo = value;
    }

    /**
     * Obtém o valor da propriedade expressaoCargo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExpressaoCargo() {
        return expressaoCargo;
    }

    /**
     * Define o valor da propriedade expressaoCargo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExpressaoCargo(String value) {
        this.expressaoCargo = value;
    }

    /**
     * Obtém o valor da propriedade expressaoTratamento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExpressaoTratamento() {
        return expressaoTratamento;
    }

    /**
     * Define o valor da propriedade expressaoTratamento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExpressaoTratamento(String value) {
        this.expressaoTratamento = value;
    }

    /**
     * Obtém o valor da propriedade expressaoVocativo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExpressaoVocativo() {
        return expressaoVocativo;
    }

    /**
     * Define o valor da propriedade expressaoVocativo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExpressaoVocativo(String value) {
        this.expressaoVocativo = value;
    }

}
