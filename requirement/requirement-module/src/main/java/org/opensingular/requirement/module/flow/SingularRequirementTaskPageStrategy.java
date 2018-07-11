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

package org.opensingular.requirement.module.flow;

import org.apache.wicket.markup.html.WebPage;
import org.opensingular.flow.core.ITaskPageStrategy;
import org.opensingular.flow.core.SUser;
import org.opensingular.flow.core.TaskInstance;

public class SingularRequirementTaskPageStrategy implements ITaskPageStrategy {

    private SingularWebRef webRef;

    public SingularRequirementTaskPageStrategy(Class<? extends WebPage> page) {
        this.webRef = new SingularWebRef(page);
    }

    public SingularRequirementTaskPageStrategy() {

    }

    public static final SingularRequirementTaskPageStrategy of(Class<? extends WebPage> page) {
        return new SingularRequirementTaskPageStrategy(page);
    }

    @Override
    public SingularWebRef getPageFor(TaskInstance taskInstance, SUser user) {
        return webRef;
    }

}
