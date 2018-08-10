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

package org.opensingular.studio.core.panel;

import java.io.Serializable;

public class CrudListConfig implements Serializable {

    private Long rowsPerPage;
    private Boolean stripedRows;
    private Boolean hoverRows;
    private Boolean borderedTable;
    private Boolean advancedTable;
    private Boolean condensedTable;

    public Long getRowsPerPage() {
        return rowsPerPage;
    }

    public CrudListConfig setRowsPerPage(Long rowsPerPage) {
        this.rowsPerPage = rowsPerPage;
        return this;
    }

    public Boolean getStripedRows() {
        return stripedRows;
    }

    public CrudListConfig setStripedRows(Boolean stripedRows) {
        this.stripedRows = stripedRows;
        return this;
    }

    public Boolean getHoverRows() {
        return hoverRows;
    }

    public CrudListConfig setHoverRows(Boolean hoverRows) {
        this.hoverRows = hoverRows;
        return this;
    }

    public Boolean getBorderedTable() {
        return borderedTable;
    }

    public CrudListConfig setBorderedTable(Boolean borderedTable) {
        this.borderedTable = borderedTable;
        return this;
    }

    public Boolean getAdvancedTable() {
        return advancedTable;
    }

    public CrudListConfig setAdvancedTable(Boolean advancedTable) {
        this.advancedTable = advancedTable;
        return this;
    }

    public Boolean getCondensedTable() {
        return condensedTable;
    }

    public CrudListConfig setCondensedTable(Boolean condensedTable) {
        this.condensedTable = condensedTable;
        return this;
    }

}
