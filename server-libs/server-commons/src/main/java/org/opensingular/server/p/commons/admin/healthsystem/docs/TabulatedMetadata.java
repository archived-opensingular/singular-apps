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

package org.opensingular.server.p.commons.admin.healthsystem.docs;

import org.apache.commons.collections4.CollectionUtils;
import org.opensingular.server.p.commons.admin.healthsystem.docs.wicket.DocumentationRow;

import java.io.Serializable;
import java.util.List;

/**
 * A documentation table definition with an ordered list of lines
 */
public class TabulatedMetadata implements Serializable {

    private String tableName;
    private List<DocumentationRow> documentationRows;

    public TabulatedMetadata(String tableName, List<DocumentationRow> documentationRows) {
        this.tableName = tableName;
        this.documentationRows = documentationRows;
    }

    public List<DocumentationRow> getDocumentationRows() {
        return documentationRows;
    }

    public String getTableName() {
        return tableName;
    }

    public boolean isEmptyOfRows() {
        return CollectionUtils.isEmpty(documentationRows);
    }
}
