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

package org.opensingular.server.p.commons.admin.healthsystem.docs;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.opensingular.form.SType;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

public class DocTable {

    private List<SType<?>> tableRoots;

    private String name;

    private LinkedHashSet<DocBlock> docBlocks = new LinkedHashSet<>();

    public DocTable(String name, SType<?>... types) {
        this.tableRoots = Arrays.asList(types);
        this.name = name;
    }

    public void addAllDocBlocks(Collection<DocBlock> blocks) {
        this.docBlocks.addAll(blocks);
    }

    public List<SType<?>> getRootSTypes() {
        return tableRoots;
    }

    public LinkedHashSet<DocBlock> getBlockList() {
        return docBlocks;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        DocTable docTable = (DocTable) o;

        return new EqualsBuilder()
                .append(tableRoots, docTable.tableRoots)
                .append(name, docTable.name)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(tableRoots)
                .append(name)
                .toHashCode();
    }
}
