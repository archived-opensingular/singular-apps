package org.opensingular.server.p.commons.admin.healthsystem.panel;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.opensingular.form.SFormUtil;
import org.opensingular.form.SInfoType;
import org.opensingular.form.SType;
import org.opensingular.form.STypeComposite;
import org.opensingular.form.spring.SpringTypeLoader;
import org.opensingular.lib.commons.scan.SingularClassPathScanner;
import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.lib.wicket.util.datatable.BSDataTable;
import org.opensingular.lib.wicket.util.datatable.BSDataTableBuilder;
import org.opensingular.lib.wicket.util.datatable.BaseDataProvider;
import org.opensingular.lib.wicket.util.datatable.column.BSActionColumn;
import org.opensingular.lib.wicket.util.resource.DefaultIcons;
import org.opensingular.server.commons.spring.security.PermissionResolverService;
import org.opensingular.server.p.commons.admin.healthsystem.docs.wicket.DocumentatioTablePage;

import javax.inject.Inject;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static org.opensingular.lib.wicket.util.util.WicketUtils.$m;

public class DocsPanel extends Panel implements Loggable {

    @Inject
    protected SpringTypeLoader typeLoader;

    protected BSDataTable<Class<? extends STypeComposite>, String> listTable;

    @Inject
    private PermissionResolverService permissionResolverService;

    public DocsPanel(String id) {
        super(id);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        listTable = setupDataTable();
        queue(listTable);
    }

    protected BSDataTable<Class<? extends STypeComposite>, String> setupDataTable() {
        return new BSDataTableBuilder<>(createDataProvider())
                .appendPropertyColumn($m.ofValue("Formulários Encontrados"), this::formatarLabel)
                .appendActionColumn("Docs Table", this::buildColumn)
                .setRowsPerPage(Long.MAX_VALUE)
                .setStripedRows(false)
                .setBorderedTable(false)
                .build("tabela");
    }

    private void buildColumn(BSActionColumn<Class<? extends STypeComposite>, String> actionColumn) {
        actionColumn.appendAction($m.ofValue("Gerar Tabela"), DefaultIcons.MAGIC, this::gerarTabela);
        actionColumn.appendAction($m.ofValue("Gerar Tabela (SN)"), DefaultIcons.TRASH, this::gerarTabela);
        actionColumn.appendAction($m.ofValue("Gerar Tabela (SO)"), DefaultIcons.EXCLAMATION_TRIANGLE, this::gerarTabela);
        actionColumn.appendAction($m.ofValue("Gerar Tabela (SO&SN)"), DefaultIcons.ROCKET, this::gerarTabela);
    }

    private void gerarTabela(AjaxRequestTarget ajaxRequestTarget, IModel<Class<? extends STypeComposite>> model) {
        setResponsePage(new DocumentatioTablePage(model.getObject(), true, true));
    }


    private String formatarLabel(Class<? extends STypeComposite> c) {
        String typeName = SFormUtil.getTypeName((Class<? extends SType<?>>) c);
        return SFormUtil.getTypeLabel(c).map(l -> l + " (" + typeName + ")").orElse(typeName);
    }

    private BaseDataProvider<Class<? extends STypeComposite>, String> createDataProvider() {

        return new BaseDataProvider<Class<? extends STypeComposite>, String>() {

            @Override
            public long size() {
                return Long.MAX_VALUE;
            }

            @Override
            public Iterator<? extends Class<? extends STypeComposite>> iterator(int first, int count, String sortProperty, boolean ascending) {
                return listCompositeTypes().iterator();
            }
        };
    }

    public List<Class<? extends STypeComposite>> listCompositeTypes() {
        return SingularClassPathScanner
                .get()
                .findSubclassesOf(STypeComposite.class)
                .stream()
                .filter(c -> !(c.getPackage().getName().startsWith("org.opensingular") || c.getPackage().getName().startsWith("com.opensingular")))
                .filter(c -> c.isAnnotationPresent(SInfoType.class))
                .sorted((c1, c2) -> new CompareToBuilder().append(formatarLabel(c1), formatarLabel(c2)).build())
                .collect(Collectors.toList());
    }

}
