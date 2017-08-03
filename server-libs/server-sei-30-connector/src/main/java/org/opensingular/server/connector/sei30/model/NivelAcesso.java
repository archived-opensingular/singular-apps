package org.opensingular.server.connector.sei30.model;

/**
 * Classe NivelAcesso.
 */
public enum NivelAcesso {

    /** O campo publico. */
    PUBLICO ("0"),

    /** O campo restrito. */
    RESTRITO("1"),

    /** O campo sigiloso. */
    SIGILOSO("2");

    private String codigo;

    private NivelAcesso(String codigo) {
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
