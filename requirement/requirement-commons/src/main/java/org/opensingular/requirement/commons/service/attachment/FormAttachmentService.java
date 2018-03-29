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

import org.opensingular.form.persistence.dao.FormAttachmentDAO;
import org.opensingular.form.persistence.entity.*;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
public class FormAttachmentService extends AbstractFormAttachmentService<AttachmentEntity, AttachmentContentEntity, FormAttachmentEntity> {

    @Inject
    private FormAttachmentDAO formAttachmentDAO;

    private FormAttachmentEntity saveNewFormAttachmentEntity(FormAttachmentEntityId formAttachmentPK) {
        if (formAttachmentPK != null) {
            FormAttachmentEntity fae = formAttachmentDAO.find(formAttachmentPK).orElse(null);
            if (fae == null) {
                fae = new FormAttachmentEntity(formAttachmentPK);
                formAttachmentDAO.save(fae);
            }
            return fae;
        }
        return null;
    }

    @Override
    public void saveNewFormAttachmentEntity(AttachmentEntity attachmentEntity, FormVersionEntity currentFormVersion) {
        saveNewFormAttachmentEntity(createFormAttachmentEntityId(attachmentEntity, currentFormVersion));
    }

    @Override
    public void deleteFormAttachmentEntity(AttachmentEntity attachmentEntity, FormVersionEntity formVersionEntity) {
        FormAttachmentEntity formAttachmentEntity = findFormAttachmentEntity(attachmentEntity, formVersionEntity);
        if (formAttachmentEntity != null) {
            formAttachmentDAO.delete(formAttachmentEntity);
        }
    }

    @Override
    public FormAttachmentEntity findFormAttachmentEntity(AttachmentEntity attachmentEntity, FormVersionEntity formVersionEntity) {
        FormAttachmentEntityId formAttachmentPK = createFormAttachmentEntityId(attachmentEntity, formVersionEntity);
        if (formAttachmentPK != null) {
            return formAttachmentDAO.find(formAttachmentPK).orElse(null);
        }
        return null;
    }

    @Override
    public List<FormAttachmentEntity> findAllByVersion(FormVersionEntity formVersionEntity) {
        return formAttachmentDAO.findFormAttachmentByFormVersionCod(formVersionEntity.getCod());
    }

    @Override
    public void deleteFormAttachmentEntity(FormAttachmentEntity formAttachmentEntity) {
        formAttachmentDAO.delete(formAttachmentEntity);
    }

}