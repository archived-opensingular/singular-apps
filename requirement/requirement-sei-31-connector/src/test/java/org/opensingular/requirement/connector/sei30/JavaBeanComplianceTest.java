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

package org.opensingular.requirement.connector.sei30;

import org.junit.Test;
import org.opensingular.requirement.connector.sei31.builder.DocumentoBuilder;
import org.opensingular.requirement.connector.sei31.builder.ProcedimentoBuilder;
import org.opensingular.requirement.connector.sei31.model.SerieEnum;
import org.opensingular.requirement.connector.sei31.ws.Andamento;
import org.opensingular.requirement.connector.sei31.ws.AndamentoMarcador;
import org.opensingular.requirement.connector.sei31.ws.ArquivoExtensao;
import org.opensingular.requirement.connector.sei31.ws.ArrayOfAndamento;
import org.opensingular.requirement.connector.sei31.ws.ArrayOfAndamentoMarcador;
import org.opensingular.requirement.connector.sei31.ws.ArrayOfArquivoExtensao;
import org.opensingular.requirement.connector.sei31.ws.ArrayOfAssinatura;
import org.opensingular.requirement.connector.sei31.ws.ArrayOfAssunto;
import org.opensingular.requirement.connector.sei31.ws.ArrayOfAtributoAndamento;
import org.opensingular.requirement.connector.sei31.ws.ArrayOfCampo;
import org.opensingular.requirement.connector.sei31.ws.ArrayOfCargo;
import org.opensingular.requirement.connector.sei31.ws.ArrayOfCidade;
import org.opensingular.requirement.connector.sei31.ws.ArrayOfContato;
import org.opensingular.requirement.connector.sei31.ws.ArrayOfDefinicaoMarcador;
import org.opensingular.requirement.connector.sei31.ws.ArrayOfDestinatario;
import org.opensingular.requirement.connector.sei31.ws.ArrayOfDocumento;
import org.opensingular.requirement.connector.sei31.ws.ArrayOfDocumentoFormatado;
import org.opensingular.requirement.connector.sei31.ws.ArrayOfEstado;
import org.opensingular.requirement.connector.sei31.ws.ArrayOfHipoteseLegal;
import org.opensingular.requirement.connector.sei31.ws.ArrayOfIdUnidade;
import org.opensingular.requirement.connector.sei31.ws.ArrayOfInteressado;
import org.opensingular.requirement.connector.sei31.ws.ArrayOfMarcador;
import org.opensingular.requirement.connector.sei31.ws.ArrayOfObservacao;
import org.opensingular.requirement.connector.sei31.ws.ArrayOfPais;
import org.opensingular.requirement.connector.sei31.ws.ArrayOfProcedimentoRelacionado;
import org.opensingular.requirement.connector.sei31.ws.ArrayOfProcedimentoResumido;
import org.opensingular.requirement.connector.sei31.ws.ArrayOfProtocoloBloco;
import org.opensingular.requirement.connector.sei31.ws.ArrayOfRetornoInclusaoDocumento;
import org.opensingular.requirement.connector.sei31.ws.ArrayOfSerie;
import org.opensingular.requirement.connector.sei31.ws.ArrayOfTipoConferencia;
import org.opensingular.requirement.connector.sei31.ws.ArrayOfTipoProcedimento;
import org.opensingular.requirement.connector.sei31.ws.ArrayOfUnidade;
import org.opensingular.requirement.connector.sei31.ws.ArrayOfUnidadeProcedimentoAberto;
import org.opensingular.requirement.connector.sei31.ws.ArrayOfUsuario;
import org.opensingular.requirement.connector.sei31.ws.Assinatura;
import org.opensingular.requirement.connector.sei31.ws.Assunto;
import org.opensingular.requirement.connector.sei31.ws.AtributoAndamento;
import org.opensingular.requirement.connector.sei31.ws.Campo;
import org.opensingular.requirement.connector.sei31.ws.Cargo;
import org.opensingular.requirement.connector.sei31.ws.Cidade;
import org.opensingular.requirement.connector.sei31.ws.Contato;
import org.opensingular.requirement.connector.sei31.ws.DefinicaoMarcador;
import org.opensingular.requirement.connector.sei31.ws.Destinatario;
import org.opensingular.requirement.connector.sei31.ws.Documento;
import org.opensingular.requirement.connector.sei31.ws.Estado;
import org.opensingular.requirement.connector.sei31.ws.HipoteseLegal;
import org.opensingular.requirement.connector.sei31.ws.Interessado;
import org.opensingular.requirement.connector.sei31.ws.Marcador;
import org.opensingular.requirement.connector.sei31.ws.ObjectFactory;
import org.opensingular.requirement.connector.sei31.ws.Observacao;
import org.opensingular.requirement.connector.sei31.ws.Pais;
import org.opensingular.requirement.connector.sei31.ws.Procedimento;
import org.opensingular.requirement.connector.sei31.ws.ProcedimentoResumido;
import org.opensingular.requirement.connector.sei31.ws.ProtocoloBloco;
import org.opensingular.requirement.connector.sei31.ws.Publicacao;
import org.opensingular.requirement.connector.sei31.ws.PublicacaoImprensaNacional;
import org.opensingular.requirement.connector.sei31.ws.Remetente;
import org.opensingular.requirement.connector.sei31.ws.RetornoConsultaBloco;
import org.opensingular.requirement.connector.sei31.ws.RetornoConsultaDocumento;
import org.opensingular.requirement.connector.sei31.ws.RetornoConsultaProcedimento;
import org.opensingular.requirement.connector.sei31.ws.RetornoGeracaoProcedimento;
import org.opensingular.requirement.connector.sei31.ws.RetornoInclusaoDocumento;
import org.opensingular.requirement.connector.sei31.ws.Serie;
import org.opensingular.requirement.connector.sei31.ws.TipoConferencia;
import org.opensingular.requirement.connector.sei31.ws.Unidade;
import org.opensingular.requirement.connector.sei31.ws.UnidadeProcedimentoAberto;
import org.opensingular.requirement.connector.sei31.ws.Usuario;

public class JavaBeanComplianceTest {

    @Test
    public void enumTest() throws Exception {
        SerieEnum.DESPACHO.getNome();

    }


    @Test
    public void testDocumentobuilder() {
        DocumentoBuilder documentobuilder = new DocumentoBuilder();
    }

    @Test
    public void testProcedimentobuilder() {
        ProcedimentoBuilder procedimentobuilder = new ProcedimentoBuilder();
    }


    @Test
    public void testAndamento() {
        Andamento andamento = new ObjectFactory().createAndamento();
        andamento.setIdAndamento(andamento.getIdAndamento());
        andamento.setIdTarefa(andamento.getIdTarefa());
        andamento.setIdTarefaModulo(andamento.getIdTarefaModulo());
        andamento.setDataHora(andamento.getDataHora());
        andamento.setAtributos(andamento.getAtributos());
        andamento.setUsuario(andamento.getUsuario());
        andamento.setUnidade(andamento.getUnidade());
        andamento.setDescricao(andamento.getDescricao());
    }

    @Test
    public void testAndamentomarcador() {
        AndamentoMarcador andamentomarcador = new ObjectFactory().createAndamentoMarcador();
        andamentomarcador.setDataHora(andamentomarcador.getDataHora());
        andamentomarcador.setIdAndamentoMarcador(andamentomarcador.getIdAndamentoMarcador());
        andamentomarcador.setTexto(andamentomarcador.getTexto());
        andamentomarcador.setMarcador(andamentomarcador.getMarcador());
        andamentomarcador.setUsuario(andamentomarcador.getUsuario());
    }

    @Test
    public void testArquivoextensao() {
        ArquivoExtensao arquivoextensao = new ObjectFactory().createArquivoExtensao();
        arquivoextensao.setIdArquivoExtensao(arquivoextensao.getIdArquivoExtensao());
        arquivoextensao.setExtensao(arquivoextensao.getExtensao());
        arquivoextensao.setDescricao(arquivoextensao.getDescricao());
    }

    @Test
    public void testArrayofandamento() {
        ArrayOfAndamento arrayofandamento = new ObjectFactory().createArrayOfAndamento();
    }

    @Test
    public void testArrayofandamentomarcador() {
        ArrayOfAndamentoMarcador arrayofandamentomarcador = new ObjectFactory().createArrayOfAndamentoMarcador();
    }

    @Test
    public void testArrayofarquivoextensao() {
        ArrayOfArquivoExtensao arrayofarquivoextensao = new ObjectFactory().createArrayOfArquivoExtensao();
    }

    @Test
    public void testArrayofassinatura() {
        ArrayOfAssinatura arrayofassinatura = new ObjectFactory().createArrayOfAssinatura();
    }

    @Test
    public void testArrayofassunto() {
        ArrayOfAssunto arrayofassunto = new ObjectFactory().createArrayOfAssunto();
    }

    @Test
    public void testArrayofatributoandamento() {
        ArrayOfAtributoAndamento arrayofatributoandamento = new ObjectFactory().createArrayOfAtributoAndamento();
    }

    @Test
    public void testArrayofcampo() {
        ArrayOfCampo arrayofcampo = new ObjectFactory().createArrayOfCampo();
    }

    @Test
    public void testArrayofcargo() {
        ArrayOfCargo arrayofcargo = new ObjectFactory().createArrayOfCargo();
    }

    @Test
    public void testArrayofcidade() {
        ArrayOfCidade arrayofcidade = new ObjectFactory().createArrayOfCidade();
    }

    @Test
    public void testArrayofcontato() {
        ArrayOfContato arrayofcontato = new ObjectFactory().createArrayOfContato();
    }

    @Test
    public void testArrayofdefinicaomarcador() {
        ArrayOfDefinicaoMarcador arrayofdefinicaomarcador = new ObjectFactory().createArrayOfDefinicaoMarcador();
    }

    @Test
    public void testArrayofdestinatario() {
        ArrayOfDestinatario arrayofdestinatario = new ObjectFactory().createArrayOfDestinatario();
    }

    @Test
    public void testArrayofdocumento() {
        ArrayOfDocumento arrayofdocumento = new ObjectFactory().createArrayOfDocumento();
    }

    @Test
    public void testArrayofdocumentoformatado() {
        ArrayOfDocumentoFormatado arrayofdocumentoformatado = new ObjectFactory().createArrayOfDocumentoFormatado();
    }

    @Test
    public void testArrayofestado() {
        ArrayOfEstado arrayofestado = new ObjectFactory().createArrayOfEstado();
    }

    @Test
    public void testArrayofhipoteselegal() {
        ArrayOfHipoteseLegal arrayofhipoteselegal = new ObjectFactory().createArrayOfHipoteseLegal();
    }

    @Test
    public void testArrayofidunidade() {
        ArrayOfIdUnidade arrayofidunidade = new ObjectFactory().createArrayOfIdUnidade();
    }

    @Test
    public void testArrayofinteressado() {
        ArrayOfInteressado arrayofinteressado = new ObjectFactory().createArrayOfInteressado();
    }

    @Test
    public void testArrayofmarcador() {
        ArrayOfMarcador arrayofmarcador = new ObjectFactory().createArrayOfMarcador();
    }

    @Test
    public void testArrayofobservacao() {
        ArrayOfObservacao arrayofobservacao = new ObjectFactory().createArrayOfObservacao();
    }

    @Test
    public void testArrayofpais() {
        ArrayOfPais arrayofpais = new ObjectFactory().createArrayOfPais();
    }

    @Test
    public void testArrayofprocedimentorelacionado() {
        ArrayOfProcedimentoRelacionado arrayofprocedimentorelacionado = new ObjectFactory().createArrayOfProcedimentoRelacionado();
    }

    @Test
    public void testArrayofprocedimentoresumido() {
        ArrayOfProcedimentoResumido arrayofprocedimentoresumido = new ObjectFactory().createArrayOfProcedimentoResumido();
    }

    @Test
    public void testArrayofprotocolobloco() {
        ArrayOfProtocoloBloco arrayofprotocolobloco = new ObjectFactory().createArrayOfProtocoloBloco();
    }

    @Test
    public void testArrayofretornoinclusaodocumento() {
        ArrayOfRetornoInclusaoDocumento arrayofretornoinclusaodocumento = new ObjectFactory().createArrayOfRetornoInclusaoDocumento();
    }

    @Test
    public void testArrayofserie() {
        ArrayOfSerie arrayofserie = new ObjectFactory().createArrayOfSerie();
    }

    @Test
    public void testArrayoftipoconferencia() {
        ArrayOfTipoConferencia arrayoftipoconferencia = new ObjectFactory().createArrayOfTipoConferencia();
    }

    @Test
    public void testArrayoftipoprocedimento() {
        ArrayOfTipoProcedimento arrayoftipoprocedimento = new ObjectFactory().createArrayOfTipoProcedimento();
    }

    @Test
    public void testArrayofunidade() {
        ArrayOfUnidade arrayofunidade = new ObjectFactory().createArrayOfUnidade();
    }

    @Test
    public void testArrayofunidadeprocedimentoaberto() {
        ArrayOfUnidadeProcedimentoAberto arrayofunidadeprocedimentoaberto = new ObjectFactory().createArrayOfUnidadeProcedimentoAberto();
    }

    @Test
    public void testArrayofusuario() {
        ArrayOfUsuario arrayofusuario = new ObjectFactory().createArrayOfUsuario();
    }

    @Test
    public void testAssinatura() {
        Assinatura assinatura = new ObjectFactory().createAssinatura();
        assinatura.setNome(assinatura.getNome());
        assinatura.setDataHora(assinatura.getDataHora());
        assinatura.setCargoFuncao(assinatura.getCargoFuncao());
        assinatura.setIdUsuario(assinatura.getIdUsuario());
        assinatura.setIdOrigem(assinatura.getIdOrigem());
        assinatura.setIdOrgao(assinatura.getIdOrgao());
        assinatura.setSigla(assinatura.getSigla());
    }

    @Test
    public void testAssunto() {
        Assunto assunto = new ObjectFactory().createAssunto();
        assunto.setDescricao(assunto.getDescricao());
        assunto.setCodigoEstruturado(assunto.getCodigoEstruturado());
    }

    @Test
    public void testAtributoandamento() {
        AtributoAndamento atributoandamento = new ObjectFactory().createAtributoAndamento();
        atributoandamento.setNome(atributoandamento.getNome());
        atributoandamento.setValor(atributoandamento.getValor());
        atributoandamento.setIdOrigem(atributoandamento.getIdOrigem());
    }

    @Test
    public void testCampo() {
        Campo campo = new ObjectFactory().createCampo();
        campo.setNome(campo.getNome());
        campo.setValor(campo.getValor());
    }

    @Test
    public void testCargo() {
        Cargo cargo = new ObjectFactory().createCargo();
        cargo.setIdCargo(cargo.getIdCargo());
        cargo.setExpressaoCargo(cargo.getExpressaoCargo());
        cargo.setExpressaoTratamento(cargo.getExpressaoTratamento());
        cargo.setExpressaoVocativo(cargo.getExpressaoVocativo());
    }

    @Test
    public void testCidade() {
        Cidade cidade = new ObjectFactory().createCidade();
        cidade.setNome(cidade.getNome());
        cidade.setIdCidade(cidade.getIdCidade());
        cidade.setIdEstado(cidade.getIdEstado());
        cidade.setIdPais(cidade.getIdPais());
        cidade.setCodigoIbge(cidade.getCodigoIbge());
        cidade.setSinCapital(cidade.getSinCapital());
        cidade.setLatitude(cidade.getLatitude());
        cidade.setLongitude(cidade.getLongitude());
    }

    @Test
    public void testContato() {
        Contato contato = new ObjectFactory().createContato();
        contato.setNome(contato.getNome());
        contato.setSigla(contato.getSigla());
        contato.setSinAtivo(contato.getSinAtivo());
        contato.setIdCidade(contato.getIdCidade());
        contato.setIdEstado(contato.getIdEstado());
        contato.setIdPais(contato.getIdPais());
        contato.setObservacao(contato.getObservacao());
        contato.setIdCargo(contato.getIdCargo());
        contato.setExpressaoCargo(contato.getExpressaoCargo());
        contato.setExpressaoTratamento(contato.getExpressaoTratamento());
        contato.setExpressaoVocativo(contato.getExpressaoVocativo());
        contato.setStaOperacao(contato.getStaOperacao());
        contato.setIdContato(contato.getIdContato());
        contato.setIdTipoContato(contato.getIdTipoContato());
        contato.setNomeTipoContato(contato.getNomeTipoContato());
        contato.setStaNatureza(contato.getStaNatureza());
        contato.setIdContatoAssociado(contato.getIdContatoAssociado());
        contato.setNomeContatoAssociado(contato.getNomeContatoAssociado());
        contato.setSinEnderecoAssociado(contato.getSinEnderecoAssociado());
        contato.setCnpjAssociado(contato.getCnpjAssociado());
        contato.setEndereco(contato.getEndereco());
        contato.setComplemento(contato.getComplemento());
        contato.setBairro(contato.getBairro());
        contato.setNomeCidade(contato.getNomeCidade());
        contato.setSiglaEstado(contato.getSiglaEstado());
        contato.setNomePais(contato.getNomePais());
        contato.setCpf(contato.getCpf());
        contato.setCep(contato.getCep());
        contato.setCnpj(contato.getCnpj());
        contato.setRg(contato.getRg());
        contato.setEmail(contato.getEmail());
        contato.setStaGenero(contato.getStaGenero());
        contato.setOrgaoExpedidor(contato.getOrgaoExpedidor());
        contato.setMatricula(contato.getMatricula());
        contato.setMatriculaOab(contato.getMatriculaOab());
        contato.setTelefoneFixo(contato.getTelefoneFixo());
        contato.setTelefoneCelular(contato.getTelefoneCelular());
        contato.setDataNascimento(contato.getDataNascimento());
        contato.setSitioInternet(contato.getSitioInternet());
    }

    @Test
    public void testDefinicaomarcador() {
        DefinicaoMarcador definicaomarcador = new ObjectFactory().createDefinicaoMarcador();
        definicaomarcador.setIdMarcador(definicaomarcador.getIdMarcador());
        definicaomarcador.setTexto(definicaomarcador.getTexto());
        definicaomarcador.setProtocoloProcedimento(definicaomarcador.getProtocoloProcedimento());
    }

    @Test
    public void testDestinatario() {
        Destinatario destinatario = new ObjectFactory().createDestinatario();
        destinatario.setNome(destinatario.getNome());
        destinatario.setSigla(destinatario.getSigla());
    }

    @Test
    public void testDocumento() {
        Documento documento = new ObjectFactory().createDocumento();
        documento.setInteressados(documento.getInteressados());
        documento.setObservacao(documento.getObservacao());
        documento.setNivelAcesso(documento.getNivelAcesso());
        documento.setIdHipoteseLegal(documento.getIdHipoteseLegal());
        documento.setIdProcedimento(documento.getIdProcedimento());
        documento.setTipo(documento.getTipo());
        documento.setProtocoloProcedimento(documento.getProtocoloProcedimento());
        documento.setIdSerie(documento.getIdSerie());
        documento.setNumero(documento.getNumero());
        documento.setData(documento.getData());
        documento.setDescricao(documento.getDescricao());
        documento.setIdTipoConferencia(documento.getIdTipoConferencia());
        documento.setRemetente(documento.getRemetente());
        documento.setDestinatarios(documento.getDestinatarios());
        documento.setNomeArquivo(documento.getNomeArquivo());
        documento.setConteudo(documento.getConteudo());
        documento.setConteudoMTOM(documento.getConteudoMTOM());
        documento.setIdArquivo(documento.getIdArquivo());
        documento.setCampos(documento.getCampos());
        documento.setSinBloqueado(documento.getSinBloqueado());
    }

    @Test
    public void testEstado() {
        Estado estado = new ObjectFactory().createEstado();
        estado.setNome(estado.getNome());
        estado.setSigla(estado.getSigla());
        estado.setIdEstado(estado.getIdEstado());
        estado.setIdPais(estado.getIdPais());
        estado.setCodigoIbge(estado.getCodigoIbge());
    }

    @Test
    public void testHipoteselegal() {
        HipoteseLegal hipoteselegal = new ObjectFactory().createHipoteseLegal();
        hipoteselegal.setNome(hipoteselegal.getNome());
        hipoteselegal.setNivelAcesso(hipoteselegal.getNivelAcesso());
        hipoteselegal.setIdHipoteseLegal(hipoteselegal.getIdHipoteseLegal());
        hipoteselegal.setBaseLegal(hipoteselegal.getBaseLegal());
    }

    @Test
    public void testInteressado() {
        Interessado interessado = new ObjectFactory().createInteressado();
        interessado.setNome(interessado.getNome());
        interessado.setSigla(interessado.getSigla());
    }

    @Test
    public void testMarcador() {
        Marcador marcador = new ObjectFactory().createMarcador();
        marcador.setNome(marcador.getNome());
        marcador.setIdMarcador(marcador.getIdMarcador());
        marcador.setIcone(marcador.getIcone());
        marcador.setSinAtivo(marcador.getSinAtivo());
    }

    @Test
    public void testObservacao() {
        Observacao observacao = new ObjectFactory().createObservacao();
        observacao.setUnidade(observacao.getUnidade());
        observacao.setDescricao(observacao.getDescricao());
    }

    @Test
    public void testPais() {
        Pais pais = new ObjectFactory().createPais();
        pais.setNome(pais.getNome());
        pais.setIdPais(pais.getIdPais());
    }

    @Test
    public void testProcedimento() {
        Procedimento procedimento = new ObjectFactory().createProcedimento();
        procedimento.setIdTipoProcedimento(procedimento.getIdTipoProcedimento());
        procedimento.setNumeroProtocolo(procedimento.getNumeroProtocolo());
        procedimento.setDataAutuacao(procedimento.getDataAutuacao());
        procedimento.setEspecificacao(procedimento.getEspecificacao());
        procedimento.setAssuntos(procedimento.getAssuntos());
        procedimento.setInteressados(procedimento.getInteressados());
        procedimento.setObservacao(procedimento.getObservacao());
        procedimento.setNivelAcesso(procedimento.getNivelAcesso());
        procedimento.setIdHipoteseLegal(procedimento.getIdHipoteseLegal());
    }

    @Test
    public void testProcedimentoresumido() {
        ProcedimentoResumido procedimentoresumido = new ObjectFactory().createProcedimentoResumido();
        procedimentoresumido.setIdProcedimento(procedimentoresumido.getIdProcedimento());
        procedimentoresumido.setProcedimentoFormatado(procedimentoresumido.getProcedimentoFormatado());
        procedimentoresumido.setTipoProcedimento(procedimentoresumido.getTipoProcedimento());
    }

    @Test
    public void testProtocolobloco() {
        ProtocoloBloco protocolobloco = new ObjectFactory().createProtocoloBloco();
        protocolobloco.setAssinaturas(protocolobloco.getAssinaturas());
        protocolobloco.setProtocoloFormatado(protocolobloco.getProtocoloFormatado());
        protocolobloco.setIdentificacao(protocolobloco.getIdentificacao());
    }

    @Test
    public void testPublicacao() {
        Publicacao publicacao = new ObjectFactory().createPublicacao();
        publicacao.setEstado(publicacao.getEstado());
        publicacao.setNumero(publicacao.getNumero());
        publicacao.setNomeVeiculo(publicacao.getNomeVeiculo());
        publicacao.setDataDisponibilizacao(publicacao.getDataDisponibilizacao());
        publicacao.setDataPublicacao(publicacao.getDataPublicacao());
        publicacao.setImprensaNacional(publicacao.getImprensaNacional());
    }

    @Test
    public void testPublicacaoimprensanacional() {
        PublicacaoImprensaNacional publicacaoimprensanacional = new ObjectFactory().createPublicacaoImprensaNacional();
        publicacaoimprensanacional.setSecao(publicacaoimprensanacional.getSecao());
        publicacaoimprensanacional.setSiglaVeiculo(publicacaoimprensanacional.getSiglaVeiculo());
        publicacaoimprensanacional.setDescricaoVeiculo(publicacaoimprensanacional.getDescricaoVeiculo());
        publicacaoimprensanacional.setPagina(publicacaoimprensanacional.getPagina());
        publicacaoimprensanacional.setData(publicacaoimprensanacional.getData());
    }

    @Test
    public void testRemetente() {
        Remetente remetente = new ObjectFactory().createRemetente();
        remetente.setNome(remetente.getNome());
        remetente.setSigla(remetente.getSigla());
    }

    @Test
    public void testRetornoconsultabloco() {
        RetornoConsultaBloco retornoconsultabloco = new ObjectFactory().createRetornoConsultaBloco();
        retornoconsultabloco.setUsuario(retornoconsultabloco.getUsuario());
        retornoconsultabloco.setEstado(retornoconsultabloco.getEstado());
        retornoconsultabloco.setUnidadesDisponibilizacao(retornoconsultabloco.getUnidadesDisponibilizacao());
        retornoconsultabloco.setProtocolos(retornoconsultabloco.getProtocolos());
        retornoconsultabloco.setIdBloco(retornoconsultabloco.getIdBloco());
        retornoconsultabloco.setUnidade(retornoconsultabloco.getUnidade());
        retornoconsultabloco.setTipo(retornoconsultabloco.getTipo());
        retornoconsultabloco.setDescricao(retornoconsultabloco.getDescricao());
    }

    @Test
    public void testRetornoconsultadocumento() {
        RetornoConsultaDocumento retornoconsultadocumento = new ObjectFactory().createRetornoConsultaDocumento();
        retornoconsultadocumento.setSerie(retornoconsultadocumento.getSerie());
        retornoconsultadocumento.setUnidadeElaboradora(retornoconsultadocumento.getUnidadeElaboradora());
        retornoconsultadocumento.setAssinaturas(retornoconsultadocumento.getAssinaturas());
        retornoconsultadocumento.setPublicacao(retornoconsultadocumento.getPublicacao());
        retornoconsultadocumento.setIdProcedimento(retornoconsultadocumento.getIdProcedimento());
        retornoconsultadocumento.setProcedimentoFormatado(retornoconsultadocumento.getProcedimentoFormatado());
        retornoconsultadocumento.setLinkAcesso(retornoconsultadocumento.getLinkAcesso());
        retornoconsultadocumento.setAndamentoGeracao(retornoconsultadocumento.getAndamentoGeracao());
        retornoconsultadocumento.setNumero(retornoconsultadocumento.getNumero());
        retornoconsultadocumento.setData(retornoconsultadocumento.getData());
        retornoconsultadocumento.setCampos(retornoconsultadocumento.getCampos());
        retornoconsultadocumento.setIdDocumento(retornoconsultadocumento.getIdDocumento());
        retornoconsultadocumento.setDocumentoFormatado(retornoconsultadocumento.getDocumentoFormatado());
    }

    @Test
    public void testRetornoconsultaprocedimento() {
        RetornoConsultaProcedimento retornoconsultaprocedimento = new ObjectFactory().createRetornoConsultaProcedimento();
        retornoconsultaprocedimento.setDataAutuacao(retornoconsultaprocedimento.getDataAutuacao());
        retornoconsultaprocedimento.setEspecificacao(retornoconsultaprocedimento.getEspecificacao());
        retornoconsultaprocedimento.setAssuntos(retornoconsultaprocedimento.getAssuntos());
        retornoconsultaprocedimento.setInteressados(retornoconsultaprocedimento.getInteressados());
        retornoconsultaprocedimento.setIdProcedimento(retornoconsultaprocedimento.getIdProcedimento());
        retornoconsultaprocedimento.setProcedimentoFormatado(retornoconsultaprocedimento.getProcedimentoFormatado());
        retornoconsultaprocedimento.setLinkAcesso(retornoconsultaprocedimento.getLinkAcesso());
        retornoconsultaprocedimento.setTipoProcedimento(retornoconsultaprocedimento.getTipoProcedimento());
        retornoconsultaprocedimento.setAndamentoGeracao(retornoconsultaprocedimento.getAndamentoGeracao());
        retornoconsultaprocedimento.setAndamentoConclusao(retornoconsultaprocedimento.getAndamentoConclusao());
        retornoconsultaprocedimento.setUltimoAndamento(retornoconsultaprocedimento.getUltimoAndamento());
        retornoconsultaprocedimento.setUnidadesProcedimentoAberto(retornoconsultaprocedimento.getUnidadesProcedimentoAberto());
        retornoconsultaprocedimento.setObservacoes(retornoconsultaprocedimento.getObservacoes());
        retornoconsultaprocedimento.setProcedimentosRelacionados(retornoconsultaprocedimento.getProcedimentosRelacionados());
        retornoconsultaprocedimento.setProcedimentosAnexados(retornoconsultaprocedimento.getProcedimentosAnexados());
    }

    @Test
    public void testRetornogeracaoprocedimento() {
        RetornoGeracaoProcedimento retornogeracaoprocedimento = new ObjectFactory().createRetornoGeracaoProcedimento();
        retornogeracaoprocedimento.setIdProcedimento(retornogeracaoprocedimento.getIdProcedimento());
        retornogeracaoprocedimento.setProcedimentoFormatado(retornogeracaoprocedimento.getProcedimentoFormatado());
        retornogeracaoprocedimento.setLinkAcesso(retornogeracaoprocedimento.getLinkAcesso());
        retornogeracaoprocedimento.setRetornoInclusaoDocumentos(retornogeracaoprocedimento.getRetornoInclusaoDocumentos());
    }

    @Test
    public void testRetornoinclusaodocumento() {
        RetornoInclusaoDocumento retornoinclusaodocumento = new ObjectFactory().createRetornoInclusaoDocumento();
        retornoinclusaodocumento.setLinkAcesso(retornoinclusaodocumento.getLinkAcesso());
        retornoinclusaodocumento.setIdDocumento(retornoinclusaodocumento.getIdDocumento());
        retornoinclusaodocumento.setDocumentoFormatado(retornoinclusaodocumento.getDocumentoFormatado());
    }


    @Test
    public void testSerie() {
        Serie serie = new ObjectFactory().createSerie();
        serie.setNome(serie.getNome());
        serie.setIdSerie(serie.getIdSerie());
        serie.setAplicabilidade(serie.getAplicabilidade());
    }

    @Test
    public void testTipoconferencia() {
        TipoConferencia tipoconferencia = new ObjectFactory().createTipoConferencia();
        tipoconferencia.setDescricao(tipoconferencia.getDescricao());
        tipoconferencia.setIdTipoConferencia(tipoconferencia.getIdTipoConferencia());
    }

    @Test
    public void testUnidade() {
        Unidade unidade = new ObjectFactory().createUnidade();
        unidade.setSigla(unidade.getSigla());
        unidade.setIdUnidade(unidade.getIdUnidade());
        unidade.setDescricao(unidade.getDescricao());
        unidade.setSinProtocolo(unidade.getSinProtocolo());
        unidade.setSinArquivamento(unidade.getSinArquivamento());
        unidade.setSinOuvidoria(unidade.getSinOuvidoria());
    }

    @Test
    public void testUnidadeprocedimentoaberto() {
        UnidadeProcedimentoAberto unidadeprocedimentoaberto = new ObjectFactory().createUnidadeProcedimentoAberto();
        unidadeprocedimentoaberto.setUnidade(unidadeprocedimentoaberto.getUnidade());
        unidadeprocedimentoaberto.setUsuarioAtribuicao(unidadeprocedimentoaberto.getUsuarioAtribuicao());
    }

    @Test
    public void testUsuario() {
        Usuario usuario = new ObjectFactory().createUsuario();
        usuario.setNome(usuario.getNome());
        usuario.setSigla(usuario.getSigla());
        usuario.setIdUsuario(usuario.getIdUsuario());
    }


}
