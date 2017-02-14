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

package org.opensingular.server.module.admin.healthPanel;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.opensingular.server.commons.wicket.view.template.Content;
import org.opensingular.server.module.admin.healthPanel.panel.CachePanel;
import org.opensingular.server.module.admin.healthPanel.panel.DbPanel;
import org.opensingular.server.module.admin.healthPanel.panel.WebPanel;
import org.opensingular.server.module.admin.healthPanel.panel.JobPanel;

@SuppressWarnings("serial")
public class PainelSaudeContent extends Content {
	
    private static final String CONTAINER_ALL_CONTENT = "containerAllContent";

	public PainelSaudeContent(String id) {
		super(id);
		buildContent();
	}
	
	private void buildContent(){
		Form<Void> form = new Form<>("form");
		add(form);
		
		form.add(new WebMarkupContainer(CONTAINER_ALL_CONTENT));
		
		AjaxButton buttonDb = new AjaxButton("buttonDb") {
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				form.replace(new DbPanel(CONTAINER_ALL_CONTENT));
				target.add(form);
			}
		};
		
		AjaxButton buttonWeb = new AjaxButton("buttonWeb") {
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				form.replace(new WebPanel(CONTAINER_ALL_CONTENT));
				target.add(form);
			}
		};
		
		AjaxButton buttonCache = new AjaxButton("buttonCache") {
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				form.replace(new CachePanel(CONTAINER_ALL_CONTENT));
				target.add(form);
			}
		};
		
		AjaxButton buttonWs = new AjaxButton("buttonJobs") {
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				form.replace(new JobPanel(CONTAINER_ALL_CONTENT));
				target.add(form);
			}
		};
		
		form.add(buttonDb);
		form.add(buttonWeb);
		form.add(buttonWs);
		form.add(buttonCache);
	}

	@Override
	protected IModel<?> getContentTitleModel() {
		return new Model<>("Painel Saude");
	}

	@Override
	protected IModel<?> getContentSubtitleModel() {
		return new Model<>("");
	}
	
}
