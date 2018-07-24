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

import org.apache.wicket.markup.html.panel.Panel;
import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.lib.wicket.util.datatable.BSDataTable;
import org.opensingular.lib.wicket.util.datatable.BSDataTableBuilder;
import org.opensingular.lib.wicket.util.datatable.BaseDataProvider;
import org.opensingular.requirement.module.service.RequirementService;
import org.opensingular.requirement.module.spring.security.PermissionResolverService;
import org.opensingular.requirement.module.spring.security.SingularPermission;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static org.opensingular.lib.wicket.util.util.WicketUtils.$m;

public class PermissionPanel extends Panel implements Loggable {

    @Inject
    protected RequirementService requirementService;

    protected BSDataTable<SingularPermission, String> listTable;

    @Inject
    private PermissionResolverService permissionResolverService;

    public PermissionPanel(String id) {
        super(id);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        listTable = setupDataTable();
        queue(listTable);
    }

    protected BSDataTable<SingularPermission, String> setupDataTable() {
        return new BSDataTableBuilder<>(createDataProvider())
                .appendPropertyColumn($m.ofValue("Permissão Singular"), SingularPermission::getSingularId)
                .setRowsPerPage(Long.MAX_VALUE)
                .setStripedRows(false)
                .setBorderedTable(false)
                .build("tabela");
    }

    private BaseDataProvider<SingularPermission, String> createDataProvider() {

        return new BaseDataProvider<SingularPermission, String>() {

            @Override
            public long size() {
                return Long.MAX_VALUE;
            }

            @Override
            public Iterator<SingularPermission> iterator(int first, int count, String sortProperty, boolean ascending) {
                List<SingularPermission> singularPermissions = listAllPermissions();
                if (singularPermissions != null) {
                    return singularPermissions.iterator();
                } else {
                    return Collections.EMPTY_LIST.iterator();
                }
            }
        };
    }

    public List<SingularPermission> listAllPermissions() {
        List<SingularPermission> permissions = new ArrayList<>();

        permissions.addAll(permissionResolverService.listAllCategoryPermissions());
        permissions.addAll(permissionResolverService.listAllTypePermissions());
        permissions.addAll(permissionResolverService.listAllProcessesPermissions());

        // Limpa o internal id por questão de segurança
        for (SingularPermission permission : permissions) {
            permission.setInternalId(null);
        }

        return permissions;
    }

}
