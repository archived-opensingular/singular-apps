/*
 * Copyright (C) 2016 Singular Studios (a.k.a Atom Tecnologia) - www.opensingular.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.opensingular.requirement.connector.sei30.model;

/**
 * Classe TipoBlocoEnum
 */
public enum TipoBlocoEnum {

    /** * Campo ASSINATURA. */
    ASSINATURA("A", "Assinatura"),

    /** * Campo REUNIAO. */
    REUNIAO("R", "Reunião"),

    /** * Campo INTERNO. */
    INTERNO("I", "Interno");

    /** Campo Sigla. */
    private String sigla;
    /** Campo Nome. */
    private String nome;

    /**
     * Instancia um novo Tipo bloco enum.
     *
     * @param sigla o(a) sigla
     * @param nome o(a) nome
     */
    private TipoBlocoEnum(String sigla, String nome) {
        this.sigla = sigla;
        this.nome = nome;
    }

    /**
     * Retorna sigla.
     *
     * @return o(a) sigla
     */
    public String getSigla() {
        return sigla;
    }

    /**
     * Recupera o valor de nome.
     *
     * @return o valor de nome
     */
    public String getNome() {
        return nome;
    }

}
