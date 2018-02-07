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

package org.opensingular.server.commons.service.attachment;

import org.opensingular.form.persistence.entity.AbstractFormAttachmentEntity;
import org.opensingular.form.persistence.entity.AttachmentContentEntity;
import org.opensingular.form.persistence.entity.AttachmentEntity;
import org.opensingular.form.persistence.entity.FormAttachmentEntityId;
import org.opensingular.form.persistence.entity.FormVersionEntity;

public abstract class AbstractFormAttachmentService<T extends AttachmentEntity, C extends AttachmentContentEntity, F extends AbstractFormAttachmentEntity<T>> implements IFormAttachmentService {

    /**
     * cria a chave utilizando a ref e o documento
     *
     * @param formVersion a versao do formulario
     * @return a pk ou null caso nao consiga cronstruir
     */
    @Override
    public FormAttachmentEntityId createFormAttachmentEntityId(AttachmentEntity attachmentEntity, FormVersionEntity formVersion) {
        if (formVersion != null || attachmentEntity != null) {
            return createFormAttachmentEntityId(formVersion, attachmentEntity);
        }
        return null;
    }

    /**
     * cria a primaria key de form attachment entity
     *
     * @param formVersion      versao do formulario
     * @param attachmentEntity anexo
     * @return a chave instanciada, null caso algum parametro seja nulo
     */
    @Override
    public FormAttachmentEntityId createFormAttachmentEntityId(FormVersionEntity formVersion, AttachmentEntity attachmentEntity) {
        if (formVersion != null && attachmentEntity != null) {
            return new FormAttachmentEntityId(formVersion.getCod(), attachmentEntity.getCod());
        }
        return null;
    }

}
