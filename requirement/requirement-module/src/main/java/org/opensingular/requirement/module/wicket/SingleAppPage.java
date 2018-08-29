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

package org.opensingular.requirement.module.wicket;

import org.apache.wicket.RestartResponseException;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.opensingular.requirement.module.wicket.box.BoxPage;
import org.opensingular.requirement.module.wicket.view.util.dispatcher.DispatcherPage;

public class SingleAppPage extends WebPage {
    public SingleAppPage() {
        throw new RestartResponseException(BoxPage.class);
    }

    public SingleAppPage(PageParameters parameters) {
        super(parameters);
        if (parameters.get("dispatch").toBoolean(false)) {
            throw new RestartResponseException(DispatcherPage.class, parameters);
        } else {
            throw new RestartResponseException(BoxPage.class, parameters);
        }
    }
}