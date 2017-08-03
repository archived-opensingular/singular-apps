package org.opensingular.server.connector.sei30.model;

/**
 * Classe TipoBlocoEnum
 */
public enum TipoBlocoEnum {

    /** * Campo ASSINATURA. */
    ASSINATURA("A", "Assinatura"),

    /** * Campo REUNIAO. */
    REUNIAO("R", "Reunião"),

    /** * Campo INTERNO. */
    INTERNO("I", "Interno");

    /** Campo Sigla. */
    private String sigla;
    /** Campo Nome. */
    private String nome;

    /**
     * Instancia um novo Tipo bloco enum.
     *
     * @param sigla o(a) sigla
     * @param nome o(a) nome
     */
    private TipoBlocoEnum(String sigla, String nome) {
        this.sigla = sigla;
        this.nome = nome;
    }

    /**
     * Retorna sigla.
     *
     * @return o(a) sigla
     */
    public String getSigla() {
        return sigla;
    }

    /**
     * Recupera o valor de nome.
     *
     * @return o valor de nome
     */
    public String getNome() {
        return nome;
    }

}
