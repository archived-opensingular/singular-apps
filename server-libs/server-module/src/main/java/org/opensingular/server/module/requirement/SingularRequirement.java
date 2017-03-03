package org.opensingular.server.module.requirement;

import org.opensingular.form.SType;

public interface SingularRequirement {

    String getName();

    Class<? extends SType> getMainForm();

    BoundedFlowResolver getFlowResolver();

}
