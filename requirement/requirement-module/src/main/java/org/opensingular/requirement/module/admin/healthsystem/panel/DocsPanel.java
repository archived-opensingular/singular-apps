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

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;

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
import org.opensingular.requirement.module.admin.healthsystem.docs.DocumentationTablePage;

import static org.opensingular.lib.wicket.util.util.WicketUtils.$m;

public class DocsPanel extends Panel implements Loggable {

    @Inject
    protected SpringTypeLoader<?>                                     typeLoader;

    protected BSDataTable<Class<? extends STypeComposite<?>>, String> listTable;

    public DocsPanel(String id) {
        super(id);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        listTable = setupDataTable();
        queue(listTable);
    }

    protected BSDataTable<Class<? extends STypeComposite<?>>, String> setupDataTable() {
        return new BSDataTableBuilder<>(createDataProvider())
            .appendPropertyColumn($m.ofValue("Formulários Encontrados"), this::formatLabel)
            .appendActionColumn("Docs Table", it -> this.buildColumn(it))
            .setRowsPerPage(Long.MAX_VALUE)
            .setStripedRows(false)
            .setBorderedTable(false)
            .build("tabela");
    }

    private void buildColumn(BSActionColumn<Class<? extends STypeComposite<?>>, String> actionColumn) {
        actionColumn.appendAction($m.ofValue("Gerar Tabela"), DefaultIcons.MAGIC, (a, s) -> createTable(a, s, false));
        actionColumn.appendAction($m.ofValue("Gerar Excel"), DefaultIcons.ROCKET, (a, s) -> createTable(a, s, true));
    }

    private void createTable(AjaxRequestTarget ajaxRequestTarget, IModel<Class<? extends STypeComposite<?>>> model, boolean excel) {
        setResponsePage(new DocumentationTablePage(model.getObject(), excel));
    }

    private String formatLabel(Class<? extends SType<?>> c) {
        String typeName = SFormUtil.getTypeName((Class<? extends SType<?>>) c);
        return SFormUtil.getTypeLabel((Class<? extends SType<?>>) c).map(l -> l + " (" + typeName + ")").orElse(typeName);
    }

    private BaseDataProvider<Class<? extends STypeComposite<?>>, String> createDataProvider() {
        return new BaseDataProvider<Class<? extends STypeComposite<?>>, String>() {
            @Override
            public long size() {
                return Long.MAX_VALUE;
            }
            @Override
            public Iterator<? extends Class<? extends STypeComposite<?>>> iterator(int first, int count, String sortProperty, boolean ascending) {
                return listCompositeTypes().iterator();
            }
        };
    }

    @SuppressWarnings("unchecked")
    public List<Class<? extends STypeComposite<?>>> listCompositeTypes() {
        return SingularClassPathScanner.get().findSubclassesOf(STypeComposite.class).stream()
            .map(it -> (Class<? extends STypeComposite<?>>) it)
            .filter(
                c -> !(c.getPackage().getName().startsWith("org.opensingular")
                    || c.getPackage().getName().startsWith("com.opensingular")))
            .filter(c -> c.isAnnotationPresent(SInfoType.class))
            .sorted((c1, c2) -> new CompareToBuilder().append(formatLabel(c1), formatLabel(c2)).build())
            .collect(Collectors.toList());
    }

}
