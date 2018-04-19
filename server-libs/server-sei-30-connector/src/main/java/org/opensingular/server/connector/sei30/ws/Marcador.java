
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
 * <p>Classe Java de Marcador complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="Marcador">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="IdMarcador" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Nome" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Icone" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="SinAtivo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Marcador", propOrder = {

})
public class Marcador {

    @XmlElement(name = "IdMarcador", required = true)
    protected String idMarcador;
    @XmlElement(name = "Nome", required = true)
    protected String nome;
    @XmlElement(name = "Icone", required = true)
    protected String icone;
    @XmlElement(name = "SinAtivo", required = true)
    protected String sinAtivo;

    /**
     * Obtém o valor da propriedade idMarcador.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdMarcador() {
        return idMarcador;
    }

    /**
     * Define o valor da propriedade idMarcador.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdMarcador(String value) {
        this.idMarcador = value;
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
     * Obtém o valor da propriedade icone.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIcone() {
        return icone;
    }

    /**
     * Define o valor da propriedade icone.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIcone(String value) {
        this.icone = value;
    }

    /**
     * Obtém o valor da propriedade sinAtivo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSinAtivo() {
        return sinAtivo;
    }

    /**
     * Define o valor da propriedade sinAtivo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSinAtivo(String value) {
        this.sinAtivo = value;
    }

}
