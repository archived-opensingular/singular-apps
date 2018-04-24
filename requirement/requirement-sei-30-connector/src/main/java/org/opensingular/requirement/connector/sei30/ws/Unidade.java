
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
 * <p>Classe Java de Unidade complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="Unidade">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="IdUnidade" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Sigla" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Descricao" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="SinProtocolo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SinArquivamento" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SinOuvidoria" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Unidade", propOrder = {

})
public class Unidade {

    @XmlElement(name = "IdUnidade", required = true)
    protected String idUnidade;
    @XmlElement(name = "Sigla", required = true)
    protected String sigla;
    @XmlElement(name = "Descricao", required = true)
    protected String descricao;
    @XmlElement(name = "SinProtocolo")
    protected String sinProtocolo;
    @XmlElement(name = "SinArquivamento")
    protected String sinArquivamento;
    @XmlElement(name = "SinOuvidoria")
    protected String sinOuvidoria;

    /**
     * Obtém o valor da propriedade idUnidade.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdUnidade() {
        return idUnidade;
    }

    /**
     * Define o valor da propriedade idUnidade.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdUnidade(String value) {
        this.idUnidade = value;
    }

    /**
     * Obtém o valor da propriedade sigla.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSigla() {
        return sigla;
    }

    /**
     * Define o valor da propriedade sigla.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSigla(String value) {
        this.sigla = value;
    }

    /**
     * Obtém o valor da propriedade descricao.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * Define o valor da propriedade descricao.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescricao(String value) {
        this.descricao = value;
    }

    /**
     * Obtém o valor da propriedade sinProtocolo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSinProtocolo() {
        return sinProtocolo;
    }

    /**
     * Define o valor da propriedade sinProtocolo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSinProtocolo(String value) {
        this.sinProtocolo = value;
    }

    /**
     * Obtém o valor da propriedade sinArquivamento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSinArquivamento() {
        return sinArquivamento;
    }

    /**
     * Define o valor da propriedade sinArquivamento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSinArquivamento(String value) {
        this.sinArquivamento = value;
    }

    /**
     * Obtém o valor da propriedade sinOuvidoria.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSinOuvidoria() {
        return sinOuvidoria;
    }

    /**
     * Define o valor da propriedade sinOuvidoria.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSinOuvidoria(String value) {
        this.sinOuvidoria = value;
    }

}
