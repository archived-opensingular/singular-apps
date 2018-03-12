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

package org.opensingular.server.commons.admin.healthsystem.panel;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ResourceLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.ResourceStreamResource;
import org.apache.wicket.util.file.File;
import org.apache.wicket.util.resource.FileResourceStream;
import org.apache.wicket.util.resource.IResourceStream;
import org.opensingular.lib.commons.util.Loggable;

import javax.annotation.Nonnull;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class LogPanel extends Panel implements Loggable {

    public LogPanel(String id) {
        super(id);
        addLogListView();
    }

    private void addLogListView() {
        add(new ListView<URI>("logs", resolveLogsURIs()) {
            @Override
            protected void populateItem(ListItem<URI> item) {
                ResourceStreamResource lopZipStream = makeZipLogStream(item.getModel());
                lopZipStream.setFileName("log.zip");
                ResourceLink downloadLink = new ResourceLink("log", lopZipStream);
                downloadLink.add(new Label("label", Paths.get(item.getModelObject()).getFileName().toString()));
                item.add(downloadLink);
            }
        });
    }

    @Nonnull
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

    @Nonnull
    private java.io.File makeZip(IModel<URI> logUriModel) throws IOException {
        byte[]       buffer  = new byte[1024];
        Path         logPath = Paths.get(logUriModel.getObject());
        java.io.File zip     = File.createTempFile("log", ".zip");
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

    @Nonnull
    private ArrayList<URI> resolveLogsURIs() {
        final ArrayList<URI> uris = new ArrayList<>();
        try {
            Path                  logDir   = resolveLogDirPath();
            DirectoryStream<Path> children = Files.newDirectoryStream(logDir);
            children.forEach(path -> uris.add(path.toUri()));
            children.close();
        } catch (IOException ex) {
            getLogger().error(ex.getMessage(), ex);
        }
        uris.sort(new Comparator<URI>() {
            @Override
            public int compare(URI o1, URI o2) {
                return ((Comparator<String>) Comparator.naturalOrder()).compare(o1.toString(), o2.toString());
            }
        });
        return uris;
    }

    private Path resolveLogDirPath() {
        Path jbossHomeDir    = Paths.get(System.getProperty("jboss.home.dir"));
        Path singularHomeDir = jbossHomeDir.getParent();
        return singularHomeDir.resolve("logs");
    }

}
