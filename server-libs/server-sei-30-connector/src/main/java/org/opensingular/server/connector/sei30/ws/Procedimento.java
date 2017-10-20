
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

package org.opensingular.server.connector.sei30.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de Procedimento complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="Procedimento">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="IdTipoProcedimento" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="NumeroProtocolo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DataAutuacao" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Especificacao" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Assuntos" type="{Sei}ArrayOfAssunto"/>
 *         &lt;element name="Interessados" type="{Sei}ArrayOfInteressado"/>
 *         &lt;element name="Observacao" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="NivelAcesso" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="IdHipoteseLegal" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Procedimento", propOrder = {

})
public class Procedimento {

    @XmlElement(name = "IdTipoProcedimento", required = true)
    protected String idTipoProcedimento;
    @XmlElement(name = "NumeroProtocolo")
    protected String numeroProtocolo;
    @XmlElement(name = "DataAutuacao")
    protected String dataAutuacao;
    @XmlElement(name = "Especificacao")
    protected String especificacao;
    @XmlElement(name = "Assuntos", required = true)
    protected ArrayOfAssunto assuntos;
    @XmlElement(name = "Interessados", required = true)
    protected ArrayOfInteressado interessados;
    @XmlElement(name = "Observacao", required = true)
    protected String observacao;
    @XmlElement(name = "NivelAcesso", required = true)
    protected String nivelAcesso;
    @XmlElement(name = "IdHipoteseLegal")
    protected String idHipoteseLegal;

    /**
     * Obtém o valor da propriedade idTipoProcedimento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdTipoProcedimento() {
        return idTipoProcedimento;
    }

    /**
     * Define o valor da propriedade idTipoProcedimento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdTipoProcedimento(String value) {
        this.idTipoProcedimento = value;
    }

    /**
     * Obtém o valor da propriedade numeroProtocolo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumeroProtocolo() {
        return numeroProtocolo;
    }

    /**
     * Define o valor da propriedade numeroProtocolo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumeroProtocolo(String value) {
        this.numeroProtocolo = value;
    }

    /**
     * Obtém o valor da propriedade dataAutuacao.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDataAutuacao() {
        return dataAutuacao;
    }

    /**
     * Define o valor da propriedade dataAutuacao.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataAutuacao(String value) {
        this.dataAutuacao = value;
    }

    /**
     * Obtém o valor da propriedade especificacao.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEspecificacao() {
        return especificacao;
    }

    /**
     * Define o valor da propriedade especificacao.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEspecificacao(String value) {
        this.especificacao = value;
    }

    /**
     * Obtém o valor da propriedade assuntos.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfAssunto }
     *     
     */
    public ArrayOfAssunto getAssuntos() {
        return assuntos;
    }

    /**
     * Define o valor da propriedade assuntos.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfAssunto }
     *     
     */
    public void setAssuntos(ArrayOfAssunto value) {
        this.assuntos = value;
    }

    /**
     * Obtém o valor da propriedade interessados.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfInteressado }
     *     
     */
    public ArrayOfInteressado getInteressados() {
        return interessados;
    }

    /**
     * Define o valor da propriedade interessados.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfInteressado }
     *     
     */
    public void setInteressados(ArrayOfInteressado value) {
        this.interessados = value;
    }

    /**
     * Obtém o valor da propriedade observacao.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getObservacao() {
        return observacao;
    }

    /**
     * Define o valor da propriedade observacao.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setObservacao(String value) {
        this.observacao = value;
    }

    /**
     * Obtém o valor da propriedade nivelAcesso.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNivelAcesso() {
        return nivelAcesso;
    }

    /**
     * Define o valor da propriedade nivelAcesso.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNivelAcesso(String value) {
        this.nivelAcesso = value;
    }

    /**
     * Obtém o valor da propriedade idHipoteseLegal.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdHipoteseLegal() {
        return idHipoteseLegal;
    }

    /**
     * Define o valor da propriedade idHipoteseLegal.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdHipoteseLegal(String value) {
        this.idHipoteseLegal = value;
    }

}
