
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
 * <p>Classe Java de RetornoGeracaoProcedimento complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="RetornoGeracaoProcedimento">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="IdProcedimento" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ProcedimentoFormatado" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="LinkAcesso" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="RetornoInclusaoDocumentos" type="{Sei}ArrayOfRetornoInclusaoDocumento"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RetornoGeracaoProcedimento", propOrder = {

})
public class RetornoGeracaoProcedimento {

    @XmlElement(name = "IdProcedimento", required = true)
    protected String idProcedimento;
    @XmlElement(name = "ProcedimentoFormatado", required = true)
    protected String procedimentoFormatado;
    @XmlElement(name = "LinkAcesso", required = true)
    protected String linkAcesso;
    @XmlElement(name = "RetornoInclusaoDocumentos", required = true)
    protected ArrayOfRetornoInclusaoDocumento retornoInclusaoDocumentos;

    /**
     * Obtém o valor da propriedade idProcedimento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdProcedimento() {
        return idProcedimento;
    }

    /**
     * Define o valor da propriedade idProcedimento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdProcedimento(String value) {
        this.idProcedimento = value;
    }

    /**
     * Obtém o valor da propriedade procedimentoFormatado.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProcedimentoFormatado() {
        return procedimentoFormatado;
    }

    /**
     * Define o valor da propriedade procedimentoFormatado.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProcedimentoFormatado(String value) {
        this.procedimentoFormatado = value;
    }

    /**
     * Obtém o valor da propriedade linkAcesso.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLinkAcesso() {
        return linkAcesso;
    }

    /**
     * Define o valor da propriedade linkAcesso.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLinkAcesso(String value) {
        this.linkAcesso = value;
    }

    /**
     * Obtém o valor da propriedade retornoInclusaoDocumentos.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfRetornoInclusaoDocumento }
     *     
     */
    public ArrayOfRetornoInclusaoDocumento getRetornoInclusaoDocumentos() {
        return retornoInclusaoDocumentos;
    }

    /**
     * Define o valor da propriedade retornoInclusaoDocumentos.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfRetornoInclusaoDocumento }
     *     
     */
    public void setRetornoInclusaoDocumentos(ArrayOfRetornoInclusaoDocumento value) {
        this.retornoInclusaoDocumentos = value;
    }

}
