package org.opensingular.server.connector.sei30.model;

public class UnidadeSei {

    private final String id;
    private final String nome;

    public UnidadeSei(String id, String nome) {
        this.id = id;
        this.nome = nome;

    }

    public String getNome() {
        return nome;
    }

    public String getId() {
        return id;
    }

}
