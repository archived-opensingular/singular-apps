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

package org.opensingular.server.commons.wicket.buttons;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.opensingular.server.commons.form.FormAction;
import org.opensingular.server.commons.wicket.view.util.ActionContext;
import org.opensingular.server.commons.wicket.view.util.DispatcherPageUtil;

import static org.opensingular.lib.wicket.util.util.WicketUtils.$b;

import java.util.Optional;

public class ViewVersionLink extends Panel {

    private ActionContext context;

    public ViewVersionLink(String id, IModel<String> labelModel, ActionContext context) {
        super(id);
        this.context = new ActionContext(context);
        this.context.setDiffEnabled(false);
        this.context.setFormAction(FormAction.FORM_ANALYSIS_VIEW);
        Link<String> link = new Link<String>("oldVersionLink") {
            @Override
            protected void onConfigure() {
                super.onConfigure();
                
                Optional<Long> formVersionId = ViewVersionLink.this.context.getFormVersionId();
                if (formVersionId.isPresent()) {
                    this.add($b.attr("target", String.format("version%s", formVersionId.get())));
                    this.add($b.attr("href", DispatcherPageUtil.buildFullURL(ViewVersionLink.this.context)));
                    this.setBody(labelModel);
                }
            }

            @Override
            public void onClick() {
            }
        };
        this.add(link);
    }
}
