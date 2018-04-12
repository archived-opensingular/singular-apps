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

package org.opensingular.requirement.commons.spring.security;

import org.opensingular.requirement.commons.config.IServerContext;
import org.opensingular.requirement.commons.util.url.UrlToolkitBuilder;
import org.opensingular.requirement.commons.wicket.SingularRequirementApplication;
import org.opensingular.requirement.commons.wicket.SingularSession;

import javax.servlet.ServletContext;
import java.io.Serializable;

public class SecurityAuthPathsFactory implements Serializable {

    public SecurityAuthPaths get() {

        SingularSession                singularSession                = SingularSession.get();
        SingularRequirementApplication singularRequirementApplication = SingularRequirementApplication.get();

        SingularUserDetails userDetails = singularSession.getUserDetails();
        IServerContext serverContext = userDetails.getServerContext();

        ServletContext servletContext = singularRequirementApplication.getServletContext();

        UrlToolkitBuilder urlToolkitBuilder = new UrlToolkitBuilder();

        return new SecurityAuthPaths(servletContext.getContextPath(), serverContext.getUrlPath(), urlToolkitBuilder);
    }

}
