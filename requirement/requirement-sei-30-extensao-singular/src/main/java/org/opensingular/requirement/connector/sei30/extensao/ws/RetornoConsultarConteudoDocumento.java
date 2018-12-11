
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
 * <p>Java class for RetornoConsultarConteudoDocumento complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RetornoConsultarConteudoDocumento"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;all&gt;
 *         &lt;element name="ProtocoloDocumento" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="Conteudo" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/all&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RetornoConsultarConteudoDocumento", propOrder = {

})
public class RetornoConsultarConteudoDocumento {

    @XmlElement(name = "ProtocoloDocumento", required = true)
    protected String protocoloDocumento;
    @XmlElement(name = "Conteudo", required = true)
    protected String conteudo;

    /**
     * Gets the value of the protocoloDocumento property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProtocoloDocumento() {
        return protocoloDocumento;
    }

    /**
     * Sets the value of the protocoloDocumento property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProtocoloDocumento(String value) {
        this.protocoloDocumento = value;
    }

    /**
     * Gets the value of the conteudo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConteudo() {
        return conteudo;
    }

    /**
     * Sets the value of the conteudo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConteudo(String value) {
        this.conteudo = value;
    }

}
