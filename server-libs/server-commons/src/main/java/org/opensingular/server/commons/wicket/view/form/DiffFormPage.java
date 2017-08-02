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

package org.opensingular.server.commons.wicket.view.form;


import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.opensingular.form.SInstance;
import org.opensingular.form.persistence.entity.FormEntity;
import org.opensingular.form.persistence.entity.FormVersionEntity;
import org.opensingular.form.service.IFormService;
import org.opensingular.form.util.diff.DocumentDiff;
import org.opensingular.form.util.diff.DocumentDiffUtil;
import org.opensingular.lib.wicket.util.bootstrap.layout.BSGrid;
import org.opensingular.lib.wicket.util.bootstrap.layout.BSLabel;
import org.opensingular.lib.wicket.util.bootstrap.layout.BSRow;
import org.opensingular.lib.wicket.util.datatable.BSDataTable;
import org.opensingular.lib.wicket.util.output.BOutputPanel;
import org.opensingular.server.commons.persistence.entity.form.DraftEntity;
import org.opensingular.server.commons.persistence.entity.form.FormPetitionEntity;
import org.opensingular.server.commons.service.FormPetitionService;
import org.opensingular.server.commons.service.PetitionInstance;
import org.opensingular.server.commons.service.PetitionService;
import org.opensingular.server.commons.service.PetitionUtil;
import org.opensingular.server.commons.wicket.view.template.ServerTemplate;
import org.opensingular.server.commons.wicket.view.util.ActionContext;
import org.opensingular.server.commons.wicket.view.util.ModuleButtonFactory;

import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.opensingular.lib.wicket.util.util.Shortcuts.$m;

public class DiffFormPage extends ServerTemplate {

    @Inject
    protected FormPetitionService<?> formPetitionService;

    @Inject
    protected IFormService formService;

    @Inject
    private PetitionService<?, ?> petitionService;

    private ActionContext config;
    protected BSDataTable<DocumentDiff, String> tabela;
    protected DocumentDiff                      diff;
    protected BSGrid contentGrid = new BSGrid("content");
    private FormVersionEntity originalFormVersion;

    public DiffFormPage(ActionContext config) {
        this.config = config;
    }

    @Override
    protected void onConfigure() {
        super.onConfigure();
        Optional<Long> petitionId = config.getPetitionId();

        if (petitionId.isPresent()) {

            PetitionInstance petition = petitionService.getPetition(petitionId.get());
            String typeName = PetitionUtil.getTypeName(petition);
            Optional<DraftEntity> draftEntity = petition.getEntity().currentEntityDraftByType(typeName);

            SInstance original = null;
            SInstance newer;

            Date originalDate = null;
            Date newerDate;

            if (draftEntity.isPresent()) {
                Optional<FormPetitionEntity> lastForm = formPetitionService.findLastFormPetitionEntityByType(petition,
                        typeName);
                if (lastForm.isPresent()) {
                    FormEntity originalForm = lastForm.get().getForm();
                    original = formPetitionService.getSInstance(originalForm);
                    originalFormVersion = originalForm.getCurrentFormVersionEntity();
                    originalDate = originalFormVersion.getInclusionDate();
                }

                FormVersionEntity newerFormVersion = draftEntity.get().getForm().getCurrentFormVersionEntity();
                FormEntity newerForm = newerFormVersion.getFormEntity();
                newer = formPetitionService.getSInstance(newerForm);
                newerDate = draftEntity.get().getEditionDate();

            } else {
                List<FormVersionEntity> formPetitionEntities = petitionService
                        .buscarDuasUltimasVersoesForm(petitionId.get());

                originalFormVersion = formPetitionEntities.get(1);
                original = formPetitionService.getSInstance(originalFormVersion);
                originalDate = originalFormVersion.getInclusionDate();

                FormVersionEntity newerFormVersion = formPetitionEntities.get(0);
                newer = formPetitionService.getSInstance(newerFormVersion);
                newerDate = newerFormVersion.getInclusionDate();
            }

            diff = DocumentDiffUtil.calculateDiff(original, newer).removeUnchangedAndCompact();

            queue(contentGrid);
            adicionarDatas(originalDate, newerDate);
            queue(new DiffVisualizer("diff", diff));

        }
    }

    private void adicionarDatas(Date originalDate, Date newerDate) {
        BSRow container = contentGrid.newRow();
        appendDate(container, "Data da modificação anterior:", originalDate);
        appendDate(container, "Data da modificação atual:", newerDate);


        contentGrid.newRow().newCol(2)
                .newFormGroup()
                .appendLabel(new BSLabel("label", $m.ofValue("")))
                .appendComponent(id -> new ModuleButtonFactory(config).getViewVersionButton(id, originalFormVersion.getCod()));
    }

    private void appendDate(BSRow container, String labelCampo, Date data) {
        container.newCol(2)
                .newFormGroup()
                .appendLabel(new BSLabel("label", labelCampo))
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