package org.opensingular.server.connector.sei30.model;

public enum SerieEnum {

    ATO("3", "Ato"),

    DESPACHO_ORDINATORIO("5", "Despacho Ordinatório"),

    SOLICITACAO("178", "Solicitação"),

    CNPJ("210", "CNPJ"),

    EMAIL("276", "E-Mail"),

    COMPROVANTE("35", "Comprovante"),

    EXTRATO_DE_ATO("190", "Extrato de Ato"),

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
