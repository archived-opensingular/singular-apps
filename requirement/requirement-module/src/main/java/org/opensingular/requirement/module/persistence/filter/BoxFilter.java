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

package org.opensingular.requirement.module.persistence.filter;

import org.opensingular.flow.core.ITaskDefinition;
import org.opensingular.form.SInstance;

import java.util.Collections;
import java.util.List;

public class BoxFilter {
    private String                filter;
    private boolean               showDraft;
    private String                idPessoa;
    private String                idUsuarioLogado;
    private int                   first;
    private int                   count;
    private String                sortProperty;
    private boolean               ascending;
    private Boolean               endedTasks;
    private List<ITaskDefinition> tasks;
    private List<String>          processesAbbreviation;
    private List<String>          typesNames;
    private SInstance             advancedFilterInstance;

    private boolean checkApplicant = false;
    private boolean boxCountQuery  = false;


    public List<FilterToken> listFilterTokens() {
        if (filter != null) {
            return new FilterTokenFactory(filter).make();
        }
        return Collections.emptyList();
    }

    public String getFilter() {
        return filter;
    }

    public BoxFilter filter(String filter) {
        this.filter = filter;
        return this;
    }

    public boolean isShowDraft() {
        return showDraft;
    }

    public BoxFilter showDraft(boolean showDraft) {
        this.showDraft = showDraft;
        return this;
    }

    public String getIdPessoa() {
        return idPessoa;
    }

    public BoxFilter idPessoa(String idPessoa) {
        this.idPessoa = idPessoa;
        return this;
    }

    public String getIdUsuarioLogado() {
        return idUsuarioLogado;
    }

    public BoxFilter idUsuarioLogado(String idUsuarioLogado) {
        this.idUsuarioLogado = idUsuarioLogado;
        return this;
    }

    public int getFirst() {
        return first;
    }

    public BoxFilter first(int first) {
        this.first = first;
        return this;
    }

    public int getCount() {
        return count;
    }

    public BoxFilter count(int count) {
        this.count = count;
        return this;
    }

    public BoxFilter setBoxCountQuery(boolean boxCountQuery) {
        this.boxCountQuery = boxCountQuery;
        return this;
    }

    public boolean isBoxCountQuery() {
        return boxCountQuery;
    }

    public String getSortProperty() {
        return sortProperty;
    }

    public BoxFilter sortProperty(String sortProperty) {
        this.sortProperty = sortProperty;
        return this;
    }

    public boolean isAscending() {
        return ascending;
    }

    public BoxFilter ascending(boolean ascending) {
        this.ascending = ascending;
        return this;
    }

    public Boolean getEndedTasks() {
        return endedTasks;
    }

    public BoxFilter endedTasks(Boolean endedTasks) {
        this.endedTasks = endedTasks;
        return this;
    }

    public List<ITaskDefinition> getTasks() {
        return tasks;
    }

    public BoxFilter tasks(List<ITaskDefinition> tasks) {
        this.tasks = tasks;
        return this;
    }

    public List<String> getProcessesAbbreviation() {
        return processesAbbreviation;
    }

    public BoxFilter processesAbbreviation(List<String> processesAbbreviation) {
        this.processesAbbreviation = processesAbbreviation;
        return this;
    }

    public List<String> getTypesAbbreviations() {
        return typesNames;
    }

    public BoxFilter typesAbbreviations(List<String> typesNames) {
        this.typesNames = typesNames;
        return this;
    }

    public boolean isCheckApplicant() {
        return checkApplicant;
    }

    public BoxFilter checkApplicant(boolean checkApplicant) {
        this.checkApplicant = checkApplicant;
        return this;
    }

    public SInstance getAdvancedFilterInstance() {
        return advancedFilterInstance;
    }

    public BoxFilter advancedFilterInstance(SInstance advancedFilterInstance) {
        this.advancedFilterInstance = advancedFilterInstance;
        return this;
    }
}