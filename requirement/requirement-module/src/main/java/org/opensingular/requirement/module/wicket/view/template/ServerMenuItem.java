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

package org.opensingular.requirement.module.wicket.view.template;

import org.apache.wicket.request.IRequestParameters;
import org.apache.wicket.request.component.IRequestablePage;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.opensingular.lib.commons.ui.Icon;
import org.opensingular.lib.wicket.util.menu.MetronicMenuItem;

import java.util.Set;

public class ServerMenuItem extends MetronicMenuItem {
    private PageParameters menuParams;

    public ServerMenuItem(Icon icon, String title, Class<? extends IRequestablePage> responsePageClass, IRequestablePage page, PageParameters parameters) {
        super(icon, title, responsePageClass, page, parameters);
        this.menuParams = parameters;
    }

    @Override
    protected boolean isActive() {
        IRequestParameters requestParams = RequestCycle.get().getRequest().getRequestParameters();
        Set<String> namedKeys = menuParams.getNamedKeys();
        boolean isSame = true;
        for (String namedKey : namedKeys) {
            String menuParamValue = menuParams.get(namedKey).toOptionalString();
            String reqParamValue = requestParams.getParameterValue(namedKey).toOptionalString();
            isSame &= menuParamValue != null && menuParamValue.equals(reqParamValue);
        }
        return isSame;
    }

}