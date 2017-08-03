package org.opensingular.server.connector.sei30.model;

/**
 * A Enum TipoProcedimento.
 */
public enum ProcedimentoEnum {

    /** O campo ute. */
    UTE("100000804", "Uso Temporário do Espectro"),

    /** O campo sch. */
    SCH("100000755", "Sistema de Gerência de Certificação e Homologação");

    private final String id;
    private final String nome;

    private ProcedimentoEnum(String id, String nome) {
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
