package org.opensingular.server.p.commons.admin.healthsystem;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.ResourceLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.resource.ResourceStreamResource;
import org.apache.wicket.util.file.File;
import org.apache.wicket.util.resource.FileResourceStream;
import org.apache.wicket.util.resource.IResourceStream;
import org.jetbrains.annotations.NotNull;
import org.opensingular.form.service.FormIndexService;
import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.server.commons.wicket.view.template.ServerTemplate;
import org.opensingular.server.p.commons.admin.healthsystem.panel.*;

import javax.inject.Inject;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


public class HealthSystemPage extends ServerTemplate implements Loggable {

    public static final String HEALTH_SYSTEM_MOUNT_PATH = "/healthsystem";

    private static final String CONTAINER_ALL_CONTENT = "containerAllContent";

    @Inject
    private FormIndexService formIndexService;

    public HealthSystemPage() {
        buildContent();
    }

    @Override
    protected IModel<String> getContentTitle() {
        return new Model<>("Health System");
    }

    @Override
    protected IModel<String> getContentSubtitle() {
        return new Model<>();
    }

    @Override
    protected boolean isWithMenu() {
        return false;
    }

    private void buildContent() {
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

        AjaxButton buttonCache = new AjaxButton("buttonCache") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                form.replace(new CachePanel(CONTAINER_ALL_CONTENT));
                target.add(form);
            }
        };

        AjaxButton buttonJobs = new AjaxButton("buttonJobs") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                form.replace(new JobPanel(CONTAINER_ALL_CONTENT));
                target.add(form);
            }
        };

        AjaxButton buttonPermissions = new AjaxButton("buttonPermissions") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                form.replace(new PermissionPanel(CONTAINER_ALL_CONTENT));
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

        AjaxButton buttonIndexForms = new AjaxButton("buttonIndexForms") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                form.replace(new IndexPanel(CONTAINER_ALL_CONTENT));
                target.add(form);
            }
        };


        form.add(buttonCache);
        form.add(buttonDb);
        form.add(buttonPermissions);
        form.add(buttonJobs);
        form.add(buttonWeb);
        form.add(buttonIndexForms);
        form.add(makeLogButton());

    }

    @NotNull
    private AjaxButton makeLogButton() {
        return new AjaxButton("buttonLog") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                try {
                    target.add(form.addOrReplace(makeLogFragment()));
                } catch (IOException ex) {
                    getLogger().error(ex.getMessage(), ex);
                }
            }
        };
    }

    @NotNull
    private Fragment makeLogFragment() throws IOException {
        Fragment fragment = new Fragment(CONTAINER_ALL_CONTENT, "logContainer", HealthSystemPage.this);
        fragment.add(makeLogListView());
        return fragment;
    }

    @NotNull
    private ListView<URI> makeLogListView() throws IOException {
        return new ListView<URI>("logs", resolveLogsURIs()) {
            @Override
            protected void populateItem(ListItem<URI> item) {
                ResourceStreamResource lopZipStream = makeZipLogStream(item.getModel());
                lopZipStream.setFileName("log.zip");
                ResourceLink downloadLink = new ResourceLink("log", lopZipStream);
                downloadLink.add(new Label("label", Paths.get(item.getModelObject()).getFileName().toString()));
                item.add(downloadLink);
            }
        };
    }

    @NotNull
    private ResourceStreamResource makeZipLogStream(IModel<URI> logPath) {
        return new ResourceStreamResource() {
            @Override
            protected IResourceStream getResourceStream() {
                try {
                    return new FileResourceStream(new File(makeZip(logPath)));
                } catch (IOException ex) {
                    getLogger().error(ex.getMessage(), ex);
                }
                return null;
            }
        };
    }

    @NotNull
    private java.io.File makeZip(IModel<URI> logUriModel) throws IOException {
        byte[] buffer = new byte[1024];
        Path logPath = Paths.get(logUriModel.getObject());
        java.io.File zip = File.createTempFile("log", ".zip");
        try (FileOutputStream fos = new FileOutputStream(zip)) {
            makeZip(buffer, logPath, fos);
        }
        return zip;
    }

    private void makeZip(byte[] buffer, Path logPath, FileOutputStream fos) throws IOException {
        ZipEntry ze = new ZipEntry(logPath.getFileName().toString());
        try (ZipOutputStream zos = new ZipOutputStream(fos)) {
            zos.putNextEntry(ze);
            try (FileInputStream in = new FileInputStream(logPath.toFile())) {
                int len;
                while ((len = in.read(buffer)) > 0) {
                    zos.write(buffer, 0, len);
                }
                zos.closeEntry();
            }
        }
    }

    @NotNull
    private ArrayList<URI> resolveLogsURIs() throws IOException {
        ArrayList<URI> uris = new ArrayList<>();
        Path logDir = resolveLogDirPath();
        DirectoryStream<Path> children = Files.newDirectoryStream(logDir);
        children.forEach(path -> uris.add(path.toUri()));
        children.close();
        return uris;
    }

    private Path resolveLogDirPath() {
        Path jbossHomeDir = Paths.get(System.getProperty("jboss.home.dir"));
        Path singularHomeDir = jbossHomeDir.getParent();
        return singularHomeDir.resolve("logs");
    }

}