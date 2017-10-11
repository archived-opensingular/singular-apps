package org.opensingular.server.p.commons.admin.healthsystem.docs;

import org.opensingular.form.SType;
import org.opensingular.form.view.Block;

public class FormDocumentationBlockSeparator implements DocumentationRow {

    private String blockName;

    public FormDocumentationBlockSeparator() {
    }

    public FormDocumentationBlockSeparator(SType<?> type, Block block) {

    }

    public String getBlockName() {
        return blockName;
    }

    @Override
    public RowType getRowType() {
        return RowType.SEPARATOR;
    }
}
