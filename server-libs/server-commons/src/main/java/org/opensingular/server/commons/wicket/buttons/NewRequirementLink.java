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

package org.opensingular.server.commons.wicket.buttons;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.opensingular.lib.wicket.util.metronic.menu.DropdownMenu;
import org.opensingular.server.commons.wicket.view.util.ActionContext;

import java.util.List;
import java.util.Optional;

import static org.opensingular.lib.wicket.util.util.WicketUtils.$b;

public class NewRequirementLink extends WebMarkupContainer {

    private List<Object> requirements;
    private IModel<?> labelModel;

    public NewRequirementLink(String id, ActionContext actionContext, List<Object> requirements) {
        super(id);
        labelModel =   new StringResourceModel("label.button.insert", this, null);
        this.requirements = requirements;
    }

    public NewRequirementLink(String id, IModel<?> labelModel, ActionContext actionContext, List<Object> requirements) {
        super(id);
        this.labelModel = labelModel;
        this.requirements = requirements;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        Integer numberOfRequirements = Optional.ofNullable(requirements).map(List::size).orElse(0);
        addSingleButton(numberOfRequirements == 1);
        addDropdownButton(numberOfRequirements > 1);
    }

    protected void addSingleButton(boolean visible) {
        Link botoes = new Link("_botao") {
            @Override
            protected void onConfigure() {
                super.onConfigure();
                this.add($b.attr("target", "_blank"));
                this.setVisible(visible);
            }

            @Override
            public void onClick() {
            }
        };
    }

    protected void addDropdownButton(boolean visible) {
        DropdownMenu dropdownMenu = new DropdownMenu("_novos");
        dropdownMenu.setVisible(visible);
    }

    protected String getURL(Object requirement) {
//        DispatcherPageUtil.buildFullURL(context);
//
//        String url = DispatcherPageUtil
//                .baseURL(getBaseUrl())
//                .formAction(FormActions.FORM_FILL.getId())
//                .petitionId(null)
//                .param(DispatcherPageParameters.FORM_NAME, form.getName())
//                .params(getLinkParams())
//                .build();
//        return url;
        return "";
    }

    @Override
    protected boolean getStatelessHint() {
        return true;
    }

}
