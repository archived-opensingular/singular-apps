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

import javax.transaction.Transactional;

import org.opensingular.form.document.SDocument;
import org.opensingular.form.persistence.entity.AttachmentContentEntity;
import org.opensingular.form.persistence.entity.AttachmentEntity;

@Transactional
public class ServerTemporaryAttachmentPersistenceService<T extends AttachmentEntity, C extends AttachmentContentEntity> extends ServerAbstractAttachmentPersistenceService<T, C> {


    /**
     * Deleta a relacional caso exita
     *
     * @param id       o id do arquivo
     * @param document documento do formulario
     */
    @Override
    public void deleteAttachment(String id, SDocument document) {
        /**
         * do nothing
         */
    }

}