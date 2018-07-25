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

package org.opensingular.requirement.module.admin.healthsystem.docs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections4.CollectionUtils;
import org.opensingular.form.SType;
import org.opensingular.form.STypes;
import org.opensingular.form.view.Block;
import org.opensingular.form.view.SView;
import org.opensingular.form.view.SViewByBlock;
import org.opensingular.form.view.SViewListByMasterDetail;
import org.opensingular.form.view.SViewTab;
import org.opensingular.form.view.ViewResolver;
import org.opensingular.lib.commons.lambda.IBiFunction;
import org.opensingular.requirement.module.admin.healthsystem.DocumentationMetadataUtil;

/**
 * Responsible for collect metadata for SType documentation
 * Reads information from attributes, validators, and views.
 * <p>
 * The documentation metadata is represented by three classes:
 * - {@link DocTable}: Form documentation is separated in tables: 1 table for each tab ({@link SViewTab}), 1 table for each master detail (#{@link SViewListByMasterDetail}) and 1 table for the root type if it is not already included by the previous rules
 * - {@link DocBlock}: Each table is further subdivided in blocks: 1 block for each block from view by block ({@link SViewByBlock}), 1 "anonymous" block gathering all types not related to any block in a table.
 * - {@link DocFieldMetadata}: Each block is inspected for STypes that directly represents fields in a HTML according to the method {@link DocumentationFieldMetadataBuilder#isFormInputField()}
 */
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
        if (isDocumentationRelated(toIterate)) {
            function.apply(toIterate, excludedTypes).forEach(blocks::add);
            for (SType<?> contained : STypes.containedTypes(toIterate)) {
                if (excludedTypes.contains(contained)) {
                    continue;
                }
                function.apply(contained, excludedTypes).forEach(blocks::add);
                blocks.addAll(recursiveIteration(contained, excludedTypes, function));
            }
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
            if (isDocumentationRelated(type)) {
                if (excludedTypes.contains(type)) {
                    continue;
                }
                orphans.add(type);
                orphans.addAll(recursiveIteration(type, excludedTypes, (s, e) -> e.contains(s) ? Stream.empty() : Stream.of(s)));
            }
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
        roots.addAll(identifyTablesRecursion(rootType));
        if (roots.stream().map(DocTable::getRootSTypes).flatMap(List::stream).noneMatch(rootType::equals)) {
            roots.add(new DocTable(DocumentationMetadataUtil.getLabelForType(rootType), rootType));

        }
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
        if (isDocumentationRelated(sType)) {
            SView view = getViewFor(sType);
            if (view instanceof SViewTab) {
                SViewTab viewTab = (SViewTab) view;
                for (SViewTab.STab t : viewTab.getTabs()) {
                    roots.add(new DocTable(DocumentationMetadataUtil.getLabelForType(t.getTitle(), sType), retrieveSTypeListFromRelativeTypeName(sType, t.getTypesNames()).toArray(new SType[0])));
                }
            } else if (view instanceof SViewListByMasterDetail) {
                roots.add(new DocTable(DocumentationMetadataUtil.getLabelForType(sType), sType));
            }
        }
        return roots;
    }

    private boolean isDocumentationRelated(SType<?> sType) {
        return !DocumentationMetadataUtil.isHiddenForDocumentation(sType);
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


    /**
     * Return all metadata information organized in the way it is described in this class javadoc {@link DocumentationMetadataBuilder}
     *
     * @return
     */
    public LinkedHashSet<DocTable> getMetadata() {
        return tableRoots;
    }




}
