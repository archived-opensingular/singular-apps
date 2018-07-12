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
