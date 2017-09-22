package org.opensingular.server.p.commons.admin.healthsystem;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;
import org.jetbrains.annotations.NotNull;
import org.opensingular.lib.commons.extension.SingularExtensionUtil;
import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.lib.wicket.util.menu.MetronicMenu;
import org.opensingular.lib.wicket.util.menu.MetronicMenuGroup;
import org.opensingular.lib.wicket.util.menu.MetronicMenuItem;
import org.opensingular.lib.wicket.util.resource.DefaultIcons;
import org.opensingular.server.commons.wicket.view.template.ServerTemplate;
import org.opensingular.server.p.commons.admin.healthsystem.extension.AdministrationEntryExtension;

import java.util.List;


public class HealthSystemPage extends ServerTemplate implements Loggable {

    public static final String HEALTH_SYSTEM_MOUNT_PATH = "/health/${entryKey}";

    private List<AdministrationEntryExtension> adminEntries;
    private Form<Void>                         form;
    private WebMarkupContainer                 content;

    public HealthSystemPage() {
        this(new PageParameters());
    }

    public HealthSystemPage(PageParameters parameters) {
        super(parameters);
        loadExtensions();
        addForm();
        addContent(parameters.get("entryKey"));
    }

    private void loadExtensions() {
        adminEntries = SingularExtensionUtil.get().findExtensionsByClass(AdministrationEntryExtension.class);
    }

    private void addForm() {
        form = new Form<>("form");
        add(form);
    }

    private void addContent(StringValue entryKey) {
        String id = "content";
        if (entryKey == null || entryKey.isEmpty()) {
            this.content = new WebMarkupContainer(id);
        }
        else {
            adminEntries.stream().filter(i -> entryKey.toString().equals(i.getKey())).findFirst().ifPresent(i -> {
                this.content = i.makePanel(id);
            });
        }
        form.add(this.content);
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
    protected @NotNull WebMarkupContainer buildPageMenu(String id) {
        MetronicMenu      metronicMenu = new MetronicMenu(id);
        MetronicMenuGroup group        = new MetronicMenuGroup(DefaultIcons.USER, "Administration");
        group.setOpen();
        metronicMenu.addItem(group);
        adminEntries.forEach(entry -> {
            group.addItem(new MetronicMenuItem(null, entry.name(), HealthSystemPage.class, new PageParameters().add("entryKey", entry.getKey())));
        });
        return metronicMenu;
    }

}