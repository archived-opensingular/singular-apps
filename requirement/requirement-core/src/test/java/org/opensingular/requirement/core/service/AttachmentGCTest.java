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

package org.opensingular.requirement.core.service;

import org.junit.Assert;
import org.junit.Test;
import org.opensingular.flow.schedule.IScheduleData;
import org.opensingular.form.persistence.dao.AttachmentContentDao;
import org.opensingular.form.persistence.dao.AttachmentDao;
import org.opensingular.form.persistence.entity.AttachmentContentEntity;
import org.opensingular.form.persistence.entity.AttachmentEntity;
import org.opensingular.requirement.core.service.AttachmentGCJob;
import org.opensingular.requirement.core.test.SingularServerBaseTest;


import javax.inject.Inject;
import javax.sql.rowset.serial.SerialBlob;
import javax.transaction.Transactional;
import java.sql.SQLException;
import java.util.Calendar;

public class AttachmentGCTest extends SingularServerBaseTest {
    @Inject
    private AttachmentGCJob job;

    @Inject
    private AttachmentContentDao<AttachmentContentEntity> attachmentContentDao;

    @Inject
    private AttachmentDao<AttachmentEntity,AttachmentContentEntity> attachmentDao;

    @Test
    public void attachmentGCJobWithoutFilesToRemoveTest(){

        IScheduleData scheduleData = job.getScheduleData();
        Assert.assertEquals("Diário às 1:01h", scheduleData.getDescription());
        Assert.assertEquals("0 1 1 * * ?", scheduleData.getCronExpression());

        Assert.assertEquals("AttachmentGCJob " +
            "[getScheduleData()=ScheduleDataImpl [cronExpression=0 1 1 * * ?, description=Diário às 1:01h], getId()=AttachmentGC]",
            job.toString());

        Assert.assertEquals("Removed 0 old orphan attachments from 0 total.", job.run());
    }

    @Test
    @Transactional
    public void attachmentGCJobWithFilesToRemoveTest() throws Exception {
        saveAttachmentEntity();
        Assert.assertEquals("Removed 1 old orphan attachments from 1 total.", job.run());
    }

    private void saveAttachmentEntity() throws SQLException {
        AttachmentContentEntity attachment = new AttachmentContentEntity();
        attachment.setSize(123456);
        attachment.setHashSha1("SHA-1");
        attachment.setContent(new SerialBlob("A simple blob with value".getBytes()));

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH)-2);
        attachment.setInclusionDate(calendar.getTime());
        attachmentContentDao.insert(attachment);

        AttachmentEntity entity = new AttachmentEntity();
        entity.setCreationDate(calendar.getTime());
        entity.setHashSha1("SHA1");
        entity.setName("name");
        entity.setSize(3512);
        entity.setCodContent(attachment.getCod());

        attachmentDao.insert(entity);
    }
}
