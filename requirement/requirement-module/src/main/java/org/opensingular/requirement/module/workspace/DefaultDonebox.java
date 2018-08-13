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

package org.opensingular.requirement.module.workspace;

import org.opensingular.lib.wicket.util.resource.DefaultIcons;
import org.opensingular.requirement.module.ActionProviderBuilder;
import org.opensingular.requirement.module.persistence.filter.BoxFilter;
import org.opensingular.requirement.module.service.dto.DatatableField;
import org.opensingular.requirement.module.service.dto.ItemBox;

import java.util.ArrayList;
import java.util.List;

public class DefaultDonebox extends AbstractRequirementBoxDefinition {
    @Override
    protected void configure(ItemBox itemBox) {
        itemBox.name("Concluídos")
                .description("Requerimentos concluídos")
                .icon(DefaultIcons.DOCS)
                .evalPermission(true);
    }

    @Override
    protected void addActions(ActionProviderBuilder builder) {
        builder
                .addViewAction()
                .addHistoryAction();
    }

    @Override
    public List<DatatableField> getDatatableFields() {
        List<DatatableField> fields = new ArrayList<>();
        fields.add(DatatableField.of("Número", "codRequirement"));
        fields.add(DatatableField.of("Dt. de Entrada", "processBeginDate"));
        fields.add(DatatableField.of("Solicitante", "solicitante"));
        fields.add(DatatableField.of("Descrição", "description"));
        fields.add(DatatableField.of("Dt. Situação", "situationBeginDate"));
        fields.add(DatatableField.of("Situação", "taskName"));
        return fields;
    }

    @Override
    public BoxFilter createBoxFilter() {
        return super.createBoxFilter()
                .endedTasks(Boolean.TRUE)
                .checkApplicant(false);
    }
}