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

package org.opensingular.requirement.module.workspace;

import org.opensingular.lib.wicket.util.resource.DefaultIcons;
import org.opensingular.requirement.module.ActionProvider;
import org.opensingular.requirement.module.ActionProviderBuilder;
import org.opensingular.requirement.module.config.IServerContext;
import org.opensingular.requirement.module.service.dto.DatatableField;
import org.opensingular.requirement.module.service.dto.ItemBox;

import java.util.ArrayList;
import java.util.List;

public class DefaultDonebox extends AbstractRequirementBoxDefinition {
    @Override
    public ItemBox build(IServerContext context) {
        final ItemBox concluidas = new ItemBox();
        concluidas.setName("Concluídos");
        concluidas.setDescription("Requerimentos concluídos");
        concluidas.setIcone(DefaultIcons.DOCS);
        concluidas.setEndedTasks(Boolean.TRUE);
        return concluidas;
    }

    @Override
    protected Boolean shouldEvalPermissions() {
        return Boolean.TRUE;
    }

    @Override
    protected ActionProvider actionProvider() {
        return new ActionProviderBuilder()
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
}
