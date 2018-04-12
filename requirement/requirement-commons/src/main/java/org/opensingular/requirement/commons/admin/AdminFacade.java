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

package org.opensingular.requirement.commons.admin;

import net.sf.ehcache.CacheManager;
import org.opensingular.flow.schedule.IScheduleService;
import org.opensingular.flow.schedule.ScheduledJob;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Named
public class AdminFacade {

    @Inject
    private Optional<IScheduleService> scheduleService;

    public List<String> runAllJobs() throws SchedulerException {
        List<String> runnedJobs = new ArrayList<>();
        if (scheduleService.isPresent()) {
            for (JobKey jobKey : scheduleService.get().getAllJobKeys()) {
                ScheduledJob scheduledJob = new ScheduledJob(jobKey.getName(), null, null);
                scheduleService.get().trigger(scheduledJob);
                runnedJobs.add(jobKey.getName()+ " - " + scheduledJob);
            }
        }
        return runnedJobs;
    }

    public void clearCaches() {
        CacheManager.getInstance().clearAll();
    }

}
