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

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.opensingular.form.SInstance;
import org.opensingular.form.document.SDocument;
import org.opensingular.form.persistence.dao.AttachmentDao;
import org.opensingular.form.persistence.dto.AttachmentRef;
import org.opensingular.form.persistence.entity.AttachmentContentEntity;
import org.opensingular.form.persistence.entity.AttachmentEntity;
import org.opensingular.form.persistence.entity.FormVersionEntity;
import org.opensingular.form.service.IFormService;
import org.opensingular.form.type.core.attachment.AttachmentCopyContext;
import org.opensingular.requirement.module.service.attachment.FormAttachmentService;
import org.opensingular.requirement.module.service.attachment.ServerAttachmentPersistenceService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class ServerAttachmentPersistenceServiceTest {

    @Mock
    private AttachmentRef attachmentRef;

    @Mock
    private SDocument document;

    @Mock
    private FormAttachmentService formAttachmentService;

    @Mock
    private SInstance root;

    @Mock
    private IFormService formService;

    @Mock
    private FormVersionEntity formVersionEntity;

    @Mock
    private AttachmentDao<AttachmentEntity, AttachmentContentEntity> attachmentDao;

    @InjectMocks
    private ServerAttachmentPersistenceService serverAttachmentPersistenceService;

    @Test
    public void copy() throws Exception {

        final Long             myAttachmenyID   = 1L;
        final AttachmentEntity attachmentEntity = new AttachmentEntity();

        when(attachmentRef.getId()).thenReturn(String.valueOf(myAttachmenyID));
        when(formService.findCurrentFormVersion(eq(document))).thenReturn(Optional.of(formVersionEntity));
        when(attachmentDao.findOrException(myAttachmenyID)).thenReturn(attachmentEntity);

        AttachmentCopyContext context = serverAttachmentPersistenceService.copy(attachmentRef, document);

        assertFalse(context.isDeleteOldFiles());
        assertFalse(context.isUpdateFileId());
        assertEquals(attachmentRef, context.getNewAttachmentRef());

        verify(formAttachmentService).saveNewFormAttachmentEntity(eq(attachmentEntity), eq(formVersionEntity));
    }

    @Test
    public void deleteAttachment() throws Exception {

        final Long             myAttachmenyID   = 10L;
        final AttachmentEntity attachmentEntity = new AttachmentEntity();

        attachmentEntity.setCod(1L);

        when(formService.findCurrentFormVersion(eq(document))).thenReturn(Optional.of(formVersionEntity));
        when(attachmentDao.findOrException(myAttachmenyID)).thenReturn(attachmentEntity);

        serverAttachmentPersistenceService.deleteAttachment(String.valueOf(myAttachmenyID), document);

        verify(formAttachmentService).deleteFormAttachmentEntity(eq(attachmentEntity), eq(formVersionEntity));
    }

}