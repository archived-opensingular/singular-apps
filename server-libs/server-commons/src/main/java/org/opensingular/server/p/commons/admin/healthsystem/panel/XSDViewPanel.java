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

package org.opensingular.server.p.commons.admin.healthsystem.panel;

import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.opensingular.form.SFormUtil;
import org.opensingular.form.SPackage;
import org.opensingular.form.SType;
import org.opensingular.form.context.SFormConfig;
import org.opensingular.form.io.FormToXSDConfig;
import org.opensingular.form.io.FormXSDUtil;
import org.opensingular.internal.lib.commons.xml.MElement;
import org.opensingular.lib.commons.scan.SingularClassPathScanner;
import org.opensingular.lib.wicket.util.datatable.BSDataTableBuilder;
import org.opensingular.lib.wicket.util.datatable.BaseDataProvider;
import org.opensingular.lib.wicket.util.datatable.IBSAction;
import org.opensingular.lib.wicket.util.modal.BSModalBorder;
import org.opensingular.lib.wicket.util.resource.DefaultIcons;

import javax.inject.Inject;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Modifier;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("serial")
public class XSDViewPanel extends Panel {

    private List<String> allTypes;

    private Model<String> xsdModel = new Model<>();

    private BSModalBorder modal;

    @Inject
    private SFormConfig<String> sFormConfig;

    public XSDViewPanel(String id) {
        super(id);
        loadAllTypes();
        addModal();
        addTable();
    }

    private void addTable() {
        BSDataTableBuilder<String, String, IColumn<String, String>> builder = new BSDataTableBuilder<>(new BaseDataProvider<String, String>() {
            @Override
            public Iterator<? extends String> iterator(int first, int count, String sortProperty, boolean ascending) {
                return allTypes.subList(first, first + count).iterator();
            }

            @Override
            public long size() {
                return allTypes.size();
            }
        });
        builder.appendPropertyColumn("Nome", i -> i);
        builder.appendActionColumn("", column -> column
                .appendAction(new Model<>(), DefaultIcons.EYE, (IBSAction<String>) (target, model) -> {
                    sFormConfig.getTypeLoader().loadType(model.getObject()).ifPresent(type -> {
                        MElement     element = FormXSDUtil.toXsd(type, FormToXSDConfig.newForUserDisplay());
                        StringWriter writer  = new StringWriter();
                        element.printTabulado(new PrintWriter(writer));
                        xsdModel.setObject(writer.toString());
                        modal.show(target);
                    });

                }));
        add(builder.build("table"));
    }

    private void addModal() {
        modal = new BSModalBorder("modal");
        modal.add(new Label("xsd", xsdModel));
        add(modal);
    }

    private void loadAllTypes() {
        allTypes = SingularClassPathScanner.get()
                .findSubclassesOf(SType.class)
                .stream()
                .filter(f -> !Modifier.isAbstract(f.getModifiers()) && !f.isAssignableFrom(SPackage.class))
                .map(i -> SFormUtil.getTypeName((Class<? extends SType<?>>) i))
                .collect(Collectors.toList());
    }


}
