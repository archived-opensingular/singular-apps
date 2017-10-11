package org.opensingular.server.p.commons.admin.healthsystem.docs;

import org.opensingular.lib.commons.util.Loggable;

import java.io.Serializable;

public interface DocumentationRow extends Serializable, Loggable {

    public enum RowType {
        DATA, SEPARATOR
    }

    RowType getRowType();
}
