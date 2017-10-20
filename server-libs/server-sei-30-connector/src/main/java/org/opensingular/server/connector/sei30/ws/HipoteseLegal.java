
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
 * <p>Classe Java de HipoteseLegal complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="HipoteseLegal">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="IdHipoteseLegal" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Nome" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="BaseLegal" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="NivelAcesso" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "HipoteseLegal", propOrder = {

})
public class HipoteseLegal {

    @XmlElement(name = "IdHipoteseLegal", required = true)
    protected String idHipoteseLegal;
    @XmlElement(name = "Nome", required = true)
    protected String nome;
    @XmlElement(name = "BaseLegal", required = true)
    protected String baseLegal;
    @XmlElement(name = "NivelAcesso", required = true)
    protected String nivelAcesso;

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

    /**
     * Obtém o valor da propriedade nome.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNome() {
        return nome;
    }

    /**
     * Define o valor da propriedade nome.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNome(String value) {
        this.nome = value;
    }

    /**
     * Obtém o valor da propriedade baseLegal.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBaseLegal() {
        return baseLegal;
    }

    /**
     * Define o valor da propriedade baseLegal.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBaseLegal(String value) {
        this.baseLegal = value;
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

}
