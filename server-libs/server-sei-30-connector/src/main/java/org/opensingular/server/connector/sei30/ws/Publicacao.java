
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
 * <p>Classe Java de Publicacao complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="Publicacao">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="NomeVeiculo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Numero" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="DataDisponibilizacao" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="DataPublicacao" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Estado" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ImprensaNacional" type="{Sei}PublicacaoImprensaNacional"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Publicacao", propOrder = {

})
public class Publicacao {

    @XmlElement(name = "NomeVeiculo", required = true)
    protected String nomeVeiculo;
    @XmlElement(name = "Numero", required = true)
    protected String numero;
    @XmlElement(name = "DataDisponibilizacao", required = true)
    protected String dataDisponibilizacao;
    @XmlElement(name = "DataPublicacao", required = true)
    protected String dataPublicacao;
    @XmlElement(name = "Estado", required = true)
    protected String estado;
    @XmlElement(name = "ImprensaNacional", required = true)
    protected PublicacaoImprensaNacional imprensaNacional;

    /**
     * Obtém o valor da propriedade nomeVeiculo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNomeVeiculo() {
        return nomeVeiculo;
    }

    /**
     * Define o valor da propriedade nomeVeiculo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNomeVeiculo(String value) {
        this.nomeVeiculo = value;
    }

    /**
     * Obtém o valor da propriedade numero.
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
     * Define o valor da propriedade numero.
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
     * Obtém o valor da propriedade dataDisponibilizacao.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDataDisponibilizacao() {
        return dataDisponibilizacao;
    }

    /**
     * Define o valor da propriedade dataDisponibilizacao.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataDisponibilizacao(String value) {
        this.dataDisponibilizacao = value;
    }

    /**
     * Obtém o valor da propriedade dataPublicacao.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDataPublicacao() {
        return dataPublicacao;
    }

    /**
     * Define o valor da propriedade dataPublicacao.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataPublicacao(String value) {
        this.dataPublicacao = value;
    }

    /**
     * Obtém o valor da propriedade estado.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEstado() {
        return estado;
    }

    /**
     * Define o valor da propriedade estado.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEstado(String value) {
        this.estado = value;
    }

    /**
     * Obtém o valor da propriedade imprensaNacional.
     * 
     * @return
     *     possible object is
     *     {@link PublicacaoImprensaNacional }
     *     
     */
    public PublicacaoImprensaNacional getImprensaNacional() {
        return imprensaNacional;
    }

    /**
     * Define o valor da propriedade imprensaNacional.
     * 
     * @param value
     *     allowed object is
     *     {@link PublicacaoImprensaNacional }
     *     
     */
    public void setImprensaNacional(PublicacaoImprensaNacional value) {
        this.imprensaNacional = value;
    }

}
