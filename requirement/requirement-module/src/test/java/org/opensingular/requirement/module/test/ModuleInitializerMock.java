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

package org.opensingular.requirement.module.test;

import org.opensingular.requirement.commons.config.SchedulerInitializer;
import org.opensingular.requirement.commons.test.CommonsInitializerMock;
import org.opensingular.requirement.module.config.AttachmentGCSchedulerInitializer;
import org.opensingular.requirement.module.config.MailSenderSchedulerInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;


public class ModuleInitializerMock extends CommonsInitializerMock {

    public ModuleInitializerMock(AnnotationConfigWebApplicationContext applicationContext) {
        super(applicationContext);
    }

    @Override
    public SchedulerInitializer schedulerConfiguration() {
        return new SchedulerInitializer() {
            @Override
            public Class<?> mailConfiguration() {
                return MailSenderSchedulerInitializer.class;
            }

            @Override
            public Class<?> attachmentGCConfiguration() {
                return AttachmentGCSchedulerInitializer.class;
            }
        };
    }


}
