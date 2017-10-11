package org.opensingular.server.p.commons.admin.healthsystem.docs;

import org.opensingular.form.SType;
import org.opensingular.form.STypes;
import org.opensingular.form.view.SViewByBlock;
import org.opensingular.form.view.SViewListByMasterDetail;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class DocumentationMetadataBuilder {

    private Map<String, SType> typeByNameCache = new HashMap<>();
    private LinkedHashSet<DocTable> tableRoots;
    private LinkedHashSet<SType<?>> tableRootsSTypes;

    private void initTypeByNameCache(SType<?> rootStype) {
        STypes
                .streamDescendants(rootStype, true)
                .forEach(s -> typeByNameCache.put(s.getName(), s));
    }

    public DocumentationMetadataBuilder(SType<?> rootStype) {
        initTypeByNameCache(rootStype);
        tableRoots = identifyTablesRoots(rootStype);
        tableRootsSTypes = tableRoots.stream().map(DocTable::getRootSType).collect(LinkedHashSet::new, LinkedHashSet::add, LinkedHashSet::addAll);
        for (DocTable docTable : tableRoots) {
            LinkedHashSet<DocBlock> docBlocks = identifyBlocks(docTable.getRootSType(), tableRootsSTypes);
            docTable.addAllDocBlocks(docBlocks);
            for (DocBlock docBlock : docBlocks) {
                LinkedHashSet<DocFieldMetadata> docFieldMetadata = identifyFields(docBlock);
                docBlock.addAllFieldsMetadata(docFieldMetadata);
            }
        }
    }

    private LinkedHashSet<DocFieldMetadata> identifyFields(DocBlock docBlock) {
        return null;
    }

    private LinkedHashSet<DocBlock> identifyBlocks(SType<?> rootType, LinkedHashSet<SType<?>> tableRootsSTypes) {
        return STypes
                .streamDescendants(rootType, true)
                .filter(s -> !tableRootsSTypes.contains(s))
                .filter(s -> s.getView() instanceof SViewByBlock)
                .map(this::toStypeToBlockStream)
                .collect(LinkedHashSet::new, LinkedHashSet::add, LinkedHashSet::addAll);
    }

    private Stream<DocBlock> toStypeToBlockStream(SType<?> sType) {
        SViewByBlock byBlock = (SViewByBlock) sType.getView();
        return byBlock.getBlocks().stream().map(b -> new DocBlock(byBlock.getName(), sType, null));
    }


    private LinkedHashSet<DocTable> identifyTablesRoots(SType<?> rootType) {
        LinkedHashSet<DocTable> typesSet = new LinkedHashSet<>();
        typesSet.add(new DocTable(rootType));
        return STypes
                .streamDescendants(rootType, true)
                .filter(s -> s.getView() instanceof SViewListByMasterDetail)
                .map(DocTable::new)
                .collect(() -> typesSet, LinkedHashSet::add, LinkedHashSet::addAll);
    }

    public LinkedHashSet<DocTable> getMetadata() {
        return tableRoots;
    }

    public List<StreamLinedMetadata> getStreamLinedMetadata() {
        return Collections.emptyList();
    }

    public static class StreamLinedMetadata {

        private DocTable docTable;
        private List<DocumentationRow> documentationRows;

        public StreamLinedMetadata(DocTable docTable, List<DocumentationRow> documentationRows) {
            this.docTable = docTable;
            this.documentationRows = documentationRows;
        }

        public DocTable getDocTable() {
            return docTable;
        }

        public List<DocumentationRow> getDocumentationRows() {
            return documentationRows;
        }
    }
}
