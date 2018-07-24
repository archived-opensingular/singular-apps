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

package org.opensingular.requirement.module.wicket.view.form;


import org.apache.commons.lang3.math.NumberUtils;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.opensingular.form.util.diff.DocumentDiff;
import org.opensingular.lib.wicket.util.bootstrap.layout.BSGrid;
import org.opensingular.lib.wicket.util.bootstrap.layout.BSLabel;
import org.opensingular.lib.wicket.util.bootstrap.layout.BSRow;
import org.opensingular.lib.wicket.util.datatable.BSDataTable;
import org.opensingular.lib.wicket.util.output.BOutputPanel;
import org.opensingular.requirement.module.service.FormRequirementService;
import org.opensingular.requirement.module.service.SingularDiffService;
import org.opensingular.requirement.module.wicket.view.template.ServerTemplate;
import org.opensingular.requirement.module.wicket.view.util.ActionContext;
import org.opensingular.requirement.module.wicket.view.util.ModuleButtonFactory;

import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import static org.opensingular.lib.wicket.util.util.Shortcuts.*;

public class DiffFormPage extends ServerTemplate {

    public static final String CURRENT_FORM_VERSION_ID  = "cfv";
    public static final String PREVIOUS_FORM_VERSION_ID = "pfv";

    public static final String CURRENT_REQUIREMENT_ID  = "cr";
    public static final String PREVIOUS_REQUIREMENT_ID = "pr";

    @Inject
    protected FormRequirementService formRequirementService;

    @Inject
    protected SingularDiffService singularDiffService;


    private   ActionContext                     config;
    protected BSDataTable<DocumentDiff, String> table;
    protected SingularDiffService.DiffSummary   diffSummary;
    protected BSGrid contentGrid = new BSGrid("content");

    public DiffFormPage(ActionContext config) {
        this.config = config;
    }

    @Override
    protected void onConfigure() {
        super.onConfigure();
        Optional<Long>   requirementId         = config.getRequirementId();
        Optional<String> formVersionId         = config.getParam(CURRENT_FORM_VERSION_ID);
        Optional<String> currentRequirementId  = config.getParam(CURRENT_REQUIREMENT_ID);
        Optional<String> previousRequirementId = config.getParam(PREVIOUS_REQUIREMENT_ID);

        if (formVersionId.isPresent()) {
            Long current  = Long.valueOf(formVersionId.get());
            Long previous = config.getParam(PREVIOUS_FORM_VERSION_ID).map(NumberUtils::toLong).orElse(null);
            diffSummary = singularDiffService.diffFormVersions(current, previous);

        } else if (currentRequirementId.isPresent() && previousRequirementId.isPresent()) {

            Long current  = Long.valueOf(currentRequirementId.get());
            Long previous = Long.valueOf(previousRequirementId.get());
            diffSummary = singularDiffService.diffRequirementsLastMainForms(current, previous);

        } else if (requirementId.isPresent()) {
            diffSummary = singularDiffService.diffFromPrevious(requirementId.get());
        }

        if (diffSummary != null) {
            add(contentGrid);
            adicionarDatas(diffSummary.getPreviousFormVersionDate(), diffSummary.getCurrentFormVersionDate());
            add(new DiffVisualizer("diff", diffSummary.getDiff()));
        }
    }

    private void adicionarDatas(Date originalDate, Date newerDate) {
        BSRow container = contentGrid.newRow();
        appendDate(container, "Data da modificação anterior:", originalDate);
        appendDate(container, "Data da modificação atual:", newerDate);


        contentGrid.newRow().newCol(2)
                .newFormGroup()
                .appendLabel(new BSLabel("label", $m.ofValue("")))
                .appendComponent(id -> new ModuleButtonFactory(config).getViewVersionButton(id, diffSummary.getPreviousFormVersionId()));
    }

    private void appendDate(BSRow container, String fieldLabel, Date data) {
        container.newCol(2)
                .newFormGroup()
                .appendLabel(new BSLabel("label", fieldLabel))
                .appendTag("div", new BOutputPanel("data", $m.ofValue(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(data))));
    }


    @Override
    protected IModel<String> getContentTitle() {
        return new Model<>();
    }

    @Override
    protected IModel<String> getContentSubtitle() {
        return new Model<>();
    }

    @Override
    protected boolean isWithMenu() {
        return false;
    }
}