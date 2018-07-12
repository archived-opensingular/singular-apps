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
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RefreshingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.util.ListModel;
import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.requirement.module.admin.AdminFacade;
import org.opensingular.requirement.module.wicket.view.SingularToastrHelper;
import org.quartz.SchedulerException;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.opensingular.lib.wicket.util.util.Shortcuts.*;

@SuppressWarnings("serial")
public class JobPanel extends Panel implements Loggable {

    @Inject
    private AdminFacade adminFacade;

    private ListModel<String> runnedJobsModel = new ListModel<>(new ArrayList<>());

    public JobPanel(String id) {
        super(id);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        add(new Button("runAllJobs") {
            @Override
            public void onSubmit() {
                try {
                    List<String> runnedJobs = adminFacade.runAllJobs();
                    runnedJobsModel.setObject(runnedJobs);
                    new SingularToastrHelper(this).
                            addToastrMessage(ToastrType.SUCCESS, "All jobs runned!");
                } catch (SchedulerException e) {
                    getLogger().error(e.getMessage(), e);
                }
            }
        });

        RefreshingView<String> repeatingView = new RefreshingView<String>("runnedJobs") {
            @Override
            protected Iterator<IModel<String>> getItemModels() {
                return runnedJobsModel.getObject().stream().map(this::toModel).iterator();
            }

            private IModel<String> toModel(String s) {
                return $m.ofValue(s);
            }

            @Override
            protected void populateItem(Item<String> item) {
                item.add(new Label("job", item.getModel()));
            }
        };
        add(repeatingView);
    }
}
