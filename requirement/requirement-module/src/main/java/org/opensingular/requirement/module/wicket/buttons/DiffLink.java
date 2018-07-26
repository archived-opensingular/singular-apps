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

package org.opensingular.requirement.module.wicket.buttons;

import java.util.Optional;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.opensingular.requirement.module.wicket.view.util.ActionContext;
import org.opensingular.requirement.module.wicket.view.util.DispatcherPageUtil;

import static org.opensingular.lib.wicket.util.util.WicketUtils.$b;
import static org.opensingular.requirement.module.wicket.view.form.DiffFormPage.CURRENT_FORM_VERSION_ID;
import static org.opensingular.requirement.module.wicket.view.form.DiffFormPage.CURRENT_REQUIREMENT_ID;
import static org.opensingular.requirement.module.wicket.view.form.DiffFormPage.PREVIOUS_FORM_VERSION_ID;
import static org.opensingular.requirement.module.wicket.view.form.DiffFormPage.PREVIOUS_REQUIREMENT_ID;

public class DiffLink extends Panel {

    public static class FormVersionsToDiff {
        private Long current;
        private Long previous;

        public FormVersionsToDiff(Long current, Long previous) {
            this.current = current;
            this.previous = previous;
        }
    }

    public static class RequirementVersionsToDiff {
        private Long current;
        private Long previous;

        public RequirementVersionsToDiff(Long current, Long previous) {
            this.current = current;
            this.previous = previous;
        }
    }


    public DiffLink(String id, IModel<String> labelModel, ActionContext context, RequirementVersionsToDiff requirementVersionsToDiff) {
        super(id);
        ActionContext actionContext = new ActionContext(context)
                .setParam(CURRENT_REQUIREMENT_ID, String.valueOf(requirementVersionsToDiff.current));

        if (requirementVersionsToDiff.previous != null) {
            actionContext.setParam(PREVIOUS_REQUIREMENT_ID, String.valueOf(requirementVersionsToDiff.previous));
        }

        init(labelModel, actionContext);
    }

    public DiffLink(String id, IModel<String> labelModel, ActionContext context, FormVersionsToDiff formVersionsToDiff) {
        super(id);
        ActionContext actionContext = new ActionContext(context)
                .setParam(CURRENT_FORM_VERSION_ID, String.valueOf(formVersionsToDiff.current));

        if (formVersionsToDiff.previous != null) {
            actionContext.setParam(PREVIOUS_FORM_VERSION_ID, String.valueOf(formVersionsToDiff.previous));
        }

        init(labelModel, actionContext);
    }

    public DiffLink(String id, IModel<String> labelModel, ActionContext context) {
        super(id);
        init(labelModel, context);
    }

    private void init(IModel<String> labelModel, ActionContext context) {
        Link<String> link = new Link<String>("diffLink") {
            @Override
            protected void onConfigure() {
                super.onConfigure();
                Optional<Long> requirementId = context.getRequirementId();
                if (requirementId.isPresent()) {
                    this.add($b.attr("target", String.format("diff%s", requirementId.get())));
                    this.add($b.attr("href", DispatcherPageUtil.buildFullURL(context)));
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
