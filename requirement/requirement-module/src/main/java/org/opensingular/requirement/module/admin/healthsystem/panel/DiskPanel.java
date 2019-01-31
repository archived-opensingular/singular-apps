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

package org.opensingular.requirement.module.admin.healthsystem.panel;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RefreshingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.util.lang.Bytes;
import org.opensingular.lib.commons.util.Loggable;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import static org.opensingular.lib.wicket.util.util.Shortcuts.$m;

@SuppressWarnings("serial")
public class DiskPanel extends Panel implements Loggable {

    private ListModel<String> disk = new ListModel<>(new ArrayList<>(0));

    public DiskPanel(String id) {
        super(id);
    }

    @Override
    protected void onConfigure() {
        super.onConfigure();
        disk.getObject().clear();
        FileSystemView fsv = FileSystemView.getFileSystemView();
        File[]         f   = File.listRoots();
        for (File file : f) {
            disk.getObject().add("<b>Drive: " + file + "</b>");
            disk.getObject().add("&nbsp;&nbsp;&nbsp;&nbsp;Display name: " + fsv.getSystemDisplayName(file));
            ;
            disk.getObject().add("&nbsp;&nbsp;&nbsp;&nbsp;Total space: " + Bytes.bytes(file.getTotalSpace()));
            disk.getObject().add("&nbsp;&nbsp;&nbsp;&nbsp;Usable space: " + Bytes.bytes(file.getUsableSpace()));
        }
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        add(new RefreshingView<String>("disk") {
            @Override
            protected Iterator<IModel<String>> getItemModels() {
                return disk.getObject().stream().map(this::toModel).iterator();
            }

            private IModel<String> toModel(String s) {
                return $m.ofValue(s);
            }

            @Override
            protected void populateItem(Item<String> item) {
                Label label = new Label("stat", item.getModel());
                label.setEscapeModelStrings(false);
                item.add(label);
            }
        });

    }
}
