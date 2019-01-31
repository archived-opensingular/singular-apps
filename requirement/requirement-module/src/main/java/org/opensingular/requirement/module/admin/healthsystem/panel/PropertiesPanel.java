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

import org.apache.wicket.markup.html.panel.Panel;
import org.opensingular.lib.commons.base.SingularProperties;
import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.lib.wicket.util.datatable.BSDataTable;
import org.opensingular.lib.wicket.util.datatable.BSDataTableBuilder;
import org.opensingular.lib.wicket.util.datatable.BaseDataProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.opensingular.lib.wicket.util.util.WicketUtils.$m;

public class PropertiesPanel extends Panel implements Loggable {


    private BSDataTable<String, String> listTable;

    public PropertiesPanel(String id) {
        super(id);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        listTable = setupDataTable();
        queue(listTable);
    }

    private BSDataTable<String, String> setupDataTable() {
        return new BSDataTableBuilder<>(createDataProvider())
                .appendPropertyColumn($m.ofValue("Debug"), s -> s)
                .setRowsPerPage(Long.MAX_VALUE)
                .setStripedRows(false)
                .setBorderedTable(false)
                .build("tabela");
    }

    private BaseDataProvider<String, String> createDataProvider() {

        return new BaseDataProvider<String, String>() {

            @Override
            public long size() {
                return Long.MAX_VALUE;
            }

            @Override
            public Iterator<String> iterator(int first, int count, String sortProperty, boolean ascending) {
                return listAllProperties().iterator();
            }
        };
    }

    @SuppressWarnings("squid:S1067")
    private List<String> listAllProperties() {
        StringBuilder sb = new StringBuilder();
        SingularProperties.get().debugContent(sb);
        String[]     values      = sb.toString().split("\n");
        List<String> permissions = Arrays.asList(values);
        List<String> finalList   = new ArrayList<>();
        for (String permission : permissions) {
            if ("#".equals(permission.trim())) {
                continue;
            }
            if (permission.contains("#")) {
                permission = permission.replaceFirst("#", "");
                permission = "<b>" + permission + "</b>";

            } else if (permission.contains("=")
                    && (permission.contains(".pass")
                                || permission.contains(".password")
                                || permission.contains(".passwd")
                                || permission.contains(".pwd")
                                || permission.contains(".senha")
            )) {
                permission = permission.substring(0, permission.indexOf('=')) + "=*****";

            }
            finalList.add(permission);
        }

        return finalList;
    }

}
