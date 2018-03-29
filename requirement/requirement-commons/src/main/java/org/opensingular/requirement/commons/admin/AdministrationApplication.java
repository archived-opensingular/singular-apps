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

import org.apache.wicket.Page;
import org.opensingular.requirement.commons.admin.healthsystem.HealthSystemPage;
import org.opensingular.requirement.commons.wicket.SingularRequirementApplication;


    public class AdministrationApplication extends SingularRequirementApplication {


    @Override
    public void init() {
        super.init();
        mountPage(HealthSystemPage.HEALTH_SYSTEM_MOUNT_PATH, HealthSystemPage.class);
    }

    @Override
    public Class<? extends Page> getHomePage() {
        return HealthSystemPage.class;
    }

}