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

package org.opensingular.server.commons.wicket.view.template;

import org.apache.wicket.request.component.IRequestablePage;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.opensingular.lib.commons.ui.Icon;
import org.opensingular.lib.wicket.util.menu.MetronicMenuItem;
import org.opensingular.server.commons.wicket.view.util.ActionContext;

public class ServerMenuItem extends MetronicMenuItem {

    public ServerMenuItem(Icon icon, String title, Class<? extends IRequestablePage> responsePageClass, IRequestablePage page, PageParameters parameters) {
        super(icon, title, responsePageClass, page, parameters);
    }

    @Override
    protected boolean isActive() {
        return RequestCycle.get()
                .getRequest()
                .getRequestParameters()
                .getParameterValue(ActionContext.ITEM_PARAM_NAME)
                .toString("")
                .equals(this.title);
    }

}