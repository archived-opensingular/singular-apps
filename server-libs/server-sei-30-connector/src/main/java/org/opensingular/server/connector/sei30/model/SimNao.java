package org.opensingular.server.connector.sei30.model;

/**
 * Classe SimNao.
 */
public enum SimNao {

    /** O campo sim. */
    SIM("S"),

    /** O campo nao. */
    NAO("N");

    private String codigo;

    private SimNao(String codigo) {
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
