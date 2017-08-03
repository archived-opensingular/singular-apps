
package org.opensingular.server.connector.sei30.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de RetornoConsultaProcedimento complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="RetornoConsultaProcedimento">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="IdProcedimento" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ProcedimentoFormatado" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Especificacao" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="DataAutuacao" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="LinkAcesso" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="TipoProcedimento" type="{Sei}TipoProcedimento"/>
 *         &lt;element name="AndamentoGeracao" type="{Sei}Andamento"/>
 *         &lt;element name="AndamentoConclusao" type="{Sei}Andamento"/>
 *         &lt;element name="UltimoAndamento" type="{Sei}Andamento"/>
 *         &lt;element name="UnidadesProcedimentoAberto" type="{Sei}ArrayOfUnidadeProcedimentoAberto"/>
 *         &lt;element name="Assuntos" type="{Sei}ArrayOfAssunto"/>
 *         &lt;element name="Interessados" type="{Sei}ArrayOfInteressado"/>
 *         &lt;element name="Observacoes" type="{Sei}ArrayOfObservacao"/>
 *         &lt;element name="ProcedimentosRelacionados" type="{Sei}ArrayOfProcedimentoResumido"/>
 *         &lt;element name="ProcedimentosAnexados" type="{Sei}ArrayOfProcedimentoResumido"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RetornoConsultaProcedimento", propOrder = {

})
public class RetornoConsultaProcedimento {

    @XmlElement(name = "IdProcedimento", required = true)
    protected String idProcedimento;
    @XmlElement(name = "ProcedimentoFormatado", required = true)
    protected String procedimentoFormatado;
    @XmlElement(name = "Especificacao", required = true)
    protected String especificacao;
    @XmlElement(name = "DataAutuacao", required = true)
    protected String dataAutuacao;
    @XmlElement(name = "LinkAcesso", required = true)
    protected String linkAcesso;
    @XmlElement(name = "TipoProcedimento", required = true)
    protected TipoProcedimento tipoProcedimento;
    @XmlElement(name = "AndamentoGeracao", required = true)
    protected Andamento andamentoGeracao;
    @XmlElement(name = "AndamentoConclusao", required = true)
    protected Andamento andamentoConclusao;
    @XmlElement(name = "UltimoAndamento", required = true)
    protected Andamento ultimoAndamento;
    @XmlElement(name = "UnidadesProcedimentoAberto", required = true)
    protected ArrayOfUnidadeProcedimentoAberto unidadesProcedimentoAberto;
    @XmlElement(name = "Assuntos", required = true)
    protected ArrayOfAssunto assuntos;
    @XmlElement(name = "Interessados", required = true)
    protected ArrayOfInteressado interessados;
    @XmlElement(name = "Observacoes", required = true)
    protected ArrayOfObservacao observacoes;
    @XmlElement(name = "ProcedimentosRelacionados", required = true)
    protected ArrayOfProcedimentoResumido procedimentosRelacionados;
    @XmlElement(name = "ProcedimentosAnexados", required = true)
    protected ArrayOfProcedimentoResumido procedimentosAnexados;

    /**
     * Obtém o valor da propriedade idProcedimento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdProcedimento() {
        return idProcedimento;
    }

    /**
     * Define o valor da propriedade idProcedimento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdProcedimento(String value) {
        this.idProcedimento = value;
    }

    /**
     * Obtém o valor da propriedade procedimentoFormatado.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProcedimentoFormatado() {
        return procedimentoFormatado;
    }

    /**
     * Define o valor da propriedade procedimentoFormatado.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProcedimentoFormatado(String value) {
        this.procedimentoFormatado = value;
    }

    /**
     * Obtém o valor da propriedade especificacao.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEspecificacao() {
        return especificacao;
    }

    /**
     * Define o valor da propriedade especificacao.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEspecificacao(String value) {
        this.especificacao = value;
    }

    /**
     * Obtém o valor da propriedade dataAutuacao.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDataAutuacao() {
        return dataAutuacao;
    }

    /**
     * Define o valor da propriedade dataAutuacao.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataAutuacao(String value) {
        this.dataAutuacao = value;
    }

    /**
     * Obtém o valor da propriedade linkAcesso.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLinkAcesso() {
        return linkAcesso;
    }

    /**
     * Define o valor da propriedade linkAcesso.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLinkAcesso(String value) {
        this.linkAcesso = value;
    }

    /**
     * Obtém o valor da propriedade tipoProcedimento.
     * 
     * @return
     *     possible object is
     *     {@link TipoProcedimento }
     *     
     */
    public TipoProcedimento getTipoProcedimento() {
        return tipoProcedimento;
    }

    /**
     * Define o valor da propriedade tipoProcedimento.
     * 
     * @param value
     *     allowed object is
     *     {@link TipoProcedimento }
     *     
     */
    public void setTipoProcedimento(TipoProcedimento value) {
        this.tipoProcedimento = value;
    }

    /**
     * Obtém o valor da propriedade andamentoGeracao.
     * 
     * @return
     *     possible object is
     *     {@link Andamento }
     *     
     */
    public Andamento getAndamentoGeracao() {
        return andamentoGeracao;
    }

    /**
     * Define o valor da propriedade andamentoGeracao.
     * 
     * @param value
     *     allowed object is
     *     {@link Andamento }
     *     
     */
    public void setAndamentoGeracao(Andamento value) {
        this.andamentoGeracao = value;
    }

    /**
     * Obtém o valor da propriedade andamentoConclusao.
     * 
     * @return
     *     possible object is
     *     {@link Andamento }
     *     
     */
    public Andamento getAndamentoConclusao() {
        return andamentoConclusao;
    }

    /**
     * Define o valor da propriedade andamentoConclusao.
     * 
     * @param value
     *     allowed object is
     *     {@link Andamento }
     *     
     */
    public void setAndamentoConclusao(Andamento value) {
        this.andamentoConclusao = value;
    }

    /**
     * Obtém o valor da propriedade ultimoAndamento.
     * 
     * @return
     *     possible object is
     *     {@link Andamento }
     *     
     */
    public Andamento getUltimoAndamento() {
        return ultimoAndamento;
    }

    /**
     * Define o valor da propriedade ultimoAndamento.
     * 
     * @param value
     *     allowed object is
     *     {@link Andamento }
     *     
     */
    public void setUltimoAndamento(Andamento value) {
        this.ultimoAndamento = value;
    }

    /**
     * Obtém o valor da propriedade unidadesProcedimentoAberto.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfUnidadeProcedimentoAberto }
     *     
     */
    public ArrayOfUnidadeProcedimentoAberto getUnidadesProcedimentoAberto() {
        return unidadesProcedimentoAberto;
    }

    /**
     * Define o valor da propriedade unidadesProcedimentoAberto.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfUnidadeProcedimentoAberto }
     *     
     */
    public void setUnidadesProcedimentoAberto(ArrayOfUnidadeProcedimentoAberto value) {
        this.unidadesProcedimentoAberto = value;
    }

    /**
     * Obtém o valor da propriedade assuntos.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfAssunto }
     *     
     */
    public ArrayOfAssunto getAssuntos() {
        return assuntos;
    }

    /**
     * Define o valor da propriedade assuntos.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfAssunto }
     *     
     */
    public void setAssuntos(ArrayOfAssunto value) {
        this.assuntos = value;
    }

    /**
     * Obtém o valor da propriedade interessados.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfInteressado }
     *     
     */
    public ArrayOfInteressado getInteressados() {
        return interessados;
    }

    /**
     * Define o valor da propriedade interessados.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfInteressado }
     *     
     */
    public void setInteressados(ArrayOfInteressado value) {
        this.interessados = value;
    }

    /**
     * Obtém o valor da propriedade observacoes.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfObservacao }
     *     
     */
    public ArrayOfObservacao getObservacoes() {
        return observacoes;
    }

    /**
     * Define o valor da propriedade observacoes.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfObservacao }
     *     
     */
    public void setObservacoes(ArrayOfObservacao value) {
        this.observacoes = value;
    }

    /**
     * Obtém o valor da propriedade procedimentosRelacionados.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfProcedimentoResumido }
     *     
     */
    public ArrayOfProcedimentoResumido getProcedimentosRelacionados() {
        return procedimentosRelacionados;
    }

    /**
     * Define o valor da propriedade procedimentosRelacionados.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfProcedimentoResumido }
     *     
     */
    public void setProcedimentosRelacionados(ArrayOfProcedimentoResumido value) {
        this.procedimentosRelacionados = value;
    }

    /**
     * Obtém o valor da propriedade procedimentosAnexados.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfProcedimentoResumido }
     *     
     */
    public ArrayOfProcedimentoResumido getProcedimentosAnexados() {
        return procedimentosAnexados;
    }

    /**
     * Define o valor da propriedade procedimentosAnexados.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfProcedimentoResumido }
     *     
     */
    public void setProcedimentosAnexados(ArrayOfProcedimentoResumido value) {
        this.procedimentosAnexados = value;
    }

}
