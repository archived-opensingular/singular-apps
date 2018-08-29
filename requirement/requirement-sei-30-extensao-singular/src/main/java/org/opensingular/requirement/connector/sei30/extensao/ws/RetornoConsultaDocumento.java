
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
 * <p>Java class for RetornoConsultaDocumento complex type.
 * 
 * <p>The following schema fragment specifies the expected         content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RetornoConsultaDocumento"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;all&gt;
 *         &lt;element name="IdProcedimento" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="ProcedimentoFormatado" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="IdDocumento" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="DocumentoFormatado" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="LinkAcesso" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="Serie" type="{extensaons}Serie"/&gt;
 *         &lt;element name="Numero" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="Data" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="Campos" type="{extensaons}ArrayOfCampo"/&gt;
 *       &lt;/all&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RetornoConsultaDocumento", propOrder = {

})
public class RetornoConsultaDocumento {

    @XmlElement(name = "IdProcedimento", required = true)
    protected String idProcedimento;
    @XmlElement(name = "ProcedimentoFormatado", required = true)
    protected String procedimentoFormatado;
    @XmlElement(name = "IdDocumento", required = true)
    protected String idDocumento;
    @XmlElement(name = "DocumentoFormatado", required = true)
    protected String documentoFormatado;
    @XmlElement(name = "LinkAcesso", required = true)
    protected String linkAcesso;
    @XmlElement(name = "Serie", required = true)
    protected Serie serie;
    @XmlElement(name = "Numero", required = true)
    protected String numero;
    @XmlElement(name = "Data", required = true)
    protected String data;
    @XmlElement(name = "Campos", required = true)
    protected ArrayOfCampo campos;

    /**
     * Gets the value of the idProcedimento property.
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
     * Sets the value of the idProcedimento property.
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
     * Gets the value of the procedimentoFormatado property.
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
     * Sets the value of the procedimentoFormatado property.
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
     * Gets the value of the idDocumento property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdDocumento() {
        return idDocumento;
    }

    /**
     * Sets the value of the idDocumento property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdDocumento(String value) {
        this.idDocumento = value;
    }

    /**
     * Gets the value of the documentoFormatado property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDocumentoFormatado() {
        return documentoFormatado;
    }

    /**
     * Sets the value of the documentoFormatado property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDocumentoFormatado(String value) {
        this.documentoFormatado = value;
    }

    /**
     * Gets the value of the linkAcesso property.
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
     * Sets the value of the linkAcesso property.
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
     * Gets the value of the serie property.
     * 
     * @return
     *     possible object is
     *     {@link Serie }
     *     
     */
    public Serie getSerie() {
        return serie;
    }

    /**
     * Sets the value of the serie property.
     * 
     * @param value
     *     allowed object is
     *     {@link Serie }
     *     
     */
    public void setSerie(Serie value) {
        this.serie = value;
    }

    /**
     * Gets the value of the numero property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumero() {
        return numero;
    }

    /**
     * Sets the value of the numero property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumero(String value) {
        this.numero = value;
    }

    /**
     * Gets the value of the data property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getData() {
        return data;
    }

    /**
     * Sets the value of the data property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setData(String value) {
        this.data = value;
    }

    /**
     * Gets the value of the campos property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfCampo }
     *     
     */
    public ArrayOfCampo getCampos() {
        return campos;
    }

    /**
     * Sets the value of the campos property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfCampo }
     *     
     */
    public void setCampos(ArrayOfCampo value) {
        this.campos = value;
    }

}
