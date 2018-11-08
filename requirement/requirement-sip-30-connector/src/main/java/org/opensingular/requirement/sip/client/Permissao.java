
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

package org.opensingular.requirement.sip.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Permissao complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Permissao">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="StaOperacao" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="IdSistema" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="IdOrgaoUsuario" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="IdUsuario" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="IdOrigemUsuario" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="IdOrgaoUnidade" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="IdUnidade" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="IdOrigemUnidade" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="IdPerfil" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="DataInicial" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="DataFinal" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="SinSubunidades" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Permissao", propOrder = {

})
public class Permissao {

    @XmlElement(name = "StaOperacao")
    protected String staOperacao;
    @XmlElement(name = "IdSistema", required = true)
    protected String idSistema;
    @XmlElement(name = "IdOrgaoUsuario", required = true)
    protected String idOrgaoUsuario;
    @XmlElement(name = "IdUsuario", required = true)
    protected String idUsuario;
    @XmlElement(name = "IdOrigemUsuario", required = true)
    protected String idOrigemUsuario;
    @XmlElement(name = "IdOrgaoUnidade", required = true)
    protected String idOrgaoUnidade;
    @XmlElement(name = "IdUnidade", required = true)
    protected String idUnidade;
    @XmlElement(name = "IdOrigemUnidade", required = true)
    protected String idOrigemUnidade;
    @XmlElement(name = "IdPerfil", required = true)
    protected String idPerfil;
    @XmlElement(name = "DataInicial", required = true)
    protected String dataInicial;
    @XmlElement(name = "DataFinal", required = true)
    protected String dataFinal;
    @XmlElement(name = "SinSubunidades", required = true)
    protected String sinSubunidades;

    /**
     * Gets the value of the staOperacao property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStaOperacao() {
        return staOperacao;
    }

    /**
     * Sets the value of the staOperacao property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStaOperacao(String value) {
        this.staOperacao = value;
    }

    /**
     * Gets the value of the idSistema property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdSistema() {
        return idSistema;
    }

    /**
     * Sets the value of the idSistema property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdSistema(String value) {
        this.idSistema = value;
    }

    /**
     * Gets the value of the idOrgaoUsuario property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdOrgaoUsuario() {
        return idOrgaoUsuario;
    }

    /**
     * Sets the value of the idOrgaoUsuario property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdOrgaoUsuario(String value) {
        this.idOrgaoUsuario = value;
    }

    /**
     * Gets the value of the idUsuario property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdUsuario() {
        return idUsuario;
    }

    /**
     * Sets the value of the idUsuario property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdUsuario(String value) {
        this.idUsuario = value;
    }

    /**
     * Gets the value of the idOrigemUsuario property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdOrigemUsuario() {
        return idOrigemUsuario;
    }

    /**
     * Sets the value of the idOrigemUsuario property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdOrigemUsuario(String value) {
        this.idOrigemUsuario = value;
    }

    /**
     * Gets the value of the idOrgaoUnidade property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdOrgaoUnidade() {
        return idOrgaoUnidade;
    }

    /**
     * Sets the value of the idOrgaoUnidade property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdOrgaoUnidade(String value) {
        this.idOrgaoUnidade = value;
    }

    /**
     * Gets the value of the idUnidade property.
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
     * Sets the value of the idUnidade property.
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
     * Gets the value of the idOrigemUnidade property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdOrigemUnidade() {
        return idOrigemUnidade;
    }

    /**
     * Sets the value of the idOrigemUnidade property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdOrigemUnidade(String value) {
        this.idOrigemUnidade = value;
    }

    /**
     * Gets the value of the idPerfil property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdPerfil() {
        return idPerfil;
    }

    /**
     * Sets the value of the idPerfil property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdPerfil(String value) {
        this.idPerfil = value;
    }

    /**
     * Gets the value of the dataInicial property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDataInicial() {
        return dataInicial;
    }

    /**
     * Sets the value of the dataInicial property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataInicial(String value) {
        this.dataInicial = value;
    }

    /**
     * Gets the value of the dataFinal property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDataFinal() {
        return dataFinal;
    }

    /**
     * Sets the value of the dataFinal property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataFinal(String value) {
        this.dataFinal = value;
    }

    /**
     * Gets the value of the sinSubunidades property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSinSubunidades() {
        return sinSubunidades;
    }

    /**
     * Sets the value of the sinSubunidades property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSinSubunidades(String value) {
        this.sinSubunidades = value;
    }

}
