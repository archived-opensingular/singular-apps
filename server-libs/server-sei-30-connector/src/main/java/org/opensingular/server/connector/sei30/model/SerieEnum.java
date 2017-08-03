package org.opensingular.server.connector.sei30.model;

/**
 * A Enum Serie.
 */
public enum SerieEnum {

    /** O campo ato. */
    ATO("3", "Ato"),

    /** O campo despacho ordinatorio. */
    DESPACHO_ORDINATORIO("5", "Despacho Ordinatório"),

    /** O campo solicitacao. */
    SOLICITACAO("178", "Solicitação"),

    /** O campo cnpj. */
    CNPJ("210", "CNPJ"),

    /** O campo cnpj. */
    EMAIL("276", "E-Mail"),

    /** O campo comprovante. */
    COMPROVANTE("35", "Comprovante"),

    /** O campo extrato de ato. */
    EXTRATO_DE_ATO("190", "Extrato de Ato"),
    /** O campo minuta. */
    MINUTA("204", "Minuta");

    private final String id;
    private final String nome;

    private SerieEnum(String id, String nome) {
        this.id = id;
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
     * Recupera o valor de codigo.
     * 
     * @return o valor de codigo
     */
    public String getId() {
        return id;
    }

}
