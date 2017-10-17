package org.opensingular.server.p.commons.admin.healthsystem.docs;

import org.opensingular.form.SType;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.List;

public class DocBlock implements Serializable {

    private final String blockName;

    private LinkedHashSet<DocFieldMetadata> metadataList = new LinkedHashSet<>();

    private LinkedHashSet<SType<?>> blockTypes = new LinkedHashSet<>();
    private boolean orphanBlock;

    public DocBlock(String blockName, List<SType<?>> blockTypes, boolean orphanBlock) {
        this.blockTypes.addAll(blockTypes);
        this.blockName = blockName;
        this.orphanBlock = orphanBlock;
    }

    public void addAllFieldsMetadata(LinkedHashSet<DocFieldMetadata> docFieldMetadata) {
        metadataList.addAll(docFieldMetadata);
    }

    public String getBlockName() {
        return blockName;
    }

    public LinkedHashSet<DocFieldMetadata> getMetadataList() {
        return metadataList;
    }

    public LinkedHashSet<SType<?>> getBlockTypes() {
        return blockTypes;
    }

    public boolean isOrphanBlock() {
        return orphanBlock;
    }
}
