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

package org.opensingular.requirement.module.admin.healthsystem;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;
import org.opensingular.lib.commons.extension.SingularExtensionUtil;
import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.lib.wicket.util.menu.MetronicMenu;
import org.opensingular.lib.wicket.util.menu.MetronicMenuGroup;
import org.opensingular.lib.wicket.util.menu.MetronicMenuItem;
import org.opensingular.lib.wicket.util.resource.DefaultIcons;
import org.opensingular.requirement.module.admin.healthsystem.extension.AdministrationEntryExtension;
import org.opensingular.requirement.module.wicket.view.template.ServerTemplate;

import javax.annotation.Nonnull;
import java.util.List;


public class HealthSystemPage extends ServerTemplate implements Loggable {

    public static final String ENTRY_PATH_PARAM         = "entry";
    public static final String HEALTH_SYSTEM_MOUNT_PATH = "/health/${" + ENTRY_PATH_PARAM + "}";

    private List<AdministrationEntryExtension> adminEntries;
    private Form<Void>                         form;

    public HealthSystemPage() {
        this(new PageParameters());
    }

    public HealthSystemPage(PageParameters parameters) {
        super(parameters);
        loadExtensions();
        addForm();
        addContent(parameters.get(ENTRY_PATH_PARAM));
    }

    private void loadExtensions() {
        adminEntries = SingularExtensionUtil.get().findExtensions(AdministrationEntryExtension.class);
    }

    private void addForm() {
        form = new Form<>("form");
        form.setMultiPart(true);
        add(form);
    }

    private void addContent(StringValue entryKey) {
        String id = "content";
        if (entryKey == null || entryKey.isEmpty()) {
            form.add(new WebMarkupContainer(id));
        }
        else {
            adminEntries.stream().filter(i -> entryKey.toString().equals(i.getKey())).findFirst().ifPresent(i -> {
                form.add(i.makePanel(id));
            });
        }
    }

    @Override
    protected IModel<String> getContentTitle() {
        return null;
    }

    @Override
    protected IModel<String> getContentSubtitle() {
        return null;
    }

    @Override
    protected boolean isWithMenu() {
        return true;
    }

    @Override
    protected @Nonnull
    WebMarkupContainer buildPageMenu(String id) {
        MetronicMenu      metronicMenu = new MetronicMenu(id);
        MetronicMenuGroup group        = new MetronicMenuGroup(DefaultIcons.USER, "Administration");
        group.setOpen();
        metronicMenu.addItem(group);
        adminEntries.forEach(entry -> {
            group.addItem(new MetronicMenuItem(null, entry.name(), HealthSystemPage.class, new PageParameters().add(ENTRY_PATH_PARAM, entry.getKey())));
        });
        return metronicMenu;
    }

}