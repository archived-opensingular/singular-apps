/*
 *
 *  * Copyright (C) 2016 Singular Studios (a.k.a Atom Tecnologia) - www.opensingular.com
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  * http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package org.opensingular.server.connector.sei30.model;

import java.io.Serializable;

public class AssuntoSei implements Serializable {

    private String sigla;
    private String nome;

    public AssuntoSei(String sigla, String nome) {
        this.sigla = sigla;
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public String getSigla() {
        return sigla;
    }

    public org.opensingular.server.connector.sei30.ws.Assunto toAssunto() {
        org.opensingular.server.connector.sei30.ws.Assunto assunto = new org.opensingular.server.connector.sei30.ws.Assunto();
        assunto.setCodigoEstruturado(sigla);
        assunto.setDescricao(nome);
        return assunto;
    }

}
