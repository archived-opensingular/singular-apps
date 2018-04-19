
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
 * <p>Classe Java de ProtocoloBloco complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="ProtocoloBloco">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="ProtocoloFormatado" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Identificacao" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Assinaturas" type="{Sei}ArrayOfAssinatura"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ProtocoloBloco", propOrder = {

})
public class ProtocoloBloco {

    @XmlElement(name = "ProtocoloFormatado", required = true)
    protected String protocoloFormatado;
    @XmlElement(name = "Identificacao", required = true)
    protected String identificacao;
    @XmlElement(name = "Assinaturas", required = true)
    protected ArrayOfAssinatura assinaturas;

    /**
     * Obtém o valor da propriedade protocoloFormatado.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProtocoloFormatado() {
        return protocoloFormatado;
    }

    /**
     * Define o valor da propriedade protocoloFormatado.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProtocoloFormatado(String value) {
        this.protocoloFormatado = value;
    }

    /**
     * Obtém o valor da propriedade identificacao.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdentificacao() {
        return identificacao;
    }

    /**
     * Define o valor da propriedade identificacao.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdentificacao(String value) {
        this.identificacao = value;
    }

    /**
     * Obtém o valor da propriedade assinaturas.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfAssinatura }
     *     
     */
    public ArrayOfAssinatura getAssinaturas() {
        return assinaturas;
    }

    /**
     * Define o valor da propriedade assinaturas.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfAssinatura }
     *     
     */
    public void setAssinaturas(ArrayOfAssinatura value) {
        this.assinaturas = value;
    }

}
