
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

package org.opensingular.requirement.connector.sei30.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de RetornoConsultaBloco complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="RetornoConsultaBloco">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="IdBloco" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Unidade" type="{Sei}Unidade"/>
 *         &lt;element name="Usuario" type="{Sei}Usuario"/>
 *         &lt;element name="Descricao" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Tipo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Estado" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="UnidadesDisponibilizacao" type="{Sei}ArrayOfUnidade"/>
 *         &lt;element name="Protocolos" type="{Sei}ArrayOfProtocoloBloco"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RetornoConsultaBloco", propOrder = {

})
public class RetornoConsultaBloco {

    @XmlElement(name = "IdBloco", required = true)
    protected String idBloco;
    @XmlElement(name = "Unidade", required = true)
    protected Unidade unidade;
    @XmlElement(name = "Usuario", required = true)
    protected Usuario usuario;
    @XmlElement(name = "Descricao", required = true)
    protected String descricao;
    @XmlElement(name = "Tipo", required = true)
    protected String tipo;
    @XmlElement(name = "Estado", required = true)
    protected String estado;
    @XmlElement(name = "UnidadesDisponibilizacao", required = true)
    protected ArrayOfUnidade unidadesDisponibilizacao;
    @XmlElement(name = "Protocolos", required = true)
    protected ArrayOfProtocoloBloco protocolos;

    /**
     * Obtém o valor da propriedade idBloco.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdBloco() {
        return idBloco;
    }

    /**
     * Define o valor da propriedade idBloco.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdBloco(String value) {
        this.idBloco = value;
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
     * Obtém o valor da propriedade tipo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * Define o valor da propriedade tipo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipo(String value) {
        this.tipo = value;
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
     * Obtém o valor da propriedade unidadesDisponibilizacao.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfUnidade }
     *     
     */
    public ArrayOfUnidade getUnidadesDisponibilizacao() {
        return unidadesDisponibilizacao;
    }

    /**
     * Define o valor da propriedade unidadesDisponibilizacao.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfUnidade }
     *     
     */
    public void setUnidadesDisponibilizacao(ArrayOfUnidade value) {
        this.unidadesDisponibilizacao = value;
    }

    /**
     * Obtém o valor da propriedade protocolos.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfProtocoloBloco }
     *     
     */
    public ArrayOfProtocoloBloco getProtocolos() {
        return protocolos;
    }

    /**
     * Define o valor da propriedade protocolos.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfProtocoloBloco }
     *     
     */
    public void setProtocolos(ArrayOfProtocoloBloco value) {
        this.protocolos = value;
    }

}
