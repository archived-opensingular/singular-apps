package org.opensingular.server.commons.service.attachment;

import org.opensingular.form.SingularFormException;
import org.opensingular.form.persistence.dao.FormAttachmentDAO;
import org.opensingular.form.persistence.dto.AttachmentRef;
import org.opensingular.form.persistence.entity.AttachmentContentEntity;
import org.opensingular.form.persistence.entity.AttachmentEntity;
import org.opensingular.form.persistence.service.AttachmentPersistenceService;

import javax.inject.Inject;
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

    @Inject
    protected transient FormAttachmentDAO formAttachmentDAO;

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