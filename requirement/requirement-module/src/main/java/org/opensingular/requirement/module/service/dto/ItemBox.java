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

package org.opensingular.requirement.module.service.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.opensingular.lib.commons.ui.Icon;
import org.opensingular.requirement.module.SingularRequirement;
import org.opensingular.requirement.module.jackson.IconJsonDeserializer;
import org.opensingular.requirement.module.jackson.IconJsonSerializer;
import org.opensingular.requirement.module.workspace.BoxDefinition;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class ItemBox implements Serializable {
    private String name;
    private String description;
    private String helpText;
    private Icon icon;
    private List<DatatableField> fieldsDatatable;
    private boolean showQuickFilter = true;
    private Class<? extends BoxDefinition> boxDefinitionClass;
    private Set<Class<? extends SingularRequirement>> requirements = new LinkedHashSet<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<DatatableField> getFieldsDatatable() {
        return fieldsDatatable;
    }

    public void setFieldsDatatable(List<DatatableField> fieldsDatatable) {
        this.fieldsDatatable = fieldsDatatable;
    }

    @JsonSerialize(using = IconJsonSerializer.class)
    public Icon getIcone() {
        return icon;
    }

    @JsonDeserialize(using = IconJsonDeserializer.class)
    public void setIcone(Icon icon) {
        this.icon = icon;
    }

    public String getHelpText() {
        return helpText;
    }

    public void setHelpText(String helpText) {
        this.helpText = helpText;
    }

    public Class<? extends BoxDefinition> getBoxDefinitionClass() {
        return boxDefinitionClass;
    }

    public void setBoxDefinitionClass(Class<? extends BoxDefinition> boxDefinitionClass) {
        this.boxDefinitionClass = boxDefinitionClass;
    }

    public boolean isShowQuickFilter() {
        return showQuickFilter;
    }

    /**
     * Indicates if the quick filter field should be displayed
     *
     * @param showQuickFilter the indicator
     */
    public void setShowQuickFilter(boolean showQuickFilter) {
        this.showQuickFilter = showQuickFilter;
    }

    public Set<Class<? extends SingularRequirement>> getRequirements() {
        return requirements;
    }

    public ItemBox newFor(Class<? extends SingularRequirement> requirement) {
        getRequirements().add(requirement);
        return this;
    }
}