package org.opensingular.server.connector.sei30.model;

import org.opensingular.server.connector.sei30.ws.Assunto;

/**
 * A Enum AssuntoEnum.
 */
public enum AssuntoEnum {

    /** O campo ute. */
    UTE("280.3", "USO TEMPORÁRIO DE ESPECTRO"),

    /** O campo sch. */
    SCH("240.1", "CERTIFICAÇÃO E HOMOLOGAÇÃO PRODUTOS DE COMUNICAÇÃO E SISTEMAS DE TELECOMUNICAÇÕES");

    private String sigla;
    private String nome;

    private AssuntoEnum(String sigla, String nome) {
        this.sigla = sigla;
        this.nome = nome;
    }

    /**
     * Recupera o valor de nome.
     * 
     * @return o valor de nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * To assunto.
     * 
     * @return o valor de assunto
     */
    public Assunto toAssunto() {
        Assunto assunto = new Assunto();
        assunto.setCodigoEstruturado(sigla);
        assunto.setDescricao(nome);
        return assunto;
    }

}
