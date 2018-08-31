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

package org.opensingular.server.commons.wicket.view.form;


import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.opensingular.form.SInstance;
import org.opensingular.form.persistence.entity.FormVersionEntity;
import org.opensingular.form.wicket.enums.AnnotationMode;
import org.opensingular.form.wicket.enums.ViewMode;
import org.opensingular.form.wicket.panel.SingularFormPanel;
import org.opensingular.server.commons.persistence.entity.form.RequirementContentHistoryEntity;
import org.opensingular.server.commons.service.FormRequirementService;
import org.opensingular.server.commons.service.RequirementService;
import org.opensingular.server.commons.wicket.view.template.ServerTemplate;

import javax.inject.Inject;

public class ReadOnlyFormPage extends ServerTemplate {
    @Inject
    private FormRequirementService formRequirementService;

    @Inject
    private RequirementService<?, ?> requirementService;

    protected final IModel<Long> formVersionEntityPK;
    protected final IModel<Boolean> showAnnotations;

    public ReadOnlyFormPage(IModel<Long> formVersionEntityPK, IModel<Boolean> showAnnotations) {
        this.formVersionEntityPK = formVersionEntityPK;
        this.showAnnotations = showAnnotations;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
          SingularFormPanel singularFormPanel = new SingularFormPanel("singularFormPanel");
        singularFormPanel.setInstanceCreator(() -> {
            FormVersionEntity formVersionEntity = formRequirementService.loadFormVersionEntity(formVersionEntityPK.getObject());
            SInstance ins = formRequirementService.getSInstance(formVersionEntity);
            if (ins instanceof ExecutedTransitionAware) {
                setExecutedTransition((ExecutedTransitionAware) ins);
            }
            return ins;
        });
        singularFormPanel.setViewMode(ViewMode.READ_ONLY);
        singularFormPanel.setAnnotationMode(showAnnotations.getObject() ? AnnotationMode.READ_ONLY : AnnotationMode.NONE);
        add(new Form("form").add(singularFormPanel));
    }

    private void setExecutedTransition(ExecutedTransitionAware ins) {
        RequirementContentHistoryEntity rch = requirementService
                .findRequirementContentHistoryByFormVersionCod(formVersionEntityPK.getObject());
        String executedTransition;
        if (rch != null) {
            executedTransition = rch.getTaskInstanceEntity().getExecutedTransition().getName();
        } else {
            executedTransition = null;
        }
        ins.setExecutedTransition(executedTransition);
    }

    @Override
    protected IModel<String> getContentTitle() {
        return new Model<>();
    }

    @Override
    protected IModel<String> getContentSubtitle() {
        return new Model<>();
    }

    @Override
    protected boolean isWithMenu() {
        return false;
    }
}