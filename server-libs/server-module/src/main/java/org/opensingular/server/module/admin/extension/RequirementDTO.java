package org.opensingular.server.module.admin.extension;

import org.opensingular.form.SFormUtil;
import org.opensingular.form.SType;
import org.opensingular.server.commons.requirement.SingularRequirement;
import org.opensingular.server.module.SingularRequirementRef;

import java.io.Serializable;

public class RequirementDTO implements Serializable {

    private final Long                   id;
    private final String                 name;
    private final Class<? extends SType> mainForm;

    public RequirementDTO(SingularRequirementRef singularRequirementRef) {
        SingularRequirement requirement = singularRequirementRef.getRequirement();
        id = singularRequirementRef.getId();
        name = requirement.getName();
        mainForm = requirement.getMainForm();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Class<? extends SType> getMainForm() {
        return mainForm;
    }

    public String getMainFormName() {
        return SFormUtil.getTypeName((Class<? extends SType<?>>) mainForm);
    }
}
