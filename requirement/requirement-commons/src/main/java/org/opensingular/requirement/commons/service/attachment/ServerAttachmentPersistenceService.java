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

package org.opensingular.requirement.commons.service.attachment;

import org.opensingular.form.document.SDocument;
import org.opensingular.form.persistence.dto.AttachmentRef;
import org.opensingular.form.persistence.entity.AttachmentContentEntity;
import org.opensingular.form.persistence.entity.AttachmentEntity;
import org.opensingular.form.persistence.entity.FormVersionEntity;
import org.opensingular.form.service.IFormService;
import org.opensingular.form.type.core.attachment.AttachmentCopyContext;
import org.opensingular.form.type.core.attachment.IAttachmentRef;
import org.opensingular.form.type.core.attachment.helper.IAttachmentPersistenceHelper;
import org.opensingular.requirement.commons.exception.SingularServerException;

import java.util.Optional;

import javax.inject.Inject;
import javax.transaction.Transactional;

@Transactional
public class ServerAttachmentPersistenceService<T extends AttachmentEntity, C extends AttachmentContentEntity> extends ServerAbstractAttachmentPersistenceService<T, C> {

    @Inject
    protected transient IFormService formService;

    @Inject
    protected transient IFormAttachmentService formAttachmentService;

    @Inject
    protected transient IAttachmentPersistenceHelper attachmentPersistenceHelper;

    /**
     * Faz o vinculo entre anexo persistido e formversionentity
     *
     * @param ref  referencia a um anexo ja persistido no banco de dados
     * @param sdoc documento atual do formulario
     * @return os dados de contexto para ações pos copia
     */
    @Override
    public AttachmentCopyContext<AttachmentRef> copy(IAttachmentRef ref, SDocument sdoc) {
        if (!(ref instanceof AttachmentRef)) {
            return super.copy(ref, sdoc);
        }
        if (sdoc != null ) {
            Optional<FormVersionEntity> fve = formService.findCurrentFormVersion(sdoc);
            if(fve.isPresent()){
                formAttachmentService.saveNewFormAttachmentEntity(getAttachmentEntity(ref), fve.get());
            }
        }
        return new AttachmentCopyContext<>((AttachmentRef) ref).setDeleteOldFiles(false).setUpdateFileId(false);
    }


    /**
     * Aciona o metodo de deletar a relacional {@link FormAttachmentService#deleteFormAttachmentEntity }
     *
     * @param id       id do anexo
     * @param document o documento atual
     */
    @Override
    public void deleteAttachment(String id, SDocument document) {
        formAttachmentService.deleteFormAttachmentEntity(getAttachmentEntity(id),
                formService.findCurrentFormVersion(document)
                        .orElseThrow(() -> new SingularServerException("FormVersion não encontrado")));
    }

    @Override
    public IAttachmentPersistenceHelper getAttachmentPersistenceHelper() {
        return attachmentPersistenceHelper;
    }

}