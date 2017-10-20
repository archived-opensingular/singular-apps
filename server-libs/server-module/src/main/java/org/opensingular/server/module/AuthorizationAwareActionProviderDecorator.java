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

package org.opensingular.server.module;

import org.opensingular.lib.support.spring.util.ApplicationContextProvider;
import org.opensingular.server.commons.box.BoxItemData;
import org.opensingular.server.commons.box.action.BoxItemActionList;
import org.opensingular.server.commons.persistence.filter.QuickFilter;
import org.opensingular.server.commons.spring.security.AuthorizationService;

public class AuthorizationAwareActionProviderDecorator implements ActionProvider {

    private ActionProvider delegate;

    public AuthorizationAwareActionProviderDecorator(ActionProvider delegate) {
        this.delegate = delegate;
    }

    @Override
    public BoxItemActionList getLineActions(BoxInfo boxInfo, BoxItemData line, QuickFilter filter) {
        BoxItemActionList actionList = delegate.getLineActions(boxInfo, line, filter);
        filterAllowedActions(actionList, line, filter);
        return actionList;
    }

    private void filterAllowedActions(BoxItemActionList actions, BoxItemData line, QuickFilter filter) {
        ApplicationContextProvider.get().getBean(AuthorizationService.class).filterActions((String) line.getType(), (Long) line.getPetitionId(), actions, filter.getIdUsuarioLogado());
    }
}
