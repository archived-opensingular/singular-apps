
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

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;


/**
 * <p>Java class for RetornoValidarLogin complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RetornoValidarLogin">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="strSiglaOrgaoSistema" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="numIdOrgaoSistema" type="{http://www.w3.org/2001/XMLSchema}byte"/>
 *         &lt;element name="strDescricaoOrgaoSistema" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="strSiglaSistema" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="numIdSistema" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="strSiglaOrgaoUsuario" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="strDescricaoOrgaoUsuario" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="numIdOrgaoUsuario" type="{http://www.w3.org/2001/XMLSchema}byte"/>
 *         &lt;element name="numIdContextoUsuario" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="numIdUsuario" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="strSiglaUsuario" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="strNomeUsuario" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="strHashInterno" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="strHashUsuario" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="arrUnidadesPadrao">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="numTimestampLogin" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="arrPropriedades">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="arrPermissoes">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="item">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="key" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *                             &lt;element name="value">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;sequence>
 *                                       &lt;element name="item" type="{sipns}ValueItem" maxOccurs="unbounded" minOccurs="0"/>
 *                                     &lt;/sequence>
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="numIdUnidadeAtual" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="strPaginaInicial" type="{http://www.w3.org/2001/XMLSchema}anyURI"/>
 *         &lt;element name="strUltimaPagina" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="strIdOrigemUsuario" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="numVersaoSip" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="numVersaoInfraSip" type="{http://www.w3.org/2001/XMLSchema}float"/>
 *         &lt;element name="strSiglaOrgaoUsuarioEmulador" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="strDescricaoOrgaoUsuarioEmulador" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="numIdOrgaoUsuarioEmulador" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="numIdUsuarioEmulador" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="strSiglaUsuarioEmulador" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="strNomeUsuarioEmulador" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="arrOrgaos">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="item">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="item" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="arrUnidades" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="strDnUsuario" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RetornoValidarLogin", propOrder = {

})
public class RetornoValidarLogin {

    @XmlElement(required = true)
    protected String strSiglaOrgaoSistema;
    protected byte numIdOrgaoSistema;
    @XmlElement(required = true)
    protected String strDescricaoOrgaoSistema;
    @XmlElement(required = true)
    protected String strSiglaSistema;
    protected int numIdSistema;
    @XmlElement(required = true)
    protected String strSiglaOrgaoUsuario;
    @XmlElement(required = true)
    protected String strDescricaoOrgaoUsuario;
    protected byte numIdOrgaoUsuario;
    @XmlElement(required = true, nillable = true)
    protected String numIdContextoUsuario;
    protected int numIdUsuario;
    @XmlElement(required = true)
    protected String strSiglaUsuario;
    @XmlElement(required = true)
    protected String strNomeUsuario;
    @XmlElement(required = true)
    protected String strHashInterno;
    @XmlElement(required = true)
    protected String strHashUsuario;
    @XmlElement(required = true)
    protected RetornoValidarLogin.ArrUnidadesPadrao arrUnidadesPadrao;
    @XmlElement(required = true, nillable = true)
    protected String numTimestampLogin;
    @XmlElement(required = true)
    protected RetornoValidarLogin.ArrPropriedades arrPropriedades;
    @XmlElement(required = true)
    protected RetornoValidarLogin.ArrPermissoes arrPermissoes;
    @XmlElement(required = true, nillable = true)
    protected String numIdUnidadeAtual;
    @XmlElement(required = true)
    @XmlSchemaType(name = "anyURI")
    protected String strPaginaInicial;
    @XmlElement(required = true, nillable = true)
    protected String strUltimaPagina;
    @XmlElement(required = true)
    protected String strIdOrigemUsuario;
    @XmlElement(required = true)
    protected String numVersaoSip;
    protected float numVersaoInfraSip;
    @XmlElement(required = true, nillable = true)
    protected String strSiglaOrgaoUsuarioEmulador;
    @XmlElement(required = true, nillable = true)
    protected String strDescricaoOrgaoUsuarioEmulador;
    @XmlElement(required = true, nillable = true)
    protected String numIdOrgaoUsuarioEmulador;
    @XmlElement(required = true, nillable = true)
    protected String numIdUsuarioEmulador;
    @XmlElement(required = true, nillable = true)
    protected String strSiglaUsuarioEmulador;
    @XmlElement(required = true, nillable = true)
    protected String strNomeUsuarioEmulador;
    @XmlElement(required = true)
    protected RetornoValidarLogin.ArrOrgaos arrOrgaos;
    @XmlElement(required = true, nillable = true)
    protected String arrUnidades;
    @XmlElement(required = true)
    protected String strDnUsuario;

    /**
     * Gets the value of the strSiglaOrgaoSistema property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStrSiglaOrgaoSistema() {
        return strSiglaOrgaoSistema;
    }

    /**
     * Sets the value of the strSiglaOrgaoSistema property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStrSiglaOrgaoSistema(String value) {
        this.strSiglaOrgaoSistema = value;
    }

    /**
     * Gets the value of the numIdOrgaoSistema property.
     * 
     */
    public byte getNumIdOrgaoSistema() {
        return numIdOrgaoSistema;
    }

    /**
     * Sets the value of the numIdOrgaoSistema property.
     * 
     */
    public void setNumIdOrgaoSistema(byte value) {
        this.numIdOrgaoSistema = value;
    }

    /**
     * Gets the value of the strDescricaoOrgaoSistema property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStrDescricaoOrgaoSistema() {
        return strDescricaoOrgaoSistema;
    }

    /**
     * Sets the value of the strDescricaoOrgaoSistema property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStrDescricaoOrgaoSistema(String value) {
        this.strDescricaoOrgaoSistema = value;
    }

    /**
     * Gets the value of the strSiglaSistema property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStrSiglaSistema() {
        return strSiglaSistema;
    }

    /**
     * Sets the value of the strSiglaSistema property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStrSiglaSistema(String value) {
        this.strSiglaSistema = value;
    }

    /**
     * Gets the value of the numIdSistema property.
     * 
     */
    public int getNumIdSistema() {
        return numIdSistema;
    }

    /**
     * Sets the value of the numIdSistema property.
     * 
     */
    public void setNumIdSistema(int value) {
        this.numIdSistema = value;
    }

    /**
     * Gets the value of the strSiglaOrgaoUsuario property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStrSiglaOrgaoUsuario() {
        return strSiglaOrgaoUsuario;
    }

    /**
     * Sets the value of the strSiglaOrgaoUsuario property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStrSiglaOrgaoUsuario(String value) {
        this.strSiglaOrgaoUsuario = value;
    }

    /**
     * Gets the value of the strDescricaoOrgaoUsuario property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStrDescricaoOrgaoUsuario() {
        return strDescricaoOrgaoUsuario;
    }

    /**
     * Sets the value of the strDescricaoOrgaoUsuario property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStrDescricaoOrgaoUsuario(String value) {
        this.strDescricaoOrgaoUsuario = value;
    }

    /**
     * Gets the value of the numIdOrgaoUsuario property.
     * 
     */
    public byte getNumIdOrgaoUsuario() {
        return numIdOrgaoUsuario;
    }

    /**
     * Sets the value of the numIdOrgaoUsuario property.
     * 
     */
    public void setNumIdOrgaoUsuario(byte value) {
        this.numIdOrgaoUsuario = value;
    }

    /**
     * Gets the value of the numIdContextoUsuario property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumIdContextoUsuario() {
        return numIdContextoUsuario;
    }

    /**
     * Sets the value of the numIdContextoUsuario property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumIdContextoUsuario(String value) {
        this.numIdContextoUsuario = value;
    }

    /**
     * Gets the value of the numIdUsuario property.
     * 
     */
    public int getNumIdUsuario() {
        return numIdUsuario;
    }

    /**
     * Sets the value of the numIdUsuario property.
     * 
     */
    public void setNumIdUsuario(int value) {
        this.numIdUsuario = value;
    }

    /**
     * Gets the value of the strSiglaUsuario property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStrSiglaUsuario() {
        return strSiglaUsuario;
    }

    /**
     * Sets the value of the strSiglaUsuario property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStrSiglaUsuario(String value) {
        this.strSiglaUsuario = value;
    }

    /**
     * Gets the value of the strNomeUsuario property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStrNomeUsuario() {
        return strNomeUsuario;
    }

    /**
     * Sets the value of the strNomeUsuario property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStrNomeUsuario(String value) {
        this.strNomeUsuario = value;
    }

    /**
     * Gets the value of the strHashInterno property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStrHashInterno() {
        return strHashInterno;
    }

    /**
     * Sets the value of the strHashInterno property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStrHashInterno(String value) {
        this.strHashInterno = value;
    }

    /**
     * Gets the value of the strHashUsuario property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStrHashUsuario() {
        return strHashUsuario;
    }

    /**
     * Sets the value of the strHashUsuario property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStrHashUsuario(String value) {
        this.strHashUsuario = value;
    }

    /**
     * Gets the value of the arrUnidadesPadrao property.
     * 
     * @return
     *     possible object is
     *     {@link RetornoValidarLogin.ArrUnidadesPadrao }
     *     
     */
    public RetornoValidarLogin.ArrUnidadesPadrao getArrUnidadesPadrao() {
        return arrUnidadesPadrao;
    }

    /**
     * Sets the value of the arrUnidadesPadrao property.
     * 
     * @param value
     *     allowed object is
     *     {@link RetornoValidarLogin.ArrUnidadesPadrao }
     *     
     */
    public void setArrUnidadesPadrao(RetornoValidarLogin.ArrUnidadesPadrao value) {
        this.arrUnidadesPadrao = value;
    }

    /**
     * Gets the value of the numTimestampLogin property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumTimestampLogin() {
        return numTimestampLogin;
    }

    /**
     * Sets the value of the numTimestampLogin property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumTimestampLogin(String value) {
        this.numTimestampLogin = value;
    }

    /**
     * Gets the value of the arrPropriedades property.
     * 
     * @return
     *     possible object is
     *     {@link RetornoValidarLogin.ArrPropriedades }
     *     
     */
    public RetornoValidarLogin.ArrPropriedades getArrPropriedades() {
        return arrPropriedades;
    }

    /**
     * Sets the value of the arrPropriedades property.
     * 
     * @param value
     *     allowed object is
     *     {@link RetornoValidarLogin.ArrPropriedades }
     *     
     */
    public void setArrPropriedades(RetornoValidarLogin.ArrPropriedades value) {
        this.arrPropriedades = value;
    }

    /**
     * Gets the value of the arrPermissoes property.
     * 
     * @return
     *     possible object is
     *     {@link RetornoValidarLogin.ArrPermissoes }
     *     
     */
    public RetornoValidarLogin.ArrPermissoes getArrPermissoes() {
        return arrPermissoes;
    }

    /**
     * Sets the value of the arrPermissoes property.
     * 
     * @param value
     *     allowed object is
     *     {@link RetornoValidarLogin.ArrPermissoes }
     *     
     */
    public void setArrPermissoes(RetornoValidarLogin.ArrPermissoes value) {
        this.arrPermissoes = value;
    }

    /**
     * Gets the value of the numIdUnidadeAtual property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumIdUnidadeAtual() {
        return numIdUnidadeAtual;
    }

    /**
     * Sets the value of the numIdUnidadeAtual property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumIdUnidadeAtual(String value) {
        this.numIdUnidadeAtual = value;
    }

    /**
     * Gets the value of the strPaginaInicial property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStrPaginaInicial() {
        return strPaginaInicial;
    }

    /**
     * Sets the value of the strPaginaInicial property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStrPaginaInicial(String value) {
        this.strPaginaInicial = value;
    }

    /**
     * Gets the value of the strUltimaPagina property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStrUltimaPagina() {
        return strUltimaPagina;
    }

    /**
     * Sets the value of the strUltimaPagina property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStrUltimaPagina(String value) {
        this.strUltimaPagina = value;
    }

    /**
     * Gets the value of the strIdOrigemUsuario property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStrIdOrigemUsuario() {
        return strIdOrigemUsuario;
    }

    /**
     * Sets the value of the strIdOrigemUsuario property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStrIdOrigemUsuario(String value) {
        this.strIdOrigemUsuario = value;
    }

    /**
     * Gets the value of the numVersaoSip property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumVersaoSip() {
        return numVersaoSip;
    }

    /**
     * Sets the value of the numVersaoSip property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumVersaoSip(String value) {
        this.numVersaoSip = value;
    }

    /**
     * Gets the value of the numVersaoInfraSip property.
     * 
     */
    public float getNumVersaoInfraSip() {
        return numVersaoInfraSip;
    }

    /**
     * Sets the value of the numVersaoInfraSip property.
     * 
     */
    public void setNumVersaoInfraSip(float value) {
        this.numVersaoInfraSip = value;
    }

    /**
     * Gets the value of the strSiglaOrgaoUsuarioEmulador property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStrSiglaOrgaoUsuarioEmulador() {
        return strSiglaOrgaoUsuarioEmulador;
    }

    /**
     * Sets the value of the strSiglaOrgaoUsuarioEmulador property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStrSiglaOrgaoUsuarioEmulador(String value) {
        this.strSiglaOrgaoUsuarioEmulador = value;
    }

    /**
     * Gets the value of the strDescricaoOrgaoUsuarioEmulador property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStrDescricaoOrgaoUsuarioEmulador() {
        return strDescricaoOrgaoUsuarioEmulador;
    }

    /**
     * Sets the value of the strDescricaoOrgaoUsuarioEmulador property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStrDescricaoOrgaoUsuarioEmulador(String value) {
        this.strDescricaoOrgaoUsuarioEmulador = value;
    }

    /**
     * Gets the value of the numIdOrgaoUsuarioEmulador property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumIdOrgaoUsuarioEmulador() {
        return numIdOrgaoUsuarioEmulador;
    }

    /**
     * Sets the value of the numIdOrgaoUsuarioEmulador property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumIdOrgaoUsuarioEmulador(String value) {
        this.numIdOrgaoUsuarioEmulador = value;
    }

    /**
     * Gets the value of the numIdUsuarioEmulador property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumIdUsuarioEmulador() {
        return numIdUsuarioEmulador;
    }

    /**
     * Sets the value of the numIdUsuarioEmulador property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumIdUsuarioEmulador(String value) {
        this.numIdUsuarioEmulador = value;
    }

    /**
     * Gets the value of the strSiglaUsuarioEmulador property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStrSiglaUsuarioEmulador() {
        return strSiglaUsuarioEmulador;
    }

    /**
     * Sets the value of the strSiglaUsuarioEmulador property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStrSiglaUsuarioEmulador(String value) {
        this.strSiglaUsuarioEmulador = value;
    }

    /**
     * Gets the value of the strNomeUsuarioEmulador property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStrNomeUsuarioEmulador() {
        return strNomeUsuarioEmulador;
    }

    /**
     * Sets the value of the strNomeUsuarioEmulador property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStrNomeUsuarioEmulador(String value) {
        this.strNomeUsuarioEmulador = value;
    }

    /**
     * Gets the value of the arrOrgaos property.
     * 
     * @return
     *     possible object is
     *     {@link RetornoValidarLogin.ArrOrgaos }
     *     
     */
    public RetornoValidarLogin.ArrOrgaos getArrOrgaos() {
        return arrOrgaos;
    }

    /**
     * Sets the value of the arrOrgaos property.
     * 
     * @param value
     *     allowed object is
     *     {@link RetornoValidarLogin.ArrOrgaos }
     *     
     */
    public void setArrOrgaos(RetornoValidarLogin.ArrOrgaos value) {
        this.arrOrgaos = value;
    }

    /**
     * Gets the value of the arrUnidades property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getArrUnidades() {
        return arrUnidades;
    }

    /**
     * Sets the value of the arrUnidades property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setArrUnidades(String value) {
        this.arrUnidades = value;
    }

    /**
     * Gets the value of the strDnUsuario property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStrDnUsuario() {
        return strDnUsuario;
    }

    /**
     * Sets the value of the strDnUsuario property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStrDnUsuario(String value) {
        this.strDnUsuario = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="item">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="item" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "item"
    })
    public static class ArrOrgaos {

        @XmlElement(required = true)
        protected RetornoValidarLogin.ArrOrgaos.Item item;

        /**
         * Gets the value of the item property.
         * 
         * @return
         *     possible object is
         *     {@link RetornoValidarLogin.ArrOrgaos.Item }
         *     
         */
        public RetornoValidarLogin.ArrOrgaos.Item getItem() {
            return item;
        }

        /**
         * Sets the value of the item property.
         * 
         * @param value
         *     allowed object is
         *     {@link RetornoValidarLogin.ArrOrgaos.Item }
         *     
         */
        public void setItem(RetornoValidarLogin.ArrOrgaos.Item value) {
            this.item = value;
        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="item" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "item"
        })
        public static class Item {

            protected List<String> item;

            /**
             * Gets the value of the item property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the item property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getItem().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link String }
             * 
             * 
             */
            public List<String> getItem() {
                if (item == null) {
                    item = new ArrayList<String>();
                }
                return this.item;
            }

        }

    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="item">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="key" type="{http://www.w3.org/2001/XMLSchema}int"/>
     *                   &lt;element name="value">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;sequence>
     *                             &lt;element name="item" type="{sipns}ValueItem" maxOccurs="unbounded" minOccurs="0"/>
     *                           &lt;/sequence>
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "item"
    })
    public static class ArrPermissoes {

        @XmlElement(required = true)
        protected RetornoValidarLogin.ArrPermissoes.Item item;

        /**
         * Gets the value of the item property.
         * 
         * @return
         *     possible object is
         *     {@link RetornoValidarLogin.ArrPermissoes.Item }
         *     
         */
        public RetornoValidarLogin.ArrPermissoes.Item getItem() {
            return item;
        }

        /**
         * Sets the value of the item property.
         * 
         * @param value
         *     allowed object is
         *     {@link RetornoValidarLogin.ArrPermissoes.Item }
         *     
         */
        public void setItem(RetornoValidarLogin.ArrPermissoes.Item value) {
            this.item = value;
        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="key" type="{http://www.w3.org/2001/XMLSchema}int"/>
         *         &lt;element name="value">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;sequence>
         *                   &lt;element name="item" type="{sipns}ValueItem" maxOccurs="unbounded" minOccurs="0"/>
         *                 &lt;/sequence>
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "key",
            "value"
        })
        public static class Item {

            protected int key;
            @XmlElement(required = true)
            protected RetornoValidarLogin.ArrPermissoes.Item.Value value;

            /**
             * Gets the value of the key property.
             * 
             */
            public int getKey() {
                return key;
            }

            /**
             * Sets the value of the key property.
             * 
             */
            public void setKey(int value) {
                this.key = value;
            }

            /**
             * Gets the value of the value property.
             * 
             * @return
             *     possible object is
             *     {@link RetornoValidarLogin.ArrPermissoes.Item.Value }
             *     
             */
            public RetornoValidarLogin.ArrPermissoes.Item.Value getValue() {
                return value;
            }

            /**
             * Sets the value of the value property.
             * 
             * @param value
             *     allowed object is
             *     {@link RetornoValidarLogin.ArrPermissoes.Item.Value }
             *     
             */
            public void setValue(RetornoValidarLogin.ArrPermissoes.Item.Value value) {
                this.value = value;
            }


            /**
             * <p>Java class for anonymous complex type.
             * 
             * <p>The following schema fragment specifies the expected content contained within this class.
             * 
             * <pre>
             * &lt;complexType>
             *   &lt;complexContent>
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *       &lt;sequence>
             *         &lt;element name="item" type="{sipns}ValueItem" maxOccurs="unbounded" minOccurs="0"/>
             *       &lt;/sequence>
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                "item"
            })
            public static class Value {

                protected List<ValueItem> item;

                /**
                 * Gets the value of the item property.
                 * 
                 * <p>
                 * This accessor method returns a reference to the live list,
                 * not a snapshot. Therefore any modification you make to the
                 * returned list will be present inside the JAXB object.
                 * This is why there is not a <CODE>set</CODE> method for the item property.
                 * 
                 * <p>
                 * For example, to add a new item, do as follows:
                 * <pre>
                 *    getItem().add(newItem);
                 * </pre>
                 * 
                 * 
                 * <p>
                 * Objects of the following type(s) are allowed in the list
                 * {@link ValueItem }
                 * 
                 * 
                 */
                public List<ValueItem> getItem() {
                    if (item == null) {
                        item = new ArrayList<ValueItem>();
                    }
                    return this.item;
                }

            }

        }

    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;simpleContent>
     *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
     *     &lt;/extension>
     *   &lt;/simpleContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "value"
    })
    public static class ArrPropriedades {

        @XmlValue
        protected String value;

        /**
         * Gets the value of the value property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getValue() {
            return value;
        }

        /**
         * Sets the value of the value property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setValue(String value) {
            this.value = value;
        }

    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;simpleContent>
     *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
     *     &lt;/extension>
     *   &lt;/simpleContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "value"
    })
    public static class ArrUnidadesPadrao {

        @XmlValue
        protected String value;

        /**
         * Gets the value of the value property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getValue() {
            return value;
        }

        /**
         * Sets the value of the value property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setValue(String value) {
            this.value = value;
        }

    }

}
