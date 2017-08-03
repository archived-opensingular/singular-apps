package org.opensingular.server.connector.sei30.model;

/**
 * Classe TipoDocumento.
 */
public enum TipoDocumento {

    /** O campo gerado. */
    GERADO("G"),

    /** O campo recebido. */
    RECEBIDO("R");

    private String codigo;

    private TipoDocumento(String codigo) {
        this.codigo = codigo;
    }

    /**
     * Recupera o valor de codigo.
     * 
     * @return o valor de codigo
     */
    public String getCodigo() {
        return codigo;
    }
}
