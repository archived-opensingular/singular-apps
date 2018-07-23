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

import java.util.ArrayList;
import java.util.List;

import org.opensingular.flow.schedule.IScheduledJob;
import org.opensingular.flow.schedule.quartz.QuartzScheduleService;
import org.opensingular.lib.commons.util.Loggable;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

public class TransactionalQuartzScheduledService extends QuartzScheduleService implements Loggable {

    private boolean contextRefreshed;
    private List<IScheduledJob> toBeScheduled = new ArrayList<>();

    @EventListener(ContextRefreshedEvent.class)
    public synchronized void init() {
        contextRefreshed = true;
        toBeScheduled.forEach(this::internalSchedule);
        toBeScheduled.clear();
    }

    @Override
    public synchronized void schedule(IScheduledJob scheduledJob) {
        if (contextRefreshed) {
            internalSchedule(scheduledJob);
        } else {
            toBeScheduled.add(scheduledJob);
        }
    }

    private void internalSchedule(IScheduledJob scheduledJob) {
        super.schedule(new TransactionalScheduledJobProxy(scheduledJob));
        getLogger().info("Job({}) scheduled.", scheduledJob);
    }
}
