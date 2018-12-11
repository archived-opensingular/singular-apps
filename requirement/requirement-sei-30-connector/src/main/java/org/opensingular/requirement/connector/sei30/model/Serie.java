package org.opensingular.requirement.connector.sei30.model;

import java.io.Serializable;

public class Serie implements Serializable {

    public static final String DOC_INTERNO_EXTERNO = "T";
    public static final String DOC_INTERNO = "I";
    public static final String DOC_EXTERNO = "E";
    public static final String FORMULARIO = "F";

    private String id;
    private String nome;
    private String aplicabilidade;

    public Serie(String id, String nome, String aplicabilidade) {
        this.id = id;
        this.nome = nome;
        this.aplicabilidade = aplicabilidade;
    }

    public Serie(String id, String nome) {
        this(id, nome, null);
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
