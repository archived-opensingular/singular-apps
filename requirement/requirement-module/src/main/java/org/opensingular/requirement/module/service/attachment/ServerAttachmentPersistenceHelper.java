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

package org.opensingular.requirement.module.service.attachment;

import org.opensingular.form.SInstances;
import org.opensingular.form.document.SDocument;
import org.opensingular.form.persistence.entity.FormAttachmentEntity;
import org.opensingular.form.persistence.entity.FormVersionEntity;
import org.opensingular.form.service.IFormService;
import org.opensingular.form.type.core.attachment.IAttachmentPersistenceHandler;
import org.opensingular.form.type.core.attachment.SIAttachment;
import org.opensingular.form.type.core.attachment.helper.DefaultAttachmentPersistenceHelper;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
public class ServerAttachmentPersistenceHelper extends DefaultAttachmentPersistenceHelper {

    private IFormService formService;

    private IFormAttachmentService formAttachmentService;

    @Inject
    public ServerAttachmentPersistenceHelper(IFormService formService,
                                             IFormAttachmentService formAttachmentService) {
        this.formService = formService;
        this.formAttachmentService = formAttachmentService;
    }

    @Override
    public void doPersistence(SDocument document,
                              IAttachmentPersistenceHandler temporaryHandler,
                              IAttachmentPersistenceHandler persistenceHandler) {

        final List<FormAttachmentEntity> currentFormAttachmentEntities = getCurrentFormAttachmentEntities(document);

        findAttachments(document).forEach(attachment -> {
            removeFormAttachment(currentFormAttachmentEntities, attachment);
            handleAttachment(attachment, temporaryHandler, persistenceHandler);
        });

        currentFormAttachmentEntities.forEach(formAttachmentService::deleteFormAttachmentEntity);
    }

    private void removeFormAttachment(List<FormAttachmentEntity> currentFormAttachmentEntities, SIAttachment attachment) {
        currentFormAttachmentEntities.stream()
                .filter(f -> f.getAttachmentEntity().getCod().toString().equals(attachment.getFileId()))
                .findFirst()
                .ifPresent(currentFormAttachmentEntities::remove);
    }

    private List<FormAttachmentEntity> getCurrentFormAttachmentEntities(SDocument document) {
        List<FormAttachmentEntity> result = new ArrayList<FormAttachmentEntity>();
        Optional<FormVersionEntity> formVersionEntity = formService.findCurrentFormVersion(document);
        if(formVersionEntity.isPresent()){
            result = formAttachmentService.findAllByVersion(formVersionEntity.get());
        }
        return result;
    }

    protected List<SIAttachment> findAttachments(SDocument document) {
        return SInstances.streamDescendants(document.getRoot(), true)
                .filter(instance -> instance instanceof SIAttachment)
                .map(instance -> (SIAttachment) instance)
                .collect(Collectors.toList());
    }

}