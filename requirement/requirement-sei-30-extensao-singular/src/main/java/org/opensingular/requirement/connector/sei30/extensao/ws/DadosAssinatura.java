
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


package org.opensingular.requirement.connector.sei30.extensao.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de DadosAssinatura complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="DadosAssinatura">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="IdOrgaoUsuario" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="IdContextoUsuario" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="SiglaUsuario" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="SenhaUsuario" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="CargoFuncao" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="IdsDocumento" type="{extensaons}ArrayOfIdDocumento"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DadosAssinatura", propOrder = {

})
public class DadosAssinatura {

    @XmlElement(name = "IdOrgaoUsuario")
    protected long idOrgaoUsuario;
    @XmlElement(name = "IdContextoUsuario")
    protected long idContextoUsuario;
    @XmlElement(name = "SiglaUsuario", required = true)
    protected String siglaUsuario;
    @XmlElement(name = "SenhaUsuario", required = true)
    protected String senhaUsuario;
    @XmlElement(name = "CargoFuncao", required = true)
    protected String cargoFuncao;
    @XmlElement(name = "IdsDocumento", required = true)
    protected ArrayOfIdDocumento idsDocumento;

    /**
     * Obtém o valor da propriedade idOrgaoUsuario.
     * 
     */
    public long getIdOrgaoUsuario() {
        return idOrgaoUsuario;
    }

    /**
     * Define o valor da propriedade idOrgaoUsuario.
     * 
     */
    public void setIdOrgaoUsuario(long value) {
        this.idOrgaoUsuario = value;
    }

    /**
     * Obtém o valor da propriedade idContextoUsuario.
     * 
     */
    public long getIdContextoUsuario() {
        return idContextoUsuario;
    }

    /**
     * Define o valor da propriedade idContextoUsuario.
     * 
     */
    public void setIdContextoUsuario(long value) {
        this.idContextoUsuario = value;
    }

    /**
     * Obtém o valor da propriedade siglaUsuario.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSiglaUsuario() {
        return siglaUsuario;
    }

    /**
     * Define o valor da propriedade siglaUsuario.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSiglaUsuario(String value) {
        this.siglaUsuario = value;
    }

    /**
     * Obtém o valor da propriedade senhaUsuario.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSenhaUsuario() {
        return senhaUsuario;
    }

    /**
     * Define o valor da propriedade senhaUsuario.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSenhaUsuario(String value) {
        this.senhaUsuario = value;
    }

    /**
     * Obtém o valor da propriedade cargoFuncao.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCargoFuncao() {
        return cargoFuncao;
    }

    /**
     * Define o valor da propriedade cargoFuncao.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCargoFuncao(String value) {
        this.cargoFuncao = value;
    }

    /**
     * Obtém o valor da propriedade idsDocumento.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfIdDocumento }
     *     
     */
    public ArrayOfIdDocumento getIdsDocumento() {
        return idsDocumento;
    }

    /**
     * Define o valor da propriedade idsDocumento.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfIdDocumento }
     *     
     */
    public void setIdsDocumento(ArrayOfIdDocumento value) {
        this.idsDocumento = value;
    }

}
