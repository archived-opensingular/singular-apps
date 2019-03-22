package org.opensingular.requirement.connector.sei31.model;

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

    public Serie(String id, String nome) {
        this(id, nome, null);
    }

    public static SerieType[] getAplicabilidadeWithModelo() {
        return new SerieType[] {SerieType.DOC_INTERNO, SerieType.DOC_INTERNO};
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

    public enum SerieType {
        DOC_INTERNO_EXTERNO("T"),
        DOC_INTERNO("I"),
        DOC_EXTERNO("E"),
        FORMULARIO("F");


        private String description;

        SerieType(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }

        public static SerieType valueOfEnumByDescription(String description) {
            for (SerieType type : SerieType.values()) {
                if (description.trim().equals(type.getDescription())) {
                    return type;
                }
            }
            return null;
        }

    }
}
