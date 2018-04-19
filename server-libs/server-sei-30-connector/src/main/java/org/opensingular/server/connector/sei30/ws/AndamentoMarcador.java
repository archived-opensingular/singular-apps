
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
 * <p>Classe Java de AndamentoMarcador complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="AndamentoMarcador">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="IdAndamentoMarcador" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Texto" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="DataHora" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Usuario" type="{Sei}Usuario"/>
 *         &lt;element name="Marcador" type="{Sei}Marcador"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AndamentoMarcador", propOrder = {

})
public class AndamentoMarcador {

    @XmlElement(name = "IdAndamentoMarcador")
    protected String idAndamentoMarcador;
    @XmlElement(name = "Texto", required = true)
    protected String texto;
    @XmlElement(name = "DataHora", required = true)
    protected String dataHora;
    @XmlElement(name = "Usuario", required = true)
    protected Usuario usuario;
    @XmlElement(name = "Marcador", required = true)
    protected Marcador marcador;

    /**
     * Obtém o valor da propriedade idAndamentoMarcador.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdAndamentoMarcador() {
        return idAndamentoMarcador;
    }

    /**
     * Define o valor da propriedade idAndamentoMarcador.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdAndamentoMarcador(String value) {
        this.idAndamentoMarcador = value;
    }

    /**
     * Obtém o valor da propriedade texto.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTexto() {
        return texto;
    }

    /**
     * Define o valor da propriedade texto.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTexto(String value) {
        this.texto = value;
    }

    /**
     * Obtém o valor da propriedade dataHora.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDataHora() {
        return dataHora;
    }

    /**
     * Define o valor da propriedade dataHora.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataHora(String value) {
        this.dataHora = value;
    }

    /**
     * Obtém o valor da propriedade usuario.
     * 
     * @return
     *     possible object is
     *     {@link Usuario }
     *     
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * Define o valor da propriedade usuario.
     * 
     * @param value
     *     allowed object is
     *     {@link Usuario }
     *     
     */
    public void setUsuario(Usuario value) {
        this.usuario = value;
    }

    /**
     * Obtém o valor da propriedade marcador.
     * 
     * @return
     *     possible object is
     *     {@link Marcador }
     *     
     */
    public Marcador getMarcador() {
        return marcador;
    }

    /**
     * Define o valor da propriedade marcador.
     * 
     * @param value
     *     allowed object is
     *     {@link Marcador }
     *     
     */
    public void setMarcador(Marcador value) {
        this.marcador = value;
    }

}
