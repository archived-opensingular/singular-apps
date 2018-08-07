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

package org.opensingular.requirement.module.wicket.view.util;

import java.util.HashMap;
import java.util.Map;

import org.opensingular.requirement.module.wicket.buttons.DiffLink;
import org.opensingular.requirement.module.wicket.buttons.ViewVersionLink;

import static org.opensingular.lib.wicket.util.util.WicketUtils.$m;

public class ModuleButtonFactory {

    private ActionContext context = new ActionContext();

    public ModuleButtonFactory(ActionContext context, Map<String, String> param) {
        this.context = new ActionContext(context);
        for (Map.Entry<String, String> s : param.entrySet()) {
            this.context.setParam(s.getKey(), s.getValue());
        }
    }

    public ModuleButtonFactory(ActionContext context) {
        this(context, new HashMap<>());
    }

    public DiffLink getDiffButton(String id) {
        this.context.setDiffEnabled(true);
        return new DiffLink(id, $m.ofValue("Visualizar Diferenças"), context);
    }

    public ViewVersionLink getViewVersionButton(String id, Long formVersionId) {
        this.context.setFormVersionId(formVersionId);
        return new ViewVersionLink(id, $m.ofValue("Versão anterior do formulário"), context);
    }

    private String getDiffViewURL() {
        return null;
    }

    private String getDraftURL() {
        return null;
    }


}
