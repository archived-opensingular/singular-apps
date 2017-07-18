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
package org.opensingular.server.core.wicket.box;


import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.opensingular.flow.persistence.entity.Actor;
import org.opensingular.flow.persistence.entity.ModuleEntity;
import org.opensingular.lib.wicket.util.modal.BSModalBorder;
import org.opensingular.server.commons.service.dto.BoxItemAction;
import org.opensingular.server.core.service.BoxService;
import org.opensingular.server.core.wicket.model.BoxItemDataMap;

import javax.inject.Inject;
import java.util.List;

import static org.opensingular.lib.wicket.util.util.WicketUtils.$m;

public abstract class BoxContentAllocateModal extends BoxContentConfirmModal<BoxItemDataMap> {
    private final IModel<ModuleEntity> moduleEntity;

    @Inject
    private BoxService boxService;

    protected DropDownChoice<Actor> usersDropDownChoice;

    public BoxContentAllocateModal(BoxItemAction itemAction, IModel<BoxItemDataMap> dataModel,
                                   IModel<ModuleEntity> moduleEntity) {
        super(itemAction, dataModel);
        this.moduleEntity = moduleEntity;
    }

    protected abstract void onDeallocate(AjaxRequestTarget target);

    @Override
    protected void onInitialize() {
        super.onInitialize();
        addUsersDropDownChoice();
        addDeallocate();
        confirmButton.add(new Behavior() {
            @Override
            public void onConfigure(Component component) {
                super.onConfigure(component);
                component.setEnabled(usersDropDownChoice.getModelObject() != null);
            }
        });
    }

    private void addUsersDropDownChoice() {
        IModel<List<Actor>> actorsModel = $m.get(() -> boxService.buscarUsuarios(moduleEntity.getObject(),
                dataModel, itemAction.getConfirmation()));
        usersDropDownChoice = new DropDownChoice<>("usersDropDownChoice",
                new Model<>(),
                actorsModel,
                new ChoiceRenderer<>("nome", "codUsuario"));
        usersDropDownChoice.add(new AjaxFormComponentUpdatingBehavior("change") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                target.add(confirmButton);
            }
        });
        border.add(usersDropDownChoice);
    }

    private void addDeallocate() {
        boolean visibleDesalocarButton = dataModel.getObject().get("codUsuarioAlocado") != null;
        AjaxButton deallocateButton = new AjaxButton("deallocate-btn", confirmationForm) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                onDeallocate(target);
                border.hide(target);
            }
        };
        deallocateButton.setDefaultFormProcessing(false)
                .setVisible(visibleDesalocarButton)
                .setRenderBodyOnly(!visibleDesalocarButton);
        border.addButton(BSModalBorder.ButtonStyle.CANCEL, $m.ofValue("Desalocar"), deallocateButton);
    }
}