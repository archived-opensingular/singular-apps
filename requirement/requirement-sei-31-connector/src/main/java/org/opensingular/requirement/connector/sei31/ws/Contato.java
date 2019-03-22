
package org.opensingular.requirement.connector.sei31.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de Contato complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="Contato">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="StaOperacao" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="IdContato" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="IdTipoContato" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="NomeTipoContato" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Sigla" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Nome" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="StaNatureza" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="IdContatoAssociado" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="NomeContatoAssociado" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SinEnderecoAssociado" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="CnpjAssociado" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Endereco" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Complemento" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Bairro" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="IdCidade" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="NomeCidade" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="IdEstado" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="SiglaEstado" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="IdPais" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="NomePais" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Cep" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="StaGenero" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="IdCargo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ExpressaoCargo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ExpressaoTratamento" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ExpressaoVocativo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Cpf" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Cnpj" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Rg" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="OrgaoExpedidor" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="NumeroPassaporte" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="IdPaisPassaporte" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NomePaisPassaporte" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Matricula" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="MatriculaOab" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="TelefoneFixo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="TelefoneCelular" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="DataNascimento" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Email" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="SitioInternet" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Observacao" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
@XmlType(name = "Contato", propOrder = {

})
public class Contato {

    @XmlElement(name = "StaOperacao")
    protected String staOperacao;
    @XmlElement(name = "IdContato", required = true)
    protected String idContato;
    @XmlElement(name = "IdTipoContato", required = true)
    protected String idTipoContato;
    @XmlElement(name = "NomeTipoContato")
    protected String nomeTipoContato;
    @XmlElement(name = "Sigla", required = true)
    protected String sigla;
    @XmlElement(name = "Nome", required = true)
    protected String nome;
    @XmlElement(name = "StaNatureza", required = true)
    protected String staNatureza;
    @XmlElement(name = "IdContatoAssociado", required = true)
    protected String idContatoAssociado;
    @XmlElement(name = "NomeContatoAssociado")
    protected String nomeContatoAssociado;
    @XmlElement(name = "SinEnderecoAssociado", required = true)
    protected String sinEnderecoAssociado;
    @XmlElement(name = "CnpjAssociado")
    protected String cnpjAssociado;
    @XmlElement(name = "Endereco", required = true)
    protected String endereco;
    @XmlElement(name = "Complemento", required = true)
    protected String complemento;
    @XmlElement(name = "Bairro", required = true)
    protected String bairro;
    @XmlElement(name = "IdCidade", required = true)
    protected String idCidade;
    @XmlElement(name = "NomeCidade")
    protected String nomeCidade;
    @XmlElement(name = "IdEstado", required = true)
    protected String idEstado;
    @XmlElement(name = "SiglaEstado")
    protected String siglaEstado;
    @XmlElement(name = "IdPais", required = true)
    protected String idPais;
    @XmlElement(name = "NomePais")
    protected String nomePais;
    @XmlElement(name = "Cep", required = true)
    protected String cep;
    @XmlElement(name = "StaGenero", required = true)
    protected String staGenero;
    @XmlElement(name = "IdCargo", required = true)
    protected String idCargo;
    @XmlElement(name = "ExpressaoCargo")
    protected String expressaoCargo;
    @XmlElement(name = "ExpressaoTratamento")
    protected String expressaoTratamento;
    @XmlElement(name = "ExpressaoVocativo")
    protected String expressaoVocativo;
    @XmlElement(name = "Cpf", required = true)
    protected String cpf;
    @XmlElement(name = "Cnpj", required = true)
    protected String cnpj;
    @XmlElement(name = "Rg", required = true)
    protected String rg;
    @XmlElement(name = "OrgaoExpedidor", required = true)
    protected String orgaoExpedidor;
    @XmlElement(name = "NumeroPassaporte")
    protected String numeroPassaporte;
    @XmlElement(name = "IdPaisPassaporte")
    protected String idPaisPassaporte;
    @XmlElement(name = "NomePaisPassaporte")
    protected String nomePaisPassaporte;
    @XmlElement(name = "Matricula", required = true)
    protected String matricula;
    @XmlElement(name = "MatriculaOab", required = true)
    protected String matriculaOab;
    @XmlElement(name = "TelefoneFixo", required = true)
    protected String telefoneFixo;
    @XmlElement(name = "TelefoneCelular", required = true)
    protected String telefoneCelular;
    @XmlElement(name = "DataNascimento", required = true)
    protected String dataNascimento;
    @XmlElement(name = "Email", required = true)
    protected String email;
    @XmlElement(name = "SitioInternet", required = true)
    protected String sitioInternet;
    @XmlElement(name = "Observacao", required = true)
    protected String observacao;
    @XmlElement(name = "SinAtivo", required = true)
    protected String sinAtivo;

    /**
     * Obtém o valor da propriedade staOperacao.
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
     * Define o valor da propriedade staOperacao.
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
     * Obtém o valor da propriedade idContato.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdContato() {
        return idContato;
    }

    /**
     * Define o valor da propriedade idContato.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdContato(String value) {
        this.idContato = value;
    }

    /**
     * Obtém o valor da propriedade idTipoContato.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdTipoContato() {
        return idTipoContato;
    }

    /**
     * Define o valor da propriedade idTipoContato.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdTipoContato(String value) {
        this.idTipoContato = value;
    }

    /**
     * Obtém o valor da propriedade nomeTipoContato.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNomeTipoContato() {
        return nomeTipoContato;
    }

    /**
     * Define o valor da propriedade nomeTipoContato.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNomeTipoContato(String value) {
        this.nomeTipoContato = value;
    }

    /**
     * Obtém o valor da propriedade sigla.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSigla() {
        return sigla;
    }

    /**
     * Define o valor da propriedade sigla.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSigla(String value) {
        this.sigla = value;
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
     * Obtém o valor da propriedade staNatureza.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStaNatureza() {
        return staNatureza;
    }

    /**
     * Define o valor da propriedade staNatureza.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStaNatureza(String value) {
        this.staNatureza = value;
    }

    /**
     * Obtém o valor da propriedade idContatoAssociado.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdContatoAssociado() {
        return idContatoAssociado;
    }

    /**
     * Define o valor da propriedade idContatoAssociado.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdContatoAssociado(String value) {
        this.idContatoAssociado = value;
    }

    /**
     * Obtém o valor da propriedade nomeContatoAssociado.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNomeContatoAssociado() {
        return nomeContatoAssociado;
    }

    /**
     * Define o valor da propriedade nomeContatoAssociado.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNomeContatoAssociado(String value) {
        this.nomeContatoAssociado = value;
    }

    /**
     * Obtém o valor da propriedade sinEnderecoAssociado.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSinEnderecoAssociado() {
        return sinEnderecoAssociado;
    }

    /**
     * Define o valor da propriedade sinEnderecoAssociado.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSinEnderecoAssociado(String value) {
        this.sinEnderecoAssociado = value;
    }

    /**
     * Obtém o valor da propriedade cnpjAssociado.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCnpjAssociado() {
        return cnpjAssociado;
    }

    /**
     * Define o valor da propriedade cnpjAssociado.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCnpjAssociado(String value) {
        this.cnpjAssociado = value;
    }

    /**
     * Obtém o valor da propriedade endereco.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEndereco() {
        return endereco;
    }

    /**
     * Define o valor da propriedade endereco.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEndereco(String value) {
        this.endereco = value;
    }

    /**
     * Obtém o valor da propriedade complemento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getComplemento() {
        return complemento;
    }

    /**
     * Define o valor da propriedade complemento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setComplemento(String value) {
        this.complemento = value;
    }

    /**
     * Obtém o valor da propriedade bairro.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBairro() {
        return bairro;
    }

    /**
     * Define o valor da propriedade bairro.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBairro(String value) {
        this.bairro = value;
    }

    /**
     * Obtém o valor da propriedade idCidade.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdCidade() {
        return idCidade;
    }

    /**
     * Define o valor da propriedade idCidade.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdCidade(String value) {
        this.idCidade = value;
    }

    /**
     * Obtém o valor da propriedade nomeCidade.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNomeCidade() {
        return nomeCidade;
    }

    /**
     * Define o valor da propriedade nomeCidade.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNomeCidade(String value) {
        this.nomeCidade = value;
    }

    /**
     * Obtém o valor da propriedade idEstado.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdEstado() {
        return idEstado;
    }

    /**
     * Define o valor da propriedade idEstado.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdEstado(String value) {
        this.idEstado = value;
    }

    /**
     * Obtém o valor da propriedade siglaEstado.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSiglaEstado() {
        return siglaEstado;
    }

    /**
     * Define o valor da propriedade siglaEstado.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSiglaEstado(String value) {
        this.siglaEstado = value;
    }

    /**
     * Obtém o valor da propriedade idPais.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdPais() {
        return idPais;
    }

    /**
     * Define o valor da propriedade idPais.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdPais(String value) {
        this.idPais = value;
    }

    /**
     * Obtém o valor da propriedade nomePais.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNomePais() {
        return nomePais;
    }

    /**
     * Define o valor da propriedade nomePais.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNomePais(String value) {
        this.nomePais = value;
    }

    /**
     * Obtém o valor da propriedade cep.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCep() {
        return cep;
    }

    /**
     * Define o valor da propriedade cep.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCep(String value) {
        this.cep = value;
    }

    /**
     * Obtém o valor da propriedade staGenero.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStaGenero() {
        return staGenero;
    }

    /**
     * Define o valor da propriedade staGenero.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStaGenero(String value) {
        this.staGenero = value;
    }

    /**
     * Obtém o valor da propriedade idCargo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdCargo() {
        return idCargo;
    }

    /**
     * Define o valor da propriedade idCargo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdCargo(String value) {
        this.idCargo = value;
    }

    /**
     * Obtém o valor da propriedade expressaoCargo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExpressaoCargo() {
        return expressaoCargo;
    }

    /**
     * Define o valor da propriedade expressaoCargo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExpressaoCargo(String value) {
        this.expressaoCargo = value;
    }

    /**
     * Obtém o valor da propriedade expressaoTratamento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExpressaoTratamento() {
        return expressaoTratamento;
    }

    /**
     * Define o valor da propriedade expressaoTratamento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExpressaoTratamento(String value) {
        this.expressaoTratamento = value;
    }

    /**
     * Obtém o valor da propriedade expressaoVocativo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExpressaoVocativo() {
        return expressaoVocativo;
    }

    /**
     * Define o valor da propriedade expressaoVocativo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExpressaoVocativo(String value) {
        this.expressaoVocativo = value;
    }

    /**
     * Obtém o valor da propriedade cpf.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCpf() {
        return cpf;
    }

    /**
     * Define o valor da propriedade cpf.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCpf(String value) {
        this.cpf = value;
    }

    /**
     * Obtém o valor da propriedade cnpj.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCnpj() {
        return cnpj;
    }

    /**
     * Define o valor da propriedade cnpj.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCnpj(String value) {
        this.cnpj = value;
    }

    /**
     * Obtém o valor da propriedade rg.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRg() {
        return rg;
    }

    /**
     * Define o valor da propriedade rg.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRg(String value) {
        this.rg = value;
    }

    /**
     * Obtém o valor da propriedade orgaoExpedidor.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrgaoExpedidor() {
        return orgaoExpedidor;
    }

    /**
     * Define o valor da propriedade orgaoExpedidor.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrgaoExpedidor(String value) {
        this.orgaoExpedidor = value;
    }

    /**
     * Obtém o valor da propriedade numeroPassaporte.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumeroPassaporte() {
        return numeroPassaporte;
    }

    /**
     * Define o valor da propriedade numeroPassaporte.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumeroPassaporte(String value) {
        this.numeroPassaporte = value;
    }

    /**
     * Obtém o valor da propriedade idPaisPassaporte.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdPaisPassaporte() {
        return idPaisPassaporte;
    }

    /**
     * Define o valor da propriedade idPaisPassaporte.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdPaisPassaporte(String value) {
        this.idPaisPassaporte = value;
    }

    /**
     * Obtém o valor da propriedade nomePaisPassaporte.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNomePaisPassaporte() {
        return nomePaisPassaporte;
    }

    /**
     * Define o valor da propriedade nomePaisPassaporte.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNomePaisPassaporte(String value) {
        this.nomePaisPassaporte = value;
    }

    /**
     * Obtém o valor da propriedade matricula.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMatricula() {
        return matricula;
    }

    /**
     * Define o valor da propriedade matricula.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMatricula(String value) {
        this.matricula = value;
    }

    /**
     * Obtém o valor da propriedade matriculaOab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMatriculaOab() {
        return matriculaOab;
    }

    /**
     * Define o valor da propriedade matriculaOab.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMatriculaOab(String value) {
        this.matriculaOab = value;
    }

    /**
     * Obtém o valor da propriedade telefoneFixo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTelefoneFixo() {
        return telefoneFixo;
    }

    /**
     * Define o valor da propriedade telefoneFixo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTelefoneFixo(String value) {
        this.telefoneFixo = value;
    }

    /**
     * Obtém o valor da propriedade telefoneCelular.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTelefoneCelular() {
        return telefoneCelular;
    }

    /**
     * Define o valor da propriedade telefoneCelular.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTelefoneCelular(String value) {
        this.telefoneCelular = value;
    }

    /**
     * Obtém o valor da propriedade dataNascimento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDataNascimento() {
        return dataNascimento;
    }

    /**
     * Define o valor da propriedade dataNascimento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataNascimento(String value) {
        this.dataNascimento = value;
    }

    /**
     * Obtém o valor da propriedade email.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmail() {
        return email;
    }

    /**
     * Define o valor da propriedade email.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmail(String value) {
        this.email = value;
    }

    /**
     * Obtém o valor da propriedade sitioInternet.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSitioInternet() {
        return sitioInternet;
    }

    /**
     * Define o valor da propriedade sitioInternet.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSitioInternet(String value) {
        this.sitioInternet = value;
    }

    /**
     * Obtém o valor da propriedade observacao.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getObservacao() {
        return observacao;
    }

    /**
     * Define o valor da propriedade observacao.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setObservacao(String value) {
        this.observacao = value;
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
