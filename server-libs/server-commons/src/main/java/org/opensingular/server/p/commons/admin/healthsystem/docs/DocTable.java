package org.opensingular.server.p.commons.admin.healthsystem.docs;

import org.opensingular.form.SType;

import java.util.Collection;
import java.util.LinkedHashSet;

public class DocTable {

    private SType<?> tableRoot;

    private LinkedHashSet<DocBlock> docBlocks = new LinkedHashSet<>();

    public DocTable(SType<?> tableRoot) {
        this.tableRoot = tableRoot;
    }

    public void addAllDocBlocks(Collection<DocBlock> blocks) {
        this.docBlocks.addAll(blocks);
    }

    public SType<?> getRootSType() {
        return tableRoot;
    }

    public LinkedHashSet<DocBlock> getBlockList() {
        return docBlocks;
    }
}
