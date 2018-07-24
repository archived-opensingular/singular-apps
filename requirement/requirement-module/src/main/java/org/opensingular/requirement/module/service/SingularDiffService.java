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

package org.opensingular.requirement.module.service;


import org.opensingular.form.SInstance;
import org.opensingular.form.persistence.dao.FormVersionDAO;
import org.opensingular.form.persistence.entity.FormEntity;
import org.opensingular.form.persistence.entity.FormVersionEntity;
import org.opensingular.form.util.diff.DocumentDiff;
import org.opensingular.form.util.diff.DocumentDiffUtil;
import org.opensingular.requirement.module.persistence.entity.form.DraftEntity;
import org.opensingular.requirement.module.persistence.entity.form.FormRequirementEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Transactional
public class SingularDiffService {

    @Inject
    protected FormRequirementService<?> formRequirementService;

    @Inject
    protected FormVersionDAO formVersionDAO;

    @Inject
    private RequirementService<?, ?> requirementService;

    /**
     * * Diffs, any two form versions be it draft or not or same type or not or whatever
     * @param currentFormVersion
     * @param previousFormVersion
     * @return
     */
    public DiffSummary diffFormVersions(@Nonnull Long currentFormVersion, @Nullable Long previousFormVersion) {
        FormVersionEntity formVersionEntity = null;
        if (previousFormVersion != null){
            formVersionEntity = formVersionDAO.findOrException(previousFormVersion);
        }
        return diffFormVersions(formVersionDAO.findOrException(currentFormVersion), formVersionEntity);
    }

    /**
     * @see SingularDiffService#diffFormVersions(Long, Long)
     * @param current
     * @param previous
     * @return
     */
    public DiffSummary diffFormVersions(@Nonnull FormVersionEntity current, @Nullable FormVersionEntity previous) {
        SInstance currentInstance = formRequirementService.getSInstance(current);
        Date      currentDate     = current.getInclusionDate();

        SInstance previousInstance = null;
        Date      previousDate     = null;

        if (previous != null) {
            previousInstance = formRequirementService.getSInstance(previous);
            previousDate = previous.getInclusionDate();
        }

        DocumentDiff diff = DocumentDiffUtil.calculateDiff(previousInstance, currentInstance).removeUnchangedAndCompact();
        return new DiffSummary(current.getCod(), Optional.ofNullable(previous).map(FormVersionEntity::getCod).orElse(null), currentDate, previousDate, diff);

    }

    /**
     * Diffs the last main form version of each requirement.
     * @param currentRequirementId
     * @param otherRequirementId
     * @return
     */
    public DiffSummary diffRequirementsLastMainForms(@Nonnull Long currentRequirementId, @Nonnull Long otherRequirementId) {
        RequirementInstance currentRequirement = requirementService.getRequirementEntity(currentRequirementId);
        RequirementInstance otherRequirement   = requirementService.getRequirementEntity(otherRequirementId);
        return diffFormVersions(currentRequirement.getMainFormCurrentFormVersion(), otherRequirement.getMainFormCurrentFormVersion());
    }


    /**
     * Diff current form version from the previous one. If the most recent version is a draft it will be compared with
     * the last closed version.
     * @param requirementId
     * @return
     */
    public DiffSummary diffFromPrevious(@Nonnull Long requirementId) {
        FormVersionEntity previousFormVersion = null;
        FormVersionEntity currentFormVersion;

        RequirementInstance   requirement = requirementService.getRequirementEntity(requirementId);
        String                typeName    = RequirementUtil.getTypeName(requirement);
        Optional<DraftEntity> draftEntity = requirement.getEntity().currentEntityDraftByType(typeName);


        if (draftEntity.isPresent()) {
            Optional<FormRequirementEntity> lastForm = formRequirementService.findLastFormRequirementEntityByType(requirement,
                    typeName);
            if (lastForm.isPresent()) {
                FormEntity originalForm = lastForm.get().getForm();
                previousFormVersion = originalForm.getCurrentFormVersionEntity();
            }
            currentFormVersion = draftEntity.get().getForm().getCurrentFormVersionEntity();

        } else {
            List<FormVersionEntity> formRequirementEntities = requirementService
                    .buscarDuasUltimasVersoesForm(requirementId);

            previousFormVersion = formRequirementEntities.get(1);

            currentFormVersion = formRequirementEntities.get(0);

        }

        return diffFormVersions(currentFormVersion, previousFormVersion);
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
