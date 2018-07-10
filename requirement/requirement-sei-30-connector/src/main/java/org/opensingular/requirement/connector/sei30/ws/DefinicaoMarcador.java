
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

package org.opensingular.requirement.connector.sei30.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de DefinicaoMarcador complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="DefinicaoMarcador">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="ProtocoloProcedimento" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="IdMarcador" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Texto" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DefinicaoMarcador", propOrder = {

})
public class DefinicaoMarcador {

    @XmlElement(name = "ProtocoloProcedimento", required = true)
    protected String protocoloProcedimento;
    @XmlElement(name = "IdMarcador", required = true)
    protected String idMarcador;
    @XmlElement(name = "Texto", required = true)
    protected String texto;

    /**
     * Obtém o valor da propriedade protocoloProcedimento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProtocoloProcedimento() {
        return protocoloProcedimento;
    }

    /**
     * Define o valor da propriedade protocoloProcedimento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProtocoloProcedimento(String value) {
        this.protocoloProcedimento = value;
    }

    /**
     * Obtém o valor da propriedade idMarcador.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdMarcador() {
        return idMarcador;
    }

    /**
     * Define o valor da propriedade idMarcador.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdMarcador(String value) {
        this.idMarcador = value;
    }

    /**
     * Obtém o valor da propriedade texto.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTexto() {
        return texto;
    }

    /**
     * Define o valor da propriedade texto.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTexto(String value) {
        this.texto = value;
    }

}
