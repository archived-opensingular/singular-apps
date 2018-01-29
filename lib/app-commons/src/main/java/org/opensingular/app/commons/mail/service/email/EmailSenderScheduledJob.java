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
package org.opensingular.app.commons.mail.service.email;

import org.opensingular.app.commons.mail.service.dto.Email;
import org.opensingular.flow.schedule.IScheduleData;
import org.opensingular.flow.schedule.IScheduledJob;
import org.opensingular.lib.commons.util.Loggable;

import javax.inject.Inject;


public class EmailSenderScheduledJob implements IScheduledJob, Loggable {
    
    @Inject
    private EmailSender emailSender;
    
    @Inject
    private EmailPersistenceService emailPersistenceService;
    
    private IScheduleData scheduleData;
    
    private int emailsPerPage = 20;
    
    public EmailSenderScheduledJob(IScheduleData scheduleData) {
        super();
        this.scheduleData = scheduleData;
    }

    @Override
    public IScheduleData getScheduleData() {
        return scheduleData;
    }

    @Override
    public Object run() {
        final int totalPendingRecipients = emailPersistenceService.countPendingRecipients();
        int pending = totalPendingRecipients;
        int page = 0, sent = 0;
        while (pending > 0) {
            for (Email.Addressee addressee : emailPersistenceService.listPendingRecipients(page * emailsPerPage, emailsPerPage)) {
                if(emailSender.send(addressee)){
                    emailPersistenceService.markAsSent(addressee);
                    sent++;
                }
            }
            pending -= emailsPerPage;
            page++;
        }
        getLogger().info("{} sent from total of {}", sent, totalPendingRecipients);
        return sent + " sent from total of "+totalPendingRecipients;
    }

    public void setEmailsPerPage(int emailsPerPage) {
        this.emailsPerPage = emailsPerPage;
    }
        
    @Override
    public String getId() {
        return "EmailSender";
    }

    @Override
    public String toString() {
        return "EmailSenderScheduledJob [getScheduleData()=" + getScheduleData() + ", getId()=" + getId() + "]";
    }

}
