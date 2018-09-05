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

import org.opensingular.lib.commons.ui.Icon;
import org.opensingular.requirement.module.SingularRequirement;
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
    private boolean displayCounters = true;
    private boolean evalPermission = false;

    public ItemBox newFor(Class<? extends SingularRequirement> requirement) {
        getRequirements().add(requirement);
        return this;
    }

    public String getName() {
        return name;
    }

    public ItemBox name(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ItemBox description(String description) {
        this.description = description;
        return this;
    }

    public String getHelpText() {
        return helpText;
    }

    public ItemBox helpText(String helpText) {
        this.helpText = helpText;
        return this;
    }

    public Icon getIcon() {
        return icon;
    }

    public ItemBox icon(Icon icon) {
        this.icon = icon;
        return this;
    }

    public List<DatatableField> getFieldsDatatable() {
        return fieldsDatatable;
    }

    public ItemBox fieldsDatatable(List<DatatableField> fieldsDatatable) {
        this.fieldsDatatable = fieldsDatatable;
        return this;
    }

    public boolean isShowQuickFilter() {
        return showQuickFilter;
    }

    public ItemBox showQuickFilter(boolean showQuickFilter) {
        this.showQuickFilter = showQuickFilter;
        return this;
    }

    public Class<? extends BoxDefinition> getBoxDefinitionClass() {
        return boxDefinitionClass;
    }

    public ItemBox boxDefinitionClass(Class<? extends BoxDefinition> boxDefinitionClass) {
        this.boxDefinitionClass = boxDefinitionClass;
        return this;
    }

    public Set<Class<? extends SingularRequirement>> getRequirements() {
        return requirements;
    }

    public ItemBox requirements(Set<Class<? extends SingularRequirement>> requirements) {
        this.requirements = requirements;
        return this;
    }

    public boolean isDisplayCounters() {
        return displayCounters;
    }

    public ItemBox displayCounters(boolean displayCounters) {
        this.displayCounters = displayCounters;
        return this;
    }

    public boolean isEvalPermission() {
        return evalPermission;
    }

    public ItemBox evalPermission(boolean evalPermission) {
        this.evalPermission = evalPermission;
        return this;
    }
}