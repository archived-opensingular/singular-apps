package org.opensingular.server.p.commons.admin.healthsystem.docs.wicket;

public class DocumentationRowBlockSeparator implements DocumentationRow {

    private String blockName;

    public DocumentationRowBlockSeparator() {
    }

    public DocumentationRowBlockSeparator(String blockName) {
        this.blockName = blockName;

    }

    public String getBlockName() {
        return blockName;
    }
}
