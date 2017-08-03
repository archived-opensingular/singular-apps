package org.opensingular.server.connector.sei30.model;

/**
 * A Enum TipoProcedimento.
 */
public enum UnidadeEnum {

    /** O campo orle. */
    ORLE("110001113", "Gerência de Outorga e Licenciamento de Estações"),

    /** O campo orcn. */
    ORCN("110001100", "Gerência de Certificação e Numeração"),

    /** O campo sor. */
    SOR("110001099", "Superintendência de Outorga e Recursos à Prestação");

    private final String id;
    private final String nome;

    private UnidadeEnum(String id, String nome) {
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
     * Recupera o valor de id.
     * 
     * @return o valor de id
     */
    public String getId() {
        return id;
    }

}
