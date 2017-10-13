package org.opensingular.server.p.commons.admin.healthsystem.docs;

import org.opensingular.form.SType;
import org.opensingular.form.view.Block;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.List;

public class DocBlock implements Serializable {

    private final String blockName;
    private SType<?> blockRootType;

    private LinkedHashSet<DocFieldMetadata> metadataList = new LinkedHashSet<>();

    private List<SType<?>> blockTypes;

    public DocBlock(String blockName, SType<?> blockRootType, List<SType<?>> blockTypes) {
        this.blockRootType = blockRootType;
        this.blockTypes = blockTypes;
        this.blockName = blockName;
    }

    public void addAllFieldsMetadata(LinkedHashSet<DocFieldMetadata> docFieldMetadata) {
        metadataList.addAll(docFieldMetadata);
    }

    public String getBlockName() {
        return blockName;
    }

    public SType<?> getBlockRootType() {
        return blockRootType;
    }

    public LinkedHashSet<DocFieldMetadata> getMetadataList() {
        return metadataList;
    }

    public List<SType<?>> getBlockTypes() {
        return blockTypes;
    }
}
