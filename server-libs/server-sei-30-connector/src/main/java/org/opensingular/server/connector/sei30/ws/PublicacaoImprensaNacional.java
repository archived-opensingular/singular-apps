
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

package org.opensingular.server.connector.sei30.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de PublicacaoImprensaNacional complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="PublicacaoImprensaNacional">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="SiglaVeiculo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="DescricaoVeiculo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Pagina" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Secao" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Data" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PublicacaoImprensaNacional", propOrder = {

})
public class PublicacaoImprensaNacional {

    @XmlElement(name = "SiglaVeiculo", required = true)
    protected String siglaVeiculo;
    @XmlElement(name = "DescricaoVeiculo", required = true)
    protected String descricaoVeiculo;
    @XmlElement(name = "Pagina", required = true)
    protected String pagina;
    @XmlElement(name = "Secao", required = true)
    protected String secao;
    @XmlElement(name = "Data", required = true)
    protected String data;

    /**
     * Obtém o valor da propriedade siglaVeiculo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSiglaVeiculo() {
        return siglaVeiculo;
    }

    /**
     * Define o valor da propriedade siglaVeiculo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSiglaVeiculo(String value) {
        this.siglaVeiculo = value;
    }

    /**
     * Obtém o valor da propriedade descricaoVeiculo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescricaoVeiculo() {
        return descricaoVeiculo;
    }

    /**
     * Define o valor da propriedade descricaoVeiculo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescricaoVeiculo(String value) {
        this.descricaoVeiculo = value;
    }

    /**
     * Obtém o valor da propriedade pagina.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPagina() {
        return pagina;
    }

    /**
     * Define o valor da propriedade pagina.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPagina(String value) {
        this.pagina = value;
    }

    /**
     * Obtém o valor da propriedade secao.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSecao() {
        return secao;
    }

    /**
     * Define o valor da propriedade secao.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSecao(String value) {
        this.secao = value;
    }

    /**
     * Obtém o valor da propriedade data.
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
     * Define o valor da propriedade data.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setData(String value) {
        this.data = value;
    }

}
