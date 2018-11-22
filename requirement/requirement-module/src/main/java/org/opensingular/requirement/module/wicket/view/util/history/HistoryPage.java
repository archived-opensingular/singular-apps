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

package org.opensingular.requirement.module.wicket.view.util.history;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.opensingular.requirement.module.wicket.view.template.ServerTemplate;
import org.opensingular.requirement.module.wicket.view.util.history.processview.ProcessViewPanel;
import org.wicketstuff.annotation.mount.MountPath;

import static org.opensingular.requirement.module.wicket.view.util.ActionContext.REQUIREMENT_ID;
import static org.opensingular.requirement.module.wicket.view.util.ActionContext.ROOT_REQUIREMENT_ID;


@MountPath("history")
public class HistoryPage extends ServerTemplate {

    private static final long serialVersionUID = -3344810189307767761L;

    public HistoryPage() {
    }

    public HistoryPage(PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        Long requirementPK     = getPage().getPageParameters().get(REQUIREMENT_ID).toOptionalLong();
        Long rootRequirementPK = getPage().getPageParameters().get(ROOT_REQUIREMENT_ID).toLong(requirementPK);
        add(new ProcessViewPanel("visaoProcesso", rootRequirementPK, requirementPK));
    }

    @Override
    protected IModel<String> getContentTitle() {
        return new ResourceModel("label.historico.title");
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
