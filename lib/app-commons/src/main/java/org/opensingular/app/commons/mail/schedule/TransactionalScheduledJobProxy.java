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

package org.opensingular.app.commons.mail.schedule;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.opensingular.lib.support.spring.util.ApplicationContextProvider;
import org.opensingular.schedule.IScheduleData;
import org.opensingular.schedule.IScheduledJob;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;


public class TransactionalScheduledJobProxy implements IScheduledJob {

    private IScheduledJob job;

    public TransactionalScheduledJobProxy(final IScheduledJob job) {
        this.job = job;
    }

    @Override
    public IScheduleData getScheduleData() {
        return job.getScheduleData();
    }

    @Override
    public Object run() {
        return new TransactionTemplate(ApplicationContextProvider.get().getBean(PlatformTransactionManager.class))
                .execute(status -> job.run());
    }

    /**
     * The writeObject is overriding for serialize the Injects of the beans.
     *
     * @param out
     * @throws IOException
     */
    private void writeObject(ObjectOutputStream out) throws IOException {
        String nameBean = ApplicationContextProvider.getBeanName(job);
        if (nameBean != null) {
            out.writeObject(nameBean);
        } else {
            out.defaultWriteObject();
        }
    }

    /**
     * The readObject is overriding for deserializable the beans object.
     *
     * @param in
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        Object value = in.readObject();
        if (value instanceof String) {
            job = (IScheduledJob) ApplicationContextProvider.get().getBean((String) value);
        } else {
            job = (IScheduledJob) value;
        }

    }

    @Override
    public String getId() {
        return job.getId();
    }

}