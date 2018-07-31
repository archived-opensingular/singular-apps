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

package org.opensingular.requirement.module.persistence.filter;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BoxFilter implements Serializable {

    private String filter;
    private boolean rascunho;
    private String idPessoa;
    private String idUsuarioLogado;
    private int first;
    private int count;
    private String sortProperty;
    private boolean ascending;
    private Boolean endedTasks;
    private List<String> tasks;
    private List<String> processesAbbreviation;
    private List<String> typesNames;

    public BoxFilter withFilter(String filter) {
        this.filter = filter;
        return this;
    }

    public String getIdUsuarioLogado() {
        return idUsuarioLogado;
    }

    public BoxFilter withIdUsuarioLogado(String idUsuarioLogado) {
        this.idUsuarioLogado = idUsuarioLogado;
        return this;
    }

    public String getIdPessoa() {
        return idPessoa;
    }

    public BoxFilter withIdPessoa(String idPessoa) {
        this.idPessoa = idPessoa;
        return this;
    }

    public boolean isRascunho() {
        return rascunho;
    }

    public BoxFilter withRascunho(boolean rascunho) {
        this.rascunho = rascunho;
        return this;
    }

    public int getFirst() {
        return first;
    }

    public BoxFilter withFirst(int first) {
        this.first = first;
        return this;
    }

    public int getCount() {
        return count;
    }

    public BoxFilter withCount(int count) {
        this.count = count;
        return this;
    }

    public String getSortProperty() {
        return sortProperty;
    }

    public BoxFilter withSortProperty(String sortProperty) {
        this.sortProperty = sortProperty;
        return this;
    }

    public boolean isAscending() {
        return ascending;
    }

    public BoxFilter withAscending(boolean ascending) {
        this.ascending = ascending;
        return this;
    }

    public BoxFilter sortAscending() {
        this.ascending = true;
        return this;
    }

    public BoxFilter sortDescending() {
        this.ascending = false;
        return this;
    }

    public boolean hasFilter() {
        return filter != null
                && !filter.isEmpty();
    }

    public BoxFilter forTasks(String... tasks) {
        this.tasks = Arrays.asList(tasks);
        return this;
    }

    public List<String> getTasks() {
        return tasks;
    }

    public List<String> getProcessesAbbreviation() {
        return processesAbbreviation;
    }

    public BoxFilter withProcessesAbbreviation(List<String> processesAbbreviation) {
        this.processesAbbreviation = processesAbbreviation;
        return this;
    }

    public List<String> getTypesNames() {
        return typesNames;
    }

    public BoxFilter withTypesNames(List<String> typesNames) {
        this.typesNames = typesNames;
        return this;
    }

    public Boolean getEndedTasks() {
        return endedTasks;
    }

    public BoxFilter withEndedTasks(Boolean endedTasks) {
        this.endedTasks = endedTasks;
        return this;
    }

    public List<FilterToken> listFilterTokens() {
        if (filter != null) {
            return new FilterTokenFactory(filter).make();
        }
        return Collections.emptyList();
    }

    public String getFilter() {
        return filter;
    }

}