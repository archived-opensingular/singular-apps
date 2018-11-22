
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
 * <p>Java class for LinkExterno complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="LinkExterno">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="ProtocoloProcedimento" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="LinkExternoProcedimento" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LinkExterno", propOrder = {

})
public class LinkExterno {

    @XmlElement(name = "ProtocoloProcedimento", required = true)
    protected String protocoloProcedimento;
    @XmlElement(name = "LinkExternoProcedimento", required = true)
    protected String linkExternoProcedimento;

    /**
     * Gets the value of the protocoloProcedimento property.
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
     * Sets the value of the protocoloProcedimento property.
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
     * Gets the value of the linkExternoProcedimento property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLinkExternoProcedimento() {
        return linkExternoProcedimento;
    }

    /**
     * Sets the value of the linkExternoProcedimento property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLinkExternoProcedimento(String value) {
        this.linkExternoProcedimento = value;
    }

}
