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

package org.opensingular.requirement.module.wicket.buttons;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.opensingular.link.NewTabPageLink;
import org.opensingular.requirement.module.wicket.view.form.DiffFormPage;
import org.opensingular.requirement.module.wicket.view.util.ActionContext;

public class DiffLink extends Panel {
    public DiffLink(String id, IModel<String> labelModel, ActionContext context) {
        super(id);
        init(labelModel, context);
    }

    private void init(IModel<String> labelModel, ActionContext context) {
        NewTabPageLink link = new NewTabPageLink("diffLink", () -> new DiffFormPage(context));
        context.getRequirementId().ifPresent(requirementCod -> link.setTarget(String.format("diff%s", requirementCod)));
        link.setBody(labelModel);
        this.add(link);
    }
}