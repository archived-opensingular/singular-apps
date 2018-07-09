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

package org.opensingular.requirement.commons.admin.healthsystem.stypes;

import org.opensingular.form.SIComposite;
import org.opensingular.form.SIList;
import org.opensingular.form.SInfoType;
import org.opensingular.form.STypeComposite;
import org.opensingular.form.STypeList;
import org.opensingular.form.TypeBuilder;
import org.opensingular.form.type.core.SIBoolean;
import org.opensingular.form.type.core.SIString;
import org.opensingular.form.type.core.STypeBoolean;
import org.opensingular.form.type.core.STypeString;
import org.opensingular.form.validation.InstanceValidatable;
import org.opensingular.form.view.SViewListByMasterDetail;
import org.opensingular.form.view.SViewListByTable;
import org.opensingular.lib.support.persistence.util.SqlUtil;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@SInfoType(spackage = SSystemHealthPackage.class,  name = "dbhealth")
public class SDbHealth extends STypeComposite<SIComposite> {

    public STypeList<STypeComposite<SIComposite>, SIComposite> tablesList;
    public STypeBoolean                                        found;
    public STypeString                                         schema;
    public STypeList<STypeString, SIString>                    userPrivs;
    public STypeBoolean                                        foundDataBase;
    public STypeBoolean                                        foundHibernate;
    public STypeBoolean                                        nullable;
    public STypeList<STypeComposite<SIComposite>, SIComposite> columnsInfo;

    @Override
    protected void onLoadType(@Nonnull TypeBuilder tb) {

        tablesList = this.addFieldListOfComposite("tablesList", "tabela");
        tablesList.withView(() -> new SViewListByMasterDetail().fullSize().disableNew().disableDelete());

        STypeComposite<SIComposite> table = tablesList.getElementsType();

        schema = table.addFieldString("schema");
        schema
                .asAtr()
                .label("Schema")
                .maxLength(20)
                .enabled(true)
                .asAtrBootstrap()
                .colPreference(2);

        table.addFieldString("tableName")
                .asAtr()
                .label("Nome")
                .maxLength(50)
                .enabled(true)
                .asAtrBootstrap()
                .colPreference(2);

        found = table.addFieldBoolean("found");
        found
                .asAtr()
                .label("Encontrado no Banco")
                .enabled(true)
                .asAtrBootstrap()
                .colPreference(2);

        userPrivs = table.addFieldListOf("userPrivs", STypeString.class);
        userPrivs
                .asAtr()
                .label("Permissões")
                .enabled(true)
                .asAtrBootstrap()
                .colPreference(2);
        userPrivs.withView(() -> new SViewListByTable().disableNew().disableDelete());

        table.addInstanceValidator(this::tableValidation);

        columnsInfo = table.addFieldListOfComposite("columnsInfo", "column");

        columnsInfo.withView(() -> new SViewListByTable().disableNew().disableDelete());
        columnsInfo.asAtr().label("Colunas");

        STypeComposite<SIComposite> column = columnsInfo.getElementsType();

        column.addFieldString("columnName")
                .asAtr()
                .label("Nome")
                .maxLength(50)
                .enabled(true)
                .asAtrBootstrap()
                .colPreference(2);

        STypeString dataType = column.addFieldString("dataType");
        dataType
                .selectionOf("CHAR", "CLOB", "DATE", "DATETIME", "NUMBER", "VARCHAR", "VARCHAR2")
                .asAtr()
                .label("Tipo de Dados")
                .maxLength(10)
                .enabled(true)
                .asAtrBootstrap()
                .colPreference(2);

        column.addFieldInteger("dataLength")
                .asAtr()
                .label("Tamanho(Bytes)")
                .enabled(true)
                .asAtrBootstrap()
                .colPreference(1);

        column.addFieldInteger("charLength")
                .asAtr()
                .label("Tamanho(Caracteres)")
                .enabled(true)
                .asAtrBootstrap()
                .colPreference(1);

        column.addFieldInteger("dataPrecision")
                .asAtr()
                .label("Precisao(valores numericos)")
                .asAtrBootstrap()
                .colPreference(1);

        nullable = column.addFieldBoolean("nullable");
        nullable
                .asAtr()
                .label("Aceita null")
                .asAtrBootstrap()
                .colPreference(1);

        foundHibernate = column.addFieldBoolean("foundHibernate");
        foundHibernate
                .asAtr()
                .label("Encontrado no Hibernate")
                .enabled(true)
                .asAtrBootstrap()
                .colPreference(2);

        foundDataBase = column.addFieldBoolean("foundDataBase");
        foundDataBase
                .asAtr()
                .label("Encontrado no Banco")
                .enabled(true)
                .asAtrBootstrap()
                .colPreference(2);

        column.addInstanceValidator(this::columnValidation);
    }

    private void columnValidation(InstanceValidatable<SIComposite> validatable) {
        Optional<SIBoolean> databaseFieldInstance  = validatable.getInstance().findNearest(foundDataBase);
        Optional<SIBoolean> hibernateFieldInstance = validatable.getInstance().findNearest(foundHibernate);

        // Encontrado no hibernate e nao no banco
        if (hibernateFieldInstance.isPresent()
                && hibernateFieldInstance.get().getValue()
                && databaseFieldInstance.isPresent()
                && !databaseFieldInstance.get().getValue()) {
            validatable.error("Inconsistency between database and Hibernate!");
        }
        else {
            // Encontrado no banco e nao no hibernate
            Optional<SIBoolean>        nullableFieldInstance = validatable.getInstance().findNearest(nullable);
            Optional<SIList<SIString>> listObj               = validatable.getInstance().findNearest(userPrivs);
            List<Object>               listPrivs             = listObj.map(SIList::getValue).orElse(Collections.emptyList());
            List<String>               vals                  = new ArrayList<>();
            listPrivs.forEach(obj -> vals.add((String) obj));

            if (nullableFieldInstance.isPresent() && !nullableFieldInstance.get().getValue() && (!vals.contains("INSERT") || !vals.contains("UPDATE"))) {
                validatable.error("Column NOT NULL without SELECT or UPDATE permissions");
            }
        }
    }

    private void tableValidation(InstanceValidatable<SIComposite> validatable) {

        Optional<SIBoolean> foundTableInstance = validatable.getInstance().findNearest(found);
        if (!foundTableInstance.isPresent() || !foundTableInstance.get().getValue()) {
            validatable.error("Table not found!");
        }

        Optional<SIString>         foundSchemaField = validatable.getInstance().findNearest(schema);
        Optional<SIList<SIString>> listObj          = validatable.getInstance().findNearest(userPrivs);
        List<Object>               listPrivs        = listObj.map(SIList::getValue).orElse(Collections.emptyList());
        List<String>               vals             = new ArrayList<>();
        listPrivs.forEach(obj -> vals.add((String) obj));

        if (foundSchemaField.isPresent()) {
            SIString foundSchemaFieldInstance = foundSchemaField.get();
            if (SqlUtil.isSingularSchema(foundSchemaFieldInstance.getValue())
                    && SqlUtil.hasCompleteCrud(vals)) {
                validatable.error("Singular table without complete CRUD!");
            }
        }

    }
}
