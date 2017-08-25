package org.opensingular.server.connector.sei30.model;

import java.io.Serializable;

public class TipoProcedimento implements Serializable {

    private final String id;
    private final String nome;

    public TipoProcedimento(String id, String nome) {
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
