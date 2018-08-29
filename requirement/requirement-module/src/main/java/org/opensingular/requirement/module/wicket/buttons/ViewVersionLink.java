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
import org.apache.wicket.model.Model;
import org.opensingular.link.NewTabPageLink;
import org.opensingular.requirement.module.form.FormAction;
import org.opensingular.requirement.module.wicket.view.form.ReadOnlyFormPage;
import org.opensingular.requirement.module.wicket.view.util.ActionContext;

public class ViewVersionLink extends Panel {
    public ViewVersionLink(String id, IModel<String> labelModel, ActionContext context) {
        super(id);
        ActionContext newContext = new ActionContext(context);
        newContext.setFormAction(FormAction.FORM_ANALYSIS_VIEW);
        Long formVersionCod = newContext.getFormVersionId().orElse(null);
        if (formVersionCod != null) {
            NewTabPageLink link = new NewTabPageLink("oldVersionLink", () ->
                    new ReadOnlyFormPage(new Model<>(formVersionCod), new Model<>(Boolean.TRUE), true));
            link.setTarget(String.format("version%s", formVersionCod));
            link.setBody(labelModel);
            this.add(link);
        }
    }
}