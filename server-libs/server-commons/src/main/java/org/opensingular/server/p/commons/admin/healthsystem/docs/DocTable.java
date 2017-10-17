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
