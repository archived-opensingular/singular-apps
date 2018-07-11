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

package org.opensingular.requirement.module;

import org.opensingular.requirement.module.box.BoxItemDataImpl;
import org.opensingular.requirement.module.box.BoxItemDataList;
import org.opensingular.requirement.module.persistence.filter.QuickFilter;
import org.opensingular.requirement.module.service.dto.RequirementData;
import org.opensingular.requirement.module.workspace.BoxDefinition;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BoxController implements BoxInfo {

    private String                      id;
    private Set<SingularRequirementRef> requirementRefs = new LinkedHashSet<>();
    private BoxDefinition boxDefinition;

    public BoxController(BoxDefinition boxDefinition, List<SingularRequirementRef> requirementRefs) {
        for (SingularRequirementRef ref : requirementRefs) {
            this.requirementRefs.add(ref);
        }
        this.boxDefinition = boxDefinition;
    }

    public BoxController(BoxDefinition itemBox) {
        this.boxDefinition = itemBox;
    }

    @Override
    public String getBoxId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public Set<SingularRequirementRef> getRequirementRefs() {
        return new LinkedHashSet<>(requirementRefs);
    }

    void addRequirementRefs(SingularRequirementRef requirementRefs) {
        this.requirementRefs.add(requirementRefs);
    }

    BoxDefinition getBoxDefinition() {
        return boxDefinition;
    }

    public Long countItens(QuickFilter filter) {
        return boxDefinition.getDataProvider().count(filter, this);
    }

    public BoxItemDataList searchItens(QuickFilter filter) {
        BoxItemDataProvider             provider       = boxDefinition.getDataProvider();
        List<Map<String, Serializable>> itens          = provider.search(filter, this);
        BoxItemDataList                 result         = new BoxItemDataList();
        ActionProvider                  actionProvider = addBuiltInDecorators(provider.getActionProvider());

        for (Map<String, Serializable> item : itens) {
            BoxItemDataImpl line = new BoxItemDataImpl();
            line.setRawMap(item);
            line.setBoxItemActions(actionProvider.getLineActions(this, line, filter));
            result.getBoxItemDataList().add(line);
        }
        return result;
    }

    protected ActionProvider addBuiltInDecorators(ActionProvider actionProvider) {
        return
                new AuthorizationAwareActionProviderDecorator(actionProvider);
    }


    List<RequirementData> getRequirementsData() {
        List<RequirementData> result = new ArrayList<>();
        for (SingularRequirementRef ref : requirementRefs) {
            result.add(new RequirementData(ref.getId(), ref.getRequirementDescription()));
        }
        return result;
    }
}
