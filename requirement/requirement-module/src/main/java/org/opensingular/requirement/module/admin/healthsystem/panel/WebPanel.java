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
package org.opensingular.requirement.module.admin.healthsystem.panel;

import de.alpharogroup.wicket.js.addon.toastr.ToastrType;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.opensingular.form.SInstance;
import org.opensingular.form.wicket.component.SingularSaveButton;
import org.opensingular.form.wicket.component.SingularValidationButton;
import org.opensingular.form.wicket.panel.SingularFormPanel;
import org.opensingular.requirement.module.admin.healthsystem.stypes.SWebHealth;
import org.opensingular.requirement.module.wicket.view.SingularToastrHelper;

@SuppressWarnings("serial")
public class WebPanel extends Panel {

	public WebPanel(String id) {
		super(id);
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();
		SingularFormPanel panelWeb = new SingularFormPanel("panelWeb", SWebHealth.class);

		SingularValidationButton checkButton = new SingularValidationButton("checkButtonWeb", panelWeb.getInstanceModel()){
			@Override
			protected void onValidationSuccess(AjaxRequestTarget target, Form<?> form,
					IModel<? extends SInstance> instanceModel) {
				new SingularToastrHelper(this).
					addToastrMessage(ToastrType.SUCCESS, "All sites can be connected!");
			}
		};
		
		SingularSaveButton saveButton = new SingularSaveButton("saveButtonWeb", panelWeb.getInstanceModel()) {
			@Override
			protected void onValidationSuccess(AjaxRequestTarget target, Form<?> form,
					IModel<? extends SInstance> instanceModel) {
				// TODO
				new SingularToastrHelper(this).
					addToastrMessage(ToastrType.INFO, "Working in progress.");
			}
		};
		
		add(panelWeb);
		add(checkButton);
		add(saveButton);
	}
}
