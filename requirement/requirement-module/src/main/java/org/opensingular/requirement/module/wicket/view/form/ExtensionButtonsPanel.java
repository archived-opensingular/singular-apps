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

package org.opensingular.requirement.module.wicket.view.form;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.opensingular.form.SInstance;
import org.opensingular.form.wicket.component.SingularSaveButton;
import org.opensingular.lib.commons.extension.SingularExtensionUtil;
import org.opensingular.requirement.module.service.RequirementInstance;
import org.opensingular.requirement.module.wicket.view.extension.RequirementButtonExtension;

import static org.opensingular.lib.wicket.util.util.WicketUtils.$b;


public class ExtensionButtonsPanel<RI extends RequirementInstance> extends Panel {
    private IModel<RI>                       petInstanceModel;
    private IModel<? extends SInstance>      formModel;
    private List<RequirementButtonExtension> extensions;

    public ExtensionButtonsPanel(String id, IModel<RI> petInstanceModel, IModel<? extends SInstance> formModel) {
        super(id);
        this.petInstanceModel = petInstanceModel;
        this.formModel = formModel;
        this.extensions = lookupExtensions();
        addButtons();
    }

    private List<RequirementButtonExtension> lookupExtensions() {
        return SingularExtensionUtil.get().findExtensions(RequirementButtonExtension.class);
    }

    private void addButtons() {
        add(new ListView<RequirementButtonExtension>("buttons", extensions) {
            @Override
            protected void populateItem(ListItem<RequirementButtonExtension> item) {
                final RequirementButtonExtension itemModel = item.getModelObject();
                SingularSaveButton button = new SingularSaveButton("button", formModel, itemModel.shouldValidateForm()) {
                    @Override
                    protected void onValidationSuccess(AjaxRequestTarget target, Form<?> form, IModel<? extends SInstance> instanceModel) {
                        itemModel.onAction(new RequirementButtonExtension
                                .ButtonExtensionActionContext(target, form, petInstanceModel.getObject(), instanceModel.getObject()));
                    }
                };
                RequirementButtonExtension.ButtonView buttonView = itemModel.getButtonView();
                WebMarkupContainer                    icon       = new WebMarkupContainer("icon");
                if (buttonView.getIcon() != null) {
                    icon.add($b.classAppender(buttonView.getIcon().getCssClass()));
                }
                else {
                    icon.setVisible(false);
                }
                button.add(icon);
                button.add(new Label("label", buttonView.getLabel()).setRenderBodyOnly(true));
                item.add(button);
            }
        }.setRenderBodyOnly(true));
    }
}