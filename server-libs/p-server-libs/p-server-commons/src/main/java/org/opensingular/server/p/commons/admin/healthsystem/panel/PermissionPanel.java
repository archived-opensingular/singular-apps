package org.opensingular.server.p.commons.admin.healthsystem.panel;

import org.apache.wicket.markup.html.panel.Panel;
import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.lib.wicket.util.datatable.BSDataTable;
import org.opensingular.lib.wicket.util.datatable.BSDataTableBuilder;
import org.opensingular.lib.wicket.util.datatable.BaseDataProvider;
import org.opensingular.server.commons.service.PetitionService;
import org.opensingular.server.commons.spring.security.PermissionResolverService;
import org.opensingular.server.commons.spring.security.SingularPermission;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static org.opensingular.lib.wicket.util.util.WicketUtils.$m;

public class PermissionPanel extends Panel implements Loggable {

    @Inject
    protected PetitionService<?,?> petitionService;

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
