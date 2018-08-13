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

package org.opensingular.requirement.module.admin.healthsystem.panel;

import de.alpharogroup.wicket.js.addon.toastr.ToastrType;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.opensingular.flow.core.FlowInstance;
import org.opensingular.flow.core.STask;
import org.opensingular.flow.core.TaskInstance;
import org.opensingular.form.wicket.behavior.InputMaskBehavior;
import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.requirement.module.exception.SingularServerException;
import org.opensingular.requirement.module.service.RequirementInstance;
import org.opensingular.requirement.module.service.RequirementService;
import org.opensingular.requirement.module.wicket.view.SingularToastrHelper;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class is responsible for possible a FORCE transition of the requirements Task.
 * <p>
 * Note: The transition will show in the history, but the user responsible will be empty.
 * Example: Requeriment A - approved
 *          Force Transition to disapproved.
 *          Requeriment A - disapproved
 */
public class RequirementTransitionPanel extends Panel implements Loggable {


    private static final String MASK_99_MILLION = "99999999";

    @Inject
    private RequirementService<?, ?> requirementService;
    private FlowInstance instance;
    private IModel<String> requirementTask = new Model<>();

    public RequirementTransitionPanel(String id) {
        super(id);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        Form form = new Form("form");

        IModel<String> idRequirement = new Model<>();

        final WebMarkupContainer containerSelect = new WebMarkupContainer("containerSelect");
        containerSelect.setVisible(false);
        final Component requirementField = createRequirementField(form, idRequirement, containerSelect);
        final Component dropDownChoice = createDropDownChoice();
        containerSelect.add(dropDownChoice);
        form.add(containerSelect);
        form.add(requirementField);
        addSubmitButton(form);

        add(form);


    }

    private Component createRequirementField(Form form, IModel<String> idRequirement, Component dropDownChoice) {
        return new TextField<>("inputRequeriment", idRequirement)
                .setRequired(true)
                .add(new InputMaskBehavior(MASK_99_MILLION))
                .add(createInputEvent(form, idRequirement, dropDownChoice));
    }

    private AjaxEventBehavior createInputEvent(Form form, IModel<String> idRequirement, Component dropDownChoice) {
        return new AjaxFormComponentUpdatingBehavior("blur") {

            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                if (idRequirement.getObject() != null) {
                    try {
                        RequirementInstance requirement = requirementService.getRequirement(Long.valueOf(idRequirement.getObject()));
                        instance = requirement.getFlowInstance();
                        requirementTask.setObject(instance.getCurrentTask().map(TaskInstance::getName).orElse(null));
                        dropDownChoice.setVisible(true);
                        target.add(form);
                    } catch (SingularServerException e) {
                        getLogger().debug(e.getMessage(), e);
                        instance = null;
                        dropDownChoice.setVisible(false);
                        target.add(form);
                        showErrorToast("Não foi encontrada a petição de cod=" + idRequirement.getObject());
                    }

                }
            }

            @Override
            protected void onError(AjaxRequestTarget target, RuntimeException e) {
                super.onError(target, e);
                instance = null;
                dropDownChoice.setVisible(false);
                target.add(form);
            }
        };
    }

    private void addSubmitButton(Form form) {
        form.add(new AjaxButton("buttonSubmit") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                super.onSubmit(target, form);

                if (instance != null) {
                    instance.forceStateUpdate(getSTaskByName(requirementTask.getObject()));

                    new SingularToastrHelper(this).
                            addToastrMessage(ToastrType.SUCCESS, "Requerimento alterado com sucesso!");
                } else {
                    showErrorToast("A instância do requerimento não foi encontrada.");
                }

            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                super.onError(target, form);
                showErrorToast("É necessário preencher os campos obrigatórios.");
            }
        });
    }

    private Component createDropDownChoice() {
        return new DropDownChoice<String>("requirementTaskSelect",
                requirementTask,
                getChoicesDetachableModel()) {
            @Override
            protected String getNullValidDisplayValue() {
                return "Selecione";
            }

            @Override
            protected String getNullKeyDisplayValue() {
                return null;
            }

            @Override
            public boolean isNullValid() {
                return true;
            }

        }.setRequired(true);
    }

    private STask<?> getSTaskByName(String name) {
        return getsTaskStream()
                .filter(choice -> choice.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    private Stream<STask<?>> getsTaskStream() {
        return instance.getFlowDefinition()
                .getFlowMap()
                .getAllTasks()
                .parallelStream();
    }

    private LoadableDetachableModel<List<String>> getChoicesDetachableModel() {
        return new LoadableDetachableModel<List<String>>() {

            @Override
            protected List<String> load() {
                if (instance != null) {
                    return getsTaskStream()
                            .map(STask::getName)
                            .collect(Collectors.toList());
                }
                return new ArrayList<>();
            }
        };
    }

    private void showErrorToast(String msg) {
        new SingularToastrHelper(this).
                addToastrMessage(ToastrType.ERROR, msg);
    }
}
