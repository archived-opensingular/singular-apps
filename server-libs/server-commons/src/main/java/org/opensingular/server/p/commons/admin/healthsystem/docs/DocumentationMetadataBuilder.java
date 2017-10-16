package org.opensingular.server.p.commons.admin.healthsystem.docs;

import org.apache.commons.collections4.CollectionUtils;
import org.opensingular.form.SFormUtil;
import org.opensingular.form.SType;
import org.opensingular.form.STypes;
import org.opensingular.form.view.Block;
import org.opensingular.form.view.SView;
import org.opensingular.form.view.SViewByBlock;
import org.opensingular.form.view.SViewListByMasterDetail;
import org.opensingular.form.view.SViewTab;
import org.opensingular.form.view.ViewResolver;
import org.opensingular.lib.commons.lambda.IBiFunction;
import org.opensingular.server.p.commons.admin.healthsystem.docs.wicket.DocumentationRow;
import org.opensingular.server.p.commons.admin.healthsystem.docs.wicket.DocumentationRowBlockSeparator;
import org.opensingular.server.p.commons.admin.healthsystem.docs.wicket.DocumentationRowFieldMetadata;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DocumentationMetadataBuilder {

    private Map<SType<?>, SView> viewsMap = new HashMap<>();
    private Map<String, SType<?>> typeByNameCache = new HashMap<>();
    private LinkedHashSet<DocTable> tableRoots;

    private void initTypeByNameCache(SType<?> rootStype) {
        STypes
                .streamDescendants(rootStype, true)
                .forEach(s -> typeByNameCache.put(s.getName(), s));
    }

    public DocumentationMetadataBuilder(SType<?> rootStype) {
        initTypeByNameCache(rootStype);
        tableRoots = identifyTablesRoots(rootStype);
        LinkedHashSet<SType<?>> tableRootsSTypes = tableRoots.stream().flatMap(d -> d.getRootSTypes().stream()).collect(LinkedHashSet::new, LinkedHashSet::add, LinkedHashSet::addAll);
        for (DocTable docTable : tableRoots) {
            LinkedHashSet<DocBlock> docBlocks = identifyBlocks(docTable.getRootSTypes(), tableRootsSTypes);
            docTable.addAllDocBlocks(docBlocks);
            LinkedHashSet<SType<?>> allTypesAssociatedToBlocks = gatherAllSTypesAssociatedToBlocks(docBlocks);
            allTypesAssociatedToBlocks.addAll(tableRootsSTypes);
            for (DocBlock docBlock : docBlocks) {
                LinkedHashSet<DocFieldMetadata> docFieldMetadata = identifyFields(rootStype, docBlock, CollectionUtils.subtract(allTypesAssociatedToBlocks, docBlock.getBlockTypes()));
                docBlock.addAllFieldsMetadata(docFieldMetadata);
            }
        }
    }

    private LinkedHashSet<DocFieldMetadata> identifyFields(SType<?> rootStype, DocBlock docBlock, Collection<SType<?>> excludedTypes) {
        LinkedHashSet<DocFieldMetadata> fields = new LinkedHashSet<>();
        for (SType<?> sType : docBlock.getBlockTypes()) {
            if (excludedTypes.contains(sType)) {
                continue;
            }
            DocumentationFieldMetadataBuilder docFieldMetadataBuilder = new DocumentationFieldMetadataBuilder(rootStype, sType);
            if (docFieldMetadataBuilder.isFormInputField()) {
                fields.add(docFieldMetadataBuilder.getDocFieldMetadata());
                excludedTypes.add(sType);
                continue;
            }
            fields.addAll(identifyFieldsRecursiveIteration(sType, rootStype, excludedTypes));
        }
        return fields;
    }

    private LinkedHashSet<DocFieldMetadata> identifyFieldsRecursiveIteration(SType<?> toIterate, SType<?> rootStype, Collection<SType<?>> excludedTypes) {
        LinkedHashSet<DocFieldMetadata> fields = new LinkedHashSet<>();
        for (SType<?> sType : STypes.containedTypes(toIterate)) {
            if (excludedTypes.contains(sType)) {
                continue;
            }
            DocumentationFieldMetadataBuilder docFieldMetadataBuilder = new DocumentationFieldMetadataBuilder(rootStype, sType);
            if (docFieldMetadataBuilder.isFormInputField()) {
                fields.add(docFieldMetadataBuilder.getDocFieldMetadata());
                excludedTypes.add(sType);
                continue;
            }
            fields.addAll(identifyFieldsRecursiveIteration(sType, rootStype, excludedTypes));
        }
        return fields;
    }


    private LinkedHashSet<DocBlock> identifyBlocks(List<SType<?>> rootTypes, LinkedHashSet<SType<?>> tableRootsSTypes) {
        LinkedHashSet<DocBlock> blocks = new LinkedHashSet<>();
        for (SType<?> type : rootTypes) {
            blocks.addAll(recursiveIteration(type, tableRootsSTypes, this::toStypeToBlockStream));
        }
        identifyOrphanTypesBlock(rootTypes, tableRootsSTypes, gatherAllSTypesAssociatedToBlocks(blocks)).ifPresent(blocks::add);
        return blocks;
    }

    private LinkedHashSet<SType<?>> gatherAllSTypesAssociatedToBlocks(LinkedHashSet<DocBlock> blocks) {
        return blocks.stream().flatMap(b -> b.getBlockTypes().stream()).collect(LinkedHashSet::new, LinkedHashSet::add, LinkedHashSet::addAll);
    }

    private <X> LinkedHashSet<X> recursiveIteration(SType<?> toIterate, LinkedHashSet<SType<?>> excludedTypes, IBiFunction<SType<?>, LinkedHashSet<SType<?>>, Stream<X>> function) {
        LinkedHashSet<X> blocks = new LinkedHashSet<>();
        function.apply(toIterate, excludedTypes).forEach(blocks::add);
        for (SType<?> contained : STypes.containedTypes(toIterate)) {
            if (excludedTypes.contains(contained)) {
                continue;
            }
            function.apply(contained, excludedTypes).forEach(blocks::add);
            blocks.addAll(recursiveIteration(contained, excludedTypes, function));
        }
        return blocks;
    }

    private Optional<DocBlock> identifyOrphanTypesBlock(List<SType<?>> rootTypes, LinkedHashSet<SType<?>> tableRootsSTypes, LinkedHashSet<SType<?>> typesAssociatedToBlocks) {
        List<SType<?>> orphans = new ArrayList<>();
        LinkedHashSet<SType<?>> excludedTypes = new LinkedHashSet<>();
        excludedTypes.addAll(tableRootsSTypes);
        excludedTypes.addAll(typesAssociatedToBlocks);
        excludedTypes.removeAll(rootTypes);
        for (SType<?> type : rootTypes) {
            if (excludedTypes.contains(type)) {
                continue;
            }
            orphans.add(type);
            orphans.addAll(recursiveIteration(type, excludedTypes, (s, e) -> e.contains(s) ? Stream.empty() : Stream.of(s)));
        }
        if (orphans.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(new DocBlock(null, orphans, true));
        }
    }

    @SuppressWarnings("unchecked")
    private Stream<DocBlock> toStypeToBlockStream(SType<?> sType, LinkedHashSet<SType<?>> excludedTypes) {
        SView view = getViewFor(sType);
        if (view instanceof SViewByBlock) {
            SViewByBlock byBlock = (SViewByBlock) view;
            List<DocBlock> docBlockList = new ArrayList<>();
            for (Block b : byBlock.getBlocks()) {
                List<SType<?>> list = retrieveSTypeListFromRelativeTypeName(sType, b.getTypes());
                list.removeAll(excludedTypes);
                docBlockList.add(new DocBlock(b.getName(), list, false));
            }
            return docBlockList.stream();
        }
        return Stream.empty();
    }

    private List<SType<?>> retrieveSTypeListFromRelativeTypeName(SType<?> sType, List<String> types) {
        return types.stream().map(s -> typeByNameCache.get(sType.getName() + "." + s)).collect(Collectors.toList());
    }


    private LinkedHashSet<DocTable> identifyTablesRoots(SType<?> rootType) {
        LinkedHashSet<DocTable> roots = new LinkedHashSet<>();
        roots.addAll(collectTableRoots(rootType));
        if (!roots.contains(rootType)) {
            roots.add(new DocTable(getLabelForType(null, rootType), rootType));
        }
        roots.addAll(identifyTablesRecursion(rootType));
        return roots;
    }

    private LinkedHashSet<DocTable> identifyTablesRecursion(SType<?> toIterate) {
        LinkedHashSet<DocTable> roots = new LinkedHashSet<>();
        for (SType<?> contained : STypes.containedTypes(toIterate)) {
            roots.addAll(collectTableRoots(contained));
            roots.addAll(identifyTablesRecursion(contained));
        }
        return roots;
    }


    private LinkedHashSet<DocTable> collectTableRoots(SType<?> sType) {
        LinkedHashSet<DocTable> roots = new LinkedHashSet<DocTable>();
        SView view = getViewFor(sType);
        if (view instanceof SViewTab) {
            SViewTab viewTab = (SViewTab) view;
            for (SViewTab.STab t : viewTab.getTabs()) {
                roots.add(new DocTable(getLabelForType(t.getTitle(), sType), retrieveSTypeListFromRelativeTypeName(sType, t.getTypesNames()).toArray(new SType[0])));
            }
        } else if (view instanceof SViewListByMasterDetail) {
            roots.add(new DocTable(getLabelForType(null, sType), sType));
        }
        return roots;
    }

    private String getLabelForType(String defaultString, SType<?> type) {
        String label = defaultString;
        if (label == null) {
            label = type.asAtr().getLabel();
            if (label == null) {
                label = SFormUtil.getTypeLabel(type.getClass()).orElse(null);
                if (label == null) {
                    label = type.getNameSimple();
                }
            }
        }
        return label;
    }

    private SView getViewFor(SType<?> s) {
        SView view = viewsMap.get(s);
        if (view == null) {
            view = Optional.ofNullable(ViewResolver.resolveView(s)).orElse(NULLVIEW);
            viewsMap.put(s, view);
        }
        if (view == NULLVIEW) {
            view = null;
        }
        return view;
    }


    private static SView NULLVIEW = new SView() {
    };


    public LinkedHashSet<DocTable> getMetadata() {
        return tableRoots;
    }

    public List<StreamLinedMetadata> getStreamLinedMetadata() {
        List<StreamLinedMetadata> list = new ArrayList<>();
        for (DocTable table : tableRoots) {
            List<DocumentationRow> documentationRows = new ArrayList<>();
            boolean first = true;
            for (DocBlock docBlock : table.getBlockList()) {
                if (!docBlock.getMetadataList().isEmpty()) {
                    if (!docBlock.isOrphanBlock() && table.getBlockList().size() > 1) {
                        documentationRows.add(new DocumentationRowBlockSeparator(docBlock.getBlockName()));
                    }
                    for (DocFieldMetadata docFieldMetadata : docBlock.getMetadataList()) {
                        documentationRows.add(new DocumentationRowFieldMetadata(docFieldMetadata));
                    }
                    first = false;
                }
            }
            StreamLinedMetadata streamLinedMetadata = new StreamLinedMetadata(table.getName(), documentationRows);
            if (!streamLinedMetadata.isEmptyOfRows()) {
                list.add(streamLinedMetadata);
            }
        }
        return list;
    }

    public static class StreamLinedMetadata implements Serializable {

        private String tableName;
        private List<DocumentationRow> documentationRows;

        public StreamLinedMetadata(String tableName, List<DocumentationRow> documentationRows) {
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
}
