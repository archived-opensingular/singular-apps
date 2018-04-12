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

package org.opensingular.requirement.commons.admin.healthsystem.panel;

import de.alpharogroup.wicket.js.addon.toastr.ToastrType;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.opensingular.form.service.FormIndexService;
import org.opensingular.requirement.commons.wicket.view.SingularToastrHelper;

import javax.inject.Inject;

@SuppressWarnings("serial")
public class IndexPanel extends Panel {

	@Inject
    private FormIndexService formIndexService;

	public IndexPanel(String id) {
		super(id);
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();
		
		add(new AjaxButton("fullUnIndex") {
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				formIndexService.resetIndexedFlag();
				target.add(form);
				
				new SingularToastrHelper(this).
					addToastrMessage(ToastrType.SUCCESS, "All forms were un-indexed!");
			}
		});

		add(new AjaxButton("fullIndex") {
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				formIndexService.indexAllForms();
				target.add(form);

				new SingularToastrHelper(this).
						addToastrMessage(ToastrType.SUCCESS, "All forms were indexed!");
			}
		});
	}

}
