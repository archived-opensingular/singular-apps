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

package org.opensingular.server.commons.service;


import org.opensingular.form.SInstance;
import org.opensingular.form.persistence.entity.FormEntity;
import org.opensingular.form.persistence.entity.FormVersionEntity;
import org.opensingular.form.service.IFormService;
import org.opensingular.form.util.diff.DocumentDiff;
import org.opensingular.form.util.diff.DocumentDiffUtil;
import org.opensingular.server.commons.persistence.entity.form.DraftEntity;
import org.opensingular.server.commons.persistence.entity.form.FormPetitionEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class SingularDiffService {

    @Inject
    protected FormPetitionService<?> formPetitionService;

    @Inject
    protected IFormService formService;

    @Inject
    private PetitionService<?, ?> petitionService;

    public DiffSummary diffFromPrevious(@Nonnull Long petitionId) {
        FormVersionEntity originalFormVersion = null;
        FormVersionEntity newerFormVersion;

        PetitionInstance      petition    = petitionService.getPetition(petitionId);
        String                typeName    = PetitionUtil.getTypeName(petition);
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

            newerFormVersion = draftEntity.get().getForm().getCurrentFormVersionEntity();
            FormEntity newerForm = newerFormVersion.getFormEntity();
            newer = formPetitionService.getSInstance(newerForm);
            newerDate = draftEntity.get().getEditionDate();

        }
        else {
            List<FormVersionEntity> formPetitionEntities = petitionService
                    .buscarDuasUltimasVersoesForm(petitionId);

            originalFormVersion = formPetitionEntities.get(1);
            original = formPetitionService.getSInstance(originalFormVersion);
            originalDate = originalFormVersion.getInclusionDate();

            newerFormVersion = formPetitionEntities.get(0);
            newer = formPetitionService.getSInstance(newerFormVersion);
            newerDate = newerFormVersion.getInclusionDate();
        }

        DocumentDiff diff = DocumentDiffUtil.calculateDiff(original, newer).removeUnchangedAndCompact();
        return new DiffSummary(newerFormVersion.getCod(), originalFormVersion != null ? originalFormVersion.getCod() : null, newerDate, originalDate, diff);
    }

    public static class DiffSummary implements Serializable {
        private Long         currentFormVersionId;
        private Long         previousFormVersionId;
        private Date         currentFormVersionDate;
        private Date         previousFormVersionDate;
        private DocumentDiff diff;

        public DiffSummary(Long currentFormVersionId, Long previousFormVersionId, Date currentFormVersionDate, Date previousFormVersionDate, DocumentDiff diff) {
            this.currentFormVersionId = currentFormVersionId;
            this.previousFormVersionId = previousFormVersionId;
            this.currentFormVersionDate = currentFormVersionDate;
            this.previousFormVersionDate = previousFormVersionDate;
            this.diff = diff;
        }

        public Long getCurrentFormVersionId() {
            return currentFormVersionId;
        }

        public Long getPreviousFormVersionId() {
            return previousFormVersionId;
        }

        public Date getCurrentFormVersionDate() {
            return currentFormVersionDate;
        }

        public Date getPreviousFormVersionDate() {
            return previousFormVersionDate;
        }

        public DocumentDiff getDiff() {
            return diff;
        }
    }

}
