package org.opensingular.server.connector.sei30.model;

import java.io.Serializable;

public class AssuntoSei implements Serializable {

    private String sigla;
    private String nome;

    public AssuntoSei(String sigla, String nome) {
        this.sigla = sigla;
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public String getSigla() {
        return sigla;
    }

    public org.opensingular.server.connector.sei30.ws.Assunto toAssunto() {
        org.opensingular.server.connector.sei30.ws.Assunto assunto = new org.opensingular.server.connector.sei30.ws.Assunto();
        assunto.setCodigoEstruturado(sigla);
        assunto.setDescricao(nome);
        return assunto;
    }

}
