package org.opensingular.requirement.connector.sei30.model;

import java.io.Serializable;

public class Serie implements Serializable {

    private String id;
    private String nome;
    private String aplicabilidade;

    public Serie(String id, String nome, String aplicabilidade) {
        this.id = id;
        this.nome = nome;
        this.aplicabilidade = aplicabilidade;
    }

    public String getNome() {
        return nome;
    }

    public String getId() {
        return id;
    }

    public String getAplicabilidade() {
        return aplicabilidade;
    }
}
