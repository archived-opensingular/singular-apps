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

package org.opensingular.requirement.module.admin.healthsystem.docs.presentation;

import org.opensingular.form.SType;
import org.opensingular.lib.commons.table.ColumnType;
import org.opensingular.lib.commons.table.TableOutput;
import org.opensingular.lib.commons.table.TableOutputHtml;
import org.opensingular.lib.commons.table.TablePopulator;
import org.opensingular.lib.commons.table.TableTool;
import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.lib.commons.views.format.ViewOutputHtmlWriterWrap;
import org.opensingular.requirement.module.admin.healthsystem.docs.DocTable;
import org.opensingular.requirement.module.admin.healthsystem.docs.DocumentationMetadataBuilder;
import org.opensingular.requirement.module.admin.healthsystem.docs.DocBlock;
import org.opensingular.requirement.module.admin.healthsystem.docs.DocFieldMetadata;

import java.io.Writer;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public class DefaultFormDocumentationRenderer implements FormDocumentationRenderer, Loggable {


    private static final String CHARSET = "UTF-8";

    @Override
    public void renderTables(SType<?> rootStype, List<FormDocumentationColumnRenderer> columns, Writer writer) {
        LinkedHashSet<DocTable> metadata = getMetadata(rootStype);
        TableOutput             html     = getTableOutputHtml(writer);
        for (DocTable table : metadata) {
            TableTool tableTool = newTableTool(table, columns);
            TablePopulator populator = tableTool.createSimpleTablePopulator();
            for (List<String> row : convertToRows(table, columns)) {
                populator.insertLine(row.toArray(new String[row.size()]));
            }
            tableTool.generate(html);
        }
    }

    private List<List<String>> convertToRows(DocTable table, List<FormDocumentationColumnRenderer> columns) {
        List<List<String>> rowsList = new ArrayList<>();
        for (DocBlock block : table.getBlockList()) {
            for (DocFieldMetadata fieldMetadata : block.getMetadataList()) {
                List<String> row = new ArrayList<>(columns.size());
                for (FormDocumentationColumnRenderer col : columns) {
                    row.add(col.renderColumn(table, block, fieldMetadata));
                }
                rowsList.add(row);
            }
        }
        return rowsList;
    }

    private TableTool newTableTool(DocTable table, List<FormDocumentationColumnRenderer> columns) {
        TableTool tableTool = new TableTool();
        for (FormDocumentationColumnRenderer renderer : columns) {
            tableTool.addColumn(ColumnType.HTML, renderer.getColumnName());
        }
        tableTool.addSuperTitle(0, columns.size() - 1, table.getName());
        return tableTool;
    }

    protected LinkedHashSet<DocTable> getMetadata(SType<?> stype) {
        return new DocumentationMetadataBuilder(stype).getMetadata();
    }

    protected TableOutput getTableOutputHtml(Writer writer) {
        try {
            return new TableOutputHtml(new ViewOutputHtmlWriterWrap(writer, true));
        } catch (Exception e) {
            getLogger().error(e.getMessage(), e);
        }
        return null;
    }


}
