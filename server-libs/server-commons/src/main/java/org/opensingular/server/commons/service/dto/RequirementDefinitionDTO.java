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

package org.opensingular.server.commons.service.dto;

import java.io.Serializable;
import java.util.List;

public class RequirementDefinitionDTO implements Serializable {

    private String       name;
    private String       formName;
    private String       abbreviation;
    private List<String> allowedHistoryTasks;

    public RequirementDefinitionDTO() {
    }

    public RequirementDefinitionDTO(String abbreviation, String name, String formName, List<String> allowedHistoryTasks) {
        this.abbreviation = abbreviation;
        this.name = name;
        this.formName = formName;
        this.allowedHistoryTasks = allowedHistoryTasks;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFormName() {
        return formName;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public List<String> getAllowedHistoryTasks() {
        return allowedHistoryTasks;
    }
}