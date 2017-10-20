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

package org.opensingular.server.connector.sei30;

import org.junit.Test;
import org.opensingular.server.connector.sei30.model.SerieEnum;
import org.opensingular.server.connector.sei30.ws.ObjectFactory;

public class JavaBeanComplianceTest {

    @Test
    public void enumTest() throws Exception {
        SerieEnum.DESPACHO.getNome();

    }


    @Test
    public void testDocumentobuilder() {
        org.opensingular.server.connector.sei30.builder.DocumentoBuilder documentobuilder = new org.opensingular.server.connector.sei30.builder.DocumentoBuilder();
    }

    @Test
    public void testProcedimentobuilder() {
        org.opensingular.server.connector.sei30.builder.ProcedimentoBuilder procedimentobuilder = new org.opensingular.server.connector.sei30.builder.ProcedimentoBuilder();
    }


    @Test
    public void testAndamento() {
        org.opensingular.server.connector.sei30.ws.Andamento andamento = new ObjectFactory().createAndamento();
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
        org.opensingular.server.connector.sei30.ws.AndamentoMarcador andamentomarcador = new ObjectFactory().createAndamentoMarcador();
        andamentomarcador.setDataHora(andamentomarcador.getDataHora());
        andamentomarcador.setIdAndamentoMarcador(andamentomarcador.getIdAndamentoMarcador());
        andamentomarcador.setTexto(andamentomarcador.getTexto());
        andamentomarcador.setMarcador(andamentomarcador.getMarcador());
        andamentomarcador.setUsuario(andamentomarcador.getUsuario());
    }

    @Test
    public void testArquivoextensao() {
        org.opensingular.server.connector.sei30.ws.ArquivoExtensao arquivoextensao = new ObjectFactory().createArquivoExtensao();
        arquivoextensao.setIdArquivoExtensao(arquivoextensao.getIdArquivoExtensao());
        arquivoextensao.setExtensao(arquivoextensao.getExtensao());
        arquivoextensao.setDescricao(arquivoextensao.getDescricao());
    }

    @Test
    public void testArrayofandamento() {
        org.opensingular.server.connector.sei30.ws.ArrayOfAndamento arrayofandamento = new ObjectFactory().createArrayOfAndamento();
    }

    @Test
    public void testArrayofandamentomarcador() {
        org.opensingular.server.connector.sei30.ws.ArrayOfAndamentoMarcador arrayofandamentomarcador = new ObjectFactory().createArrayOfAndamentoMarcador();
    }

    @Test
    public void testArrayofarquivoextensao() {
        org.opensingular.server.connector.sei30.ws.ArrayOfArquivoExtensao arrayofarquivoextensao = new ObjectFactory().createArrayOfArquivoExtensao();
    }

    @Test
    public void testArrayofassinatura() {
        org.opensingular.server.connector.sei30.ws.ArrayOfAssinatura arrayofassinatura = new ObjectFactory().createArrayOfAssinatura();
    }

    @Test
    public void testArrayofassunto() {
        org.opensingular.server.connector.sei30.ws.ArrayOfAssunto arrayofassunto = new ObjectFactory().createArrayOfAssunto();
    }

    @Test
    public void testArrayofatributoandamento() {
        org.opensingular.server.connector.sei30.ws.ArrayOfAtributoAndamento arrayofatributoandamento = new ObjectFactory().createArrayOfAtributoAndamento();
    }

    @Test
    public void testArrayofcampo() {
        org.opensingular.server.connector.sei30.ws.ArrayOfCampo arrayofcampo = new ObjectFactory().createArrayOfCampo();
    }

    @Test
    public void testArrayofcargo() {
        org.opensingular.server.connector.sei30.ws.ArrayOfCargo arrayofcargo = new ObjectFactory().createArrayOfCargo();
    }

    @Test
    public void testArrayofcidade() {
        org.opensingular.server.connector.sei30.ws.ArrayOfCidade arrayofcidade = new ObjectFactory().createArrayOfCidade();
    }

    @Test
    public void testArrayofcontato() {
        org.opensingular.server.connector.sei30.ws.ArrayOfContato arrayofcontato = new ObjectFactory().createArrayOfContato();
    }

    @Test
    public void testArrayofdefinicaomarcador() {
        org.opensingular.server.connector.sei30.ws.ArrayOfDefinicaoMarcador arrayofdefinicaomarcador = new ObjectFactory().createArrayOfDefinicaoMarcador();
    }

    @Test
    public void testArrayofdestinatario() {
        org.opensingular.server.connector.sei30.ws.ArrayOfDestinatario arrayofdestinatario = new ObjectFactory().createArrayOfDestinatario();
    }

    @Test
    public void testArrayofdocumento() {
        org.opensingular.server.connector.sei30.ws.ArrayOfDocumento arrayofdocumento = new ObjectFactory().createArrayOfDocumento();
    }

    @Test
    public void testArrayofdocumentoformatado() {
        org.opensingular.server.connector.sei30.ws.ArrayOfDocumentoFormatado arrayofdocumentoformatado = new ObjectFactory().createArrayOfDocumentoFormatado();
    }

    @Test
    public void testArrayofestado() {
        org.opensingular.server.connector.sei30.ws.ArrayOfEstado arrayofestado = new ObjectFactory().createArrayOfEstado();
    }

    @Test
    public void testArrayofhipoteselegal() {
        org.opensingular.server.connector.sei30.ws.ArrayOfHipoteseLegal arrayofhipoteselegal = new ObjectFactory().createArrayOfHipoteseLegal();
    }

    @Test
    public void testArrayofidunidade() {
        org.opensingular.server.connector.sei30.ws.ArrayOfIdUnidade arrayofidunidade = new ObjectFactory().createArrayOfIdUnidade();
    }

    @Test
    public void testArrayofinteressado() {
        org.opensingular.server.connector.sei30.ws.ArrayOfInteressado arrayofinteressado = new ObjectFactory().createArrayOfInteressado();
    }

    @Test
    public void testArrayofmarcador() {
        org.opensingular.server.connector.sei30.ws.ArrayOfMarcador arrayofmarcador = new ObjectFactory().createArrayOfMarcador();
    }

    @Test
    public void testArrayofobservacao() {
        org.opensingular.server.connector.sei30.ws.ArrayOfObservacao arrayofobservacao = new ObjectFactory().createArrayOfObservacao();
    }

    @Test
    public void testArrayofpais() {
        org.opensingular.server.connector.sei30.ws.ArrayOfPais arrayofpais = new ObjectFactory().createArrayOfPais();
    }

    @Test
    public void testArrayofprocedimentorelacionado() {
        org.opensingular.server.connector.sei30.ws.ArrayOfProcedimentoRelacionado arrayofprocedimentorelacionado = new ObjectFactory().createArrayOfProcedimentoRelacionado();
    }

    @Test
    public void testArrayofprocedimentoresumido() {
        org.opensingular.server.connector.sei30.ws.ArrayOfProcedimentoResumido arrayofprocedimentoresumido = new ObjectFactory().createArrayOfProcedimentoResumido();
    }

    @Test
    public void testArrayofprotocolobloco() {
        org.opensingular.server.connector.sei30.ws.ArrayOfProtocoloBloco arrayofprotocolobloco = new ObjectFactory().createArrayOfProtocoloBloco();
    }

    @Test
    public void testArrayofretornoinclusaodocumento() {
        org.opensingular.server.connector.sei30.ws.ArrayOfRetornoInclusaoDocumento arrayofretornoinclusaodocumento = new ObjectFactory().createArrayOfRetornoInclusaoDocumento();
    }

    @Test
    public void testArrayofserie() {
        org.opensingular.server.connector.sei30.ws.ArrayOfSerie arrayofserie = new ObjectFactory().createArrayOfSerie();
    }

    @Test
    public void testArrayofstring() {
        org.opensingular.server.connector.sei30.ws.ArrayOfString arrayofstring = new ObjectFactory().createArrayOfString();
    }

    @Test
    public void testArrayoftipoconferencia() {
        org.opensingular.server.connector.sei30.ws.ArrayOfTipoConferencia arrayoftipoconferencia = new ObjectFactory().createArrayOfTipoConferencia();
    }

    @Test
    public void testArrayoftipoprocedimento() {
        org.opensingular.server.connector.sei30.ws.ArrayOfTipoProcedimento arrayoftipoprocedimento = new ObjectFactory().createArrayOfTipoProcedimento();
    }

    @Test
    public void testArrayofunidade() {
        org.opensingular.server.connector.sei30.ws.ArrayOfUnidade arrayofunidade = new ObjectFactory().createArrayOfUnidade();
    }

    @Test
    public void testArrayofunidadeprocedimentoaberto() {
        org.opensingular.server.connector.sei30.ws.ArrayOfUnidadeProcedimentoAberto arrayofunidadeprocedimentoaberto = new ObjectFactory().createArrayOfUnidadeProcedimentoAberto();
    }

    @Test
    public void testArrayofusuario() {
        org.opensingular.server.connector.sei30.ws.ArrayOfUsuario arrayofusuario = new ObjectFactory().createArrayOfUsuario();
    }

    @Test
    public void testAssinatura() {
        org.opensingular.server.connector.sei30.ws.Assinatura assinatura = new ObjectFactory().createAssinatura();
        assinatura.setNome(assinatura.getNome());
        assinatura.setDataHora(assinatura.getDataHora());
        assinatura.setCargoFuncao(assinatura.getCargoFuncao());
    }

    @Test
    public void testAssunto() {
        org.opensingular.server.connector.sei30.ws.Assunto assunto = new ObjectFactory().createAssunto();
        assunto.setDescricao(assunto.getDescricao());
        assunto.setCodigoEstruturado(assunto.getCodigoEstruturado());
    }

    @Test
    public void testAtributoandamento() {
        org.opensingular.server.connector.sei30.ws.AtributoAndamento atributoandamento = new ObjectFactory().createAtributoAndamento();
        atributoandamento.setNome(atributoandamento.getNome());
        atributoandamento.setValor(atributoandamento.getValor());
        atributoandamento.setIdOrigem(atributoandamento.getIdOrigem());
    }

    @Test
    public void testCampo() {
        org.opensingular.server.connector.sei30.ws.Campo campo = new ObjectFactory().createCampo();
        campo.setNome(campo.getNome());
        campo.setValor(campo.getValor());
    }

    @Test
    public void testCargo() {
        org.opensingular.server.connector.sei30.ws.Cargo cargo = new ObjectFactory().createCargo();
        cargo.setIdCargo(cargo.getIdCargo());
        cargo.setExpressaoCargo(cargo.getExpressaoCargo());
        cargo.setExpressaoTratamento(cargo.getExpressaoTratamento());
        cargo.setExpressaoVocativo(cargo.getExpressaoVocativo());
    }

    @Test
    public void testCidade() {
        org.opensingular.server.connector.sei30.ws.Cidade cidade = new ObjectFactory().createCidade();
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
        org.opensingular.server.connector.sei30.ws.Contato contato = new ObjectFactory().createContato();
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
        contato.setEnderecoAssociado(contato.getEnderecoAssociado());
        contato.setComplementoAssociado(contato.getComplementoAssociado());
        contato.setBairroAssociado(contato.getBairroAssociado());
        contato.setIdCidadeAssociado(contato.getIdCidadeAssociado());
        contato.setNomeCidadeAssociado(contato.getNomeCidadeAssociado());
        contato.setIdEstadoAssociado(contato.getIdEstadoAssociado());
        contato.setSiglaEstadoAssociado(contato.getSiglaEstadoAssociado());
        contato.setIdPaisAssociado(contato.getIdPaisAssociado());
        contato.setNomePaisAssociado(contato.getNomePaisAssociado());
        contato.setCepAssociado(contato.getCepAssociado());
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
        org.opensingular.server.connector.sei30.ws.DefinicaoMarcador definicaomarcador = new ObjectFactory().createDefinicaoMarcador();
        definicaomarcador.setIdMarcador(definicaomarcador.getIdMarcador());
        definicaomarcador.setTexto(definicaomarcador.getTexto());
        definicaomarcador.setProtocoloProcedimento(definicaomarcador.getProtocoloProcedimento());
    }

    @Test
    public void testDestinatario() {
        org.opensingular.server.connector.sei30.ws.Destinatario destinatario = new ObjectFactory().createDestinatario();
        destinatario.setNome(destinatario.getNome());
        destinatario.setSigla(destinatario.getSigla());
    }

    @Test
    public void testDocumento() {
        org.opensingular.server.connector.sei30.ws.Documento documento = new ObjectFactory().createDocumento();
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
        org.opensingular.server.connector.sei30.ws.Estado estado = new ObjectFactory().createEstado();
        estado.setNome(estado.getNome());
        estado.setSigla(estado.getSigla());
        estado.setIdEstado(estado.getIdEstado());
        estado.setIdPais(estado.getIdPais());
        estado.setCodigoIbge(estado.getCodigoIbge());
    }

    @Test
    public void testHipoteselegal() {
        org.opensingular.server.connector.sei30.ws.HipoteseLegal hipoteselegal = new ObjectFactory().createHipoteseLegal();
        hipoteselegal.setNome(hipoteselegal.getNome());
        hipoteselegal.setNivelAcesso(hipoteselegal.getNivelAcesso());
        hipoteselegal.setIdHipoteseLegal(hipoteselegal.getIdHipoteseLegal());
        hipoteselegal.setBaseLegal(hipoteselegal.getBaseLegal());
    }

    @Test
    public void testInteressado() {
        org.opensingular.server.connector.sei30.ws.Interessado interessado = new ObjectFactory().createInteressado();
        interessado.setNome(interessado.getNome());
        interessado.setSigla(interessado.getSigla());
    }

    @Test
    public void testMarcador() {
        org.opensingular.server.connector.sei30.ws.Marcador marcador = new ObjectFactory().createMarcador();
        marcador.setNome(marcador.getNome());
        marcador.setIdMarcador(marcador.getIdMarcador());
        marcador.setIcone(marcador.getIcone());
        marcador.setSinAtivo(marcador.getSinAtivo());
    }

    @Test
    public void testObservacao() {
        org.opensingular.server.connector.sei30.ws.Observacao observacao = new ObjectFactory().createObservacao();
        observacao.setUnidade(observacao.getUnidade());
        observacao.setDescricao(observacao.getDescricao());
    }

    @Test
    public void testPais() {
        org.opensingular.server.connector.sei30.ws.Pais pais = new ObjectFactory().createPais();
        pais.setNome(pais.getNome());
        pais.setIdPais(pais.getIdPais());
    }

    @Test
    public void testProcedimento() {
        org.opensingular.server.connector.sei30.ws.Procedimento procedimento = new ObjectFactory().createProcedimento();
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
        org.opensingular.server.connector.sei30.ws.ProcedimentoResumido procedimentoresumido = new ObjectFactory().createProcedimentoResumido();
        procedimentoresumido.setIdProcedimento(procedimentoresumido.getIdProcedimento());
        procedimentoresumido.setProcedimentoFormatado(procedimentoresumido.getProcedimentoFormatado());
        procedimentoresumido.setTipoProcedimento(procedimentoresumido.getTipoProcedimento());
    }

    @Test
    public void testProtocolobloco() {
        org.opensingular.server.connector.sei30.ws.ProtocoloBloco protocolobloco = new ObjectFactory().createProtocoloBloco();
        protocolobloco.setAssinaturas(protocolobloco.getAssinaturas());
        protocolobloco.setProtocoloFormatado(protocolobloco.getProtocoloFormatado());
        protocolobloco.setIdentificacao(protocolobloco.getIdentificacao());
    }

    @Test
    public void testPublicacao() {
        org.opensingular.server.connector.sei30.ws.Publicacao publicacao = new ObjectFactory().createPublicacao();
        publicacao.setEstado(publicacao.getEstado());
        publicacao.setNumero(publicacao.getNumero());
        publicacao.setNomeVeiculo(publicacao.getNomeVeiculo());
        publicacao.setDataDisponibilizacao(publicacao.getDataDisponibilizacao());
        publicacao.setDataPublicacao(publicacao.getDataPublicacao());
        publicacao.setImprensaNacional(publicacao.getImprensaNacional());
    }

    @Test
    public void testPublicacaoimprensanacional() {
        org.opensingular.server.connector.sei30.ws.PublicacaoImprensaNacional publicacaoimprensanacional = new ObjectFactory().createPublicacaoImprensaNacional();
        publicacaoimprensanacional.setSecao(publicacaoimprensanacional.getSecao());
        publicacaoimprensanacional.setSiglaVeiculo(publicacaoimprensanacional.getSiglaVeiculo());
        publicacaoimprensanacional.setDescricaoVeiculo(publicacaoimprensanacional.getDescricaoVeiculo());
        publicacaoimprensanacional.setPagina(publicacaoimprensanacional.getPagina());
        publicacaoimprensanacional.setData(publicacaoimprensanacional.getData());
    }

    @Test
    public void testRemetente() {
        org.opensingular.server.connector.sei30.ws.Remetente remetente = new ObjectFactory().createRemetente();
        remetente.setNome(remetente.getNome());
        remetente.setSigla(remetente.getSigla());
    }

    @Test
    public void testRetornoconsultabloco() {
        org.opensingular.server.connector.sei30.ws.RetornoConsultaBloco retornoconsultabloco = new ObjectFactory().createRetornoConsultaBloco();
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
        org.opensingular.server.connector.sei30.ws.RetornoConsultaDocumento retornoconsultadocumento = new ObjectFactory().createRetornoConsultaDocumento();
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
        org.opensingular.server.connector.sei30.ws.RetornoConsultaProcedimento retornoconsultaprocedimento = new ObjectFactory().createRetornoConsultaProcedimento();
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
        org.opensingular.server.connector.sei30.ws.RetornoGeracaoProcedimento retornogeracaoprocedimento = new ObjectFactory().createRetornoGeracaoProcedimento();
        retornogeracaoprocedimento.setIdProcedimento(retornogeracaoprocedimento.getIdProcedimento());
        retornogeracaoprocedimento.setProcedimentoFormatado(retornogeracaoprocedimento.getProcedimentoFormatado());
        retornogeracaoprocedimento.setLinkAcesso(retornogeracaoprocedimento.getLinkAcesso());
        retornogeracaoprocedimento.setRetornoInclusaoDocumentos(retornogeracaoprocedimento.getRetornoInclusaoDocumentos());
    }

    @Test
    public void testRetornoinclusaodocumento() {
        org.opensingular.server.connector.sei30.ws.RetornoInclusaoDocumento retornoinclusaodocumento = new ObjectFactory().createRetornoInclusaoDocumento();
        retornoinclusaodocumento.setLinkAcesso(retornoinclusaodocumento.getLinkAcesso());
        retornoinclusaodocumento.setIdDocumento(retornoinclusaodocumento.getIdDocumento());
        retornoinclusaodocumento.setDocumentoFormatado(retornoinclusaodocumento.getDocumentoFormatado());
    }


    @Test
    public void testSerie() {
        org.opensingular.server.connector.sei30.ws.Serie serie = new ObjectFactory().createSerie();
        serie.setNome(serie.getNome());
        serie.setIdSerie(serie.getIdSerie());
        serie.setAplicabilidade(serie.getAplicabilidade());
    }

    @Test
    public void testTipoconferencia() {
        org.opensingular.server.connector.sei30.ws.TipoConferencia tipoconferencia = new ObjectFactory().createTipoConferencia();
        tipoconferencia.setDescricao(tipoconferencia.getDescricao());
        tipoconferencia.setIdTipoConferencia(tipoconferencia.getIdTipoConferencia());
    }

    @Test
    public void testUnidade() {
        org.opensingular.server.connector.sei30.ws.Unidade unidade = new ObjectFactory().createUnidade();
        unidade.setSigla(unidade.getSigla());
        unidade.setIdUnidade(unidade.getIdUnidade());
        unidade.setDescricao(unidade.getDescricao());
    }

    @Test
    public void testUnidadeprocedimentoaberto() {
        org.opensingular.server.connector.sei30.ws.UnidadeProcedimentoAberto unidadeprocedimentoaberto = new ObjectFactory().createUnidadeProcedimentoAberto();
        unidadeprocedimentoaberto.setUnidade(unidadeprocedimentoaberto.getUnidade());
        unidadeprocedimentoaberto.setUsuarioAtribuicao(unidadeprocedimentoaberto.getUsuarioAtribuicao());
    }

    @Test
    public void testUsuario() {
        org.opensingular.server.connector.sei30.ws.Usuario usuario = new ObjectFactory().createUsuario();
        usuario.setNome(usuario.getNome());
        usuario.setSigla(usuario.getSigla());
        usuario.setIdUsuario(usuario.getIdUsuario());
    }


}
