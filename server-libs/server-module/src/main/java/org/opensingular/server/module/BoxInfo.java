package org.opensingular.server.module;

import java.util.Set;

public interface BoxInfo {

    String getBoxId();

    Set<SingularRequirementRef> getRequirementRefs();
}
