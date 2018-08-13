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

import java.util.List;

import org.opensingular.form.persistence.entity.AttachmentEntity;
import org.opensingular.form.persistence.entity.FormAttachmentEntity;
import org.opensingular.form.persistence.entity.FormAttachmentEntityId;
import org.opensingular.form.persistence.entity.FormVersionEntity;

public interface IFormAttachmentService {

    void saveNewFormAttachmentEntity(AttachmentEntity attachmentEntity, FormVersionEntity currentFormVersion);

    void deleteFormAttachmentEntity(AttachmentEntity attachmentEntity, FormVersionEntity formVersionEntity);

    FormAttachmentEntity findFormAttachmentEntity(AttachmentEntity attachmentEntity, FormVersionEntity formVersionEntity);

    FormAttachmentEntityId createFormAttachmentEntityId(AttachmentEntity attachmentEntity, FormVersionEntity formVersion);

    FormAttachmentEntityId createFormAttachmentEntityId(FormVersionEntity formVersion, AttachmentEntity attachmentEntity);

    List<FormAttachmentEntity> findAllByVersion(FormVersionEntity formVersionEntity);

    void deleteFormAttachmentEntity(FormAttachmentEntity formAttachmentEntity);
}
