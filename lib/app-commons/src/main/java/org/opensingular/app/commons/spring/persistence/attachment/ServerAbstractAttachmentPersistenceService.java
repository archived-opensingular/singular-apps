/*
 *   Copyright (C) 2016 Singular Studios (a.k.a Atom Tecnologia) - www.opensingular.com
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package org.opensingular.app.commons.spring.persistence.attachment;

import org.opensingular.form.SingularFormException;
import org.opensingular.form.persistence.dto.AttachmentRef;
import org.opensingular.form.persistence.entity.AttachmentContentEntity;
import org.opensingular.form.persistence.entity.AttachmentEntity;
import org.opensingular.form.persistence.service.AttachmentPersistenceService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Classe base para os anexos do singular server
 *
 * @param <T> a entidade de anexo
 * @param <C> a entidade de anexo conteudo
 */
public abstract class ServerAbstractAttachmentPersistenceService<T extends AttachmentEntity, C extends AttachmentContentEntity> extends AttachmentPersistenceService<T, C> {

    /**
     * Adiciona o anexo ao banco de dados, faz o calculo de HASH
     *
     * @param file   o arquivo a ser inserido
     * @param length tamanho do arquivo
     * @param name   o nome
     * @return a referencia
     */
    @Override
    public AttachmentRef addAttachment(File file, long length, String name, String hash) {
        try (InputStream in = getFileInputStream(file)) {
            return createRef(attachmentDao.insert(in, length, name, hash));
        } catch (Exception e) {
            throw new SingularFormException("Erro lendo origem de dados", e);
        }
    }

    InputStream getFileInputStream(File file) throws FileNotFoundException {
        return new FileInputStream(file);
    }

}