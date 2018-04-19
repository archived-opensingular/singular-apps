
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
 * <p>Classe Java de Andamento complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="Andamento">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="IdAndamento" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="IdTarefa" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="IdTarefaModulo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Descricao" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="DataHora" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Unidade" type="{Sei}Unidade"/>
 *         &lt;element name="Usuario" type="{Sei}Usuario"/>
 *         &lt;element name="Atributos" type="{Sei}ArrayOfAtributoAndamento" minOccurs="0"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Andamento", propOrder = {

})
public class Andamento {

    @XmlElement(name = "IdAndamento")
    protected String idAndamento;
    @XmlElement(name = "IdTarefa")
    protected String idTarefa;
    @XmlElement(name = "IdTarefaModulo")
    protected String idTarefaModulo;
    @XmlElement(name = "Descricao", required = true)
    protected String descricao;
    @XmlElement(name = "DataHora", required = true)
    protected String dataHora;
    @XmlElement(name = "Unidade", required = true)
    protected Unidade unidade;
    @XmlElement(name = "Usuario", required = true)
    protected Usuario usuario;
    @XmlElement(name = "Atributos")
    protected ArrayOfAtributoAndamento atributos;

    /**
     * Obtém o valor da propriedade idAndamento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdAndamento() {
        return idAndamento;
    }

    /**
     * Define o valor da propriedade idAndamento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdAndamento(String value) {
        this.idAndamento = value;
    }

    /**
     * Obtém o valor da propriedade idTarefa.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdTarefa() {
        return idTarefa;
    }

    /**
     * Define o valor da propriedade idTarefa.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdTarefa(String value) {
        this.idTarefa = value;
    }

    /**
     * Obtém o valor da propriedade idTarefaModulo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdTarefaModulo() {
        return idTarefaModulo;
    }

    /**
     * Define o valor da propriedade idTarefaModulo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdTarefaModulo(String value) {
        this.idTarefaModulo = value;
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
     * Obtém o valor da propriedade unidade.
     * 
     * @return
     *     possible object is
     *     {@link Unidade }
     *     
     */
    public Unidade getUnidade() {
        return unidade;
    }

    /**
     * Define o valor da propriedade unidade.
     * 
     * @param value
     *     allowed object is
     *     {@link Unidade }
     *     
     */
    public void setUnidade(Unidade value) {
        this.unidade = value;
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
     * Obtém o valor da propriedade atributos.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfAtributoAndamento }
     *     
     */
    public ArrayOfAtributoAndamento getAtributos() {
        return atributos;
    }

    /**
     * Define o valor da propriedade atributos.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfAtributoAndamento }
     *     
     */
    public void setAtributos(ArrayOfAtributoAndamento value) {
        this.atributos = value;
    }

}
