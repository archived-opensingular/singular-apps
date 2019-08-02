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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.opensingular.form.document.SDocument;
import org.opensingular.form.persistence.entity.AttachmentEntity;
import org.opensingular.form.persistence.entity.FormAttachmentEntity;
import org.opensingular.form.persistence.entity.FormAttachmentEntityId;
import org.opensingular.form.persistence.entity.FormVersionEntity;
import org.opensingular.form.service.IFormService;
import org.opensingular.form.type.core.attachment.IAttachmentPersistenceHandler;
import org.opensingular.form.type.core.attachment.SIAttachment;
import org.opensingular.form.type.core.attachment.helper.DefaultAttachmentPersistenceHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ServerAttachmentPersistenceHelperTest {

    public static final long         OBSOLTE_ATTACHMENT_ENTITY_ID = 1L;
    public static final long         NORMAL_ATTACHMENT_ENTITY_ID  = 2L;
    public static final long         FORM_VERSION_ID              = 1L;
    @Mock
    private             IFormService formService;

    @Mock
    private IFormAttachmentService formAttachmentService;

    @Mock
    private DefaultAttachmentPersistenceHelper defaultAttachmentPersistenceHelper;

    @Mock
    private SIAttachment obsoletAttachment;

    @Mock
    private SIAttachment newtAttachment;

    @Mock
    private SIAttachment normalAttachment;

    @Mock
    private FormAttachmentEntity obsoletFormAttachmentEntity;

    @Mock
    private FormAttachmentEntity normalFormAttachmentEntity;

    @Mock
    private AttachmentEntity obsoletAttachmentEntity;

    @Mock
    private AttachmentEntity normalAttachmentEntity;

    @Mock
    private SDocument document;

    @Mock
    private IAttachmentPersistenceHandler persistenceHandler;

    @Mock
    private IAttachmentPersistenceHandler temporaryHandler;

    @Mock
    private FormVersionEntity formVersionEntity;

    private ServerAttachmentPersistenceHelper serverAttachmentPersistenceHelper;

    @Before
    public void setUp() {


        when(obsoletFormAttachmentEntity.getCod()).thenReturn(new FormAttachmentEntityId(FORM_VERSION_ID, OBSOLTE_ATTACHMENT_ENTITY_ID));

        when(normalFormAttachmentEntity.getCod()).thenReturn(new FormAttachmentEntityId(FORM_VERSION_ID, NORMAL_ATTACHMENT_ENTITY_ID));



        when(normalAttachment.getFileId()).thenReturn("2");

        when(formService.findCurrentFormVersion(document)).thenReturn(Optional.of(formVersionEntity));
        when(formAttachmentService.findAllByVersion(eq(formVersionEntity))).thenReturn(new ArrayList<>(Arrays.asList(obsoletFormAttachmentEntity, normalFormAttachmentEntity)));

        serverAttachmentPersistenceHelper = new ServerAttachmentPersistenceHelper(formService, formAttachmentService) {
            @Override
            protected List<SIAttachment> findAttachments(SDocument document) {
                return new ArrayList<>(Arrays.asList(newtAttachment, normalAttachment));
            }

            @Override
            public void handleAttachment(SIAttachment attachment, IAttachmentPersistenceHandler temporaryHandler, IAttachmentPersistenceHandler persistenceHandler) {
                defaultAttachmentPersistenceHelper.handleAttachment(attachment, temporaryHandler, persistenceHandler);
            }
        };
    }

    @Test
    public void testIfDoPersistenceRemoveObsoletFormAttachmentEntities() {

        serverAttachmentPersistenceHelper.doPersistence(document, temporaryHandler, persistenceHandler);

        verify(defaultAttachmentPersistenceHelper).handleAttachment(eq(newtAttachment), eq(temporaryHandler), eq(persistenceHandler));
        verify(defaultAttachmentPersistenceHelper).handleAttachment(eq(normalAttachment), eq(temporaryHandler), eq(persistenceHandler));
        verifyNoMoreInteractions(defaultAttachmentPersistenceHelper);

        verify(formAttachmentService).deleteFormAttachmentEntity(eq(obsoletFormAttachmentEntity));
        verify(formAttachmentService).findAllByVersion(eq(formVersionEntity));
        verifyNoMoreInteractions(formAttachmentService);

    }



}