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
