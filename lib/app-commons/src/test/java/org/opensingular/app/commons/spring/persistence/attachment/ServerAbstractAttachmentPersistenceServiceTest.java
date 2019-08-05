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

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.opensingular.form.persistence.dao.AttachmentDao;
import org.opensingular.form.persistence.dto.AttachmentRef;
import org.opensingular.form.persistence.entity.AttachmentEntity;

import javax.inject.Inject;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ServerAbstractAttachmentPersistenceServiceTest {

    @Mock
    private File file;

    @Mock
    private AttachmentDao attachmentDao;

    @Mock
    private InputStream inputStream;

    @InjectMocks
    private MockServerAbstractAttachmentPersistenceService attachmentPersistenceService;

    @Test
    public void addAttachment() throws Exception {

        long myFileCod = 1L;
        long myFileLength = 10L;
        String myFileName = "abc.pdf";
        String myFileHash = "123456456456456";
        AttachmentEntity attachmentEntity = new AttachmentEntity();

        attachmentEntity.setCod(myFileCod);
        attachmentEntity.setName(myFileName);
        attachmentEntity.setHashSha1(myFileHash);

        when(attachmentDao.insert(eq(inputStream), eq(myFileLength), eq(myFileName), eq(myFileHash))).thenReturn(attachmentEntity);

        AttachmentRef attachmentRef = attachmentPersistenceService.addAttachment(file, myFileLength, myFileName, myFileHash);

        Assert.assertEquals(attachmentRef.getId(), String.valueOf(myFileCod));
        Assert.assertEquals(attachmentRef.getName(), myFileName);
        Assert.assertEquals(attachmentRef.getHashSHA1(), myFileHash);

    }

    static class MockServerAbstractAttachmentPersistenceService extends ServerAbstractAttachmentPersistenceService {

        @Inject
        private InputStream inputStream;

        @Override
        InputStream getFileInputStream(File file) throws FileNotFoundException {
            return inputStream;
        }
    }

}