package org.opensingular.server.module;

import org.opensingular.lib.commons.lambda.IFunction;
import org.opensingular.server.module.requirement.SingularRequirement;
import org.opensingular.server.module.requirement.builder.SingularRequirementBuilder;
import org.opensingular.server.module.workspace.ItemBoxFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Configuration object for module {@link ItemBoxFactory} registrations.
 */
public class WorkspaceConfiguration {

    private List<BoxCofiguration> itemBoxes = new ArrayList<>();
    private RequirementConfiguration requirementConfiguration;

    WorkspaceConfiguration(RequirementConfiguration requirementConfiguration) {
        this.requirementConfiguration = requirementConfiguration;
    }

    /**
     * Register a single {@link ItemBoxFactory}
     *
     * @param itemBox the
     * @return
     */
    public WorkspaceConfiguration addBox(ItemBoxFactory itemBox) {
        itemBoxes.add(new BoxCofiguration(itemBox));
        return this;
    }

    List<BoxCofiguration> getItemBoxes() {
        return itemBoxes;
    }

    public WorkspaceConfiguration newFor(RequirementProvider... requirementProvider) {
        Arrays
                .stream(requirementProvider)
                .map(requirementConfiguration::getRequirementRef)
                .forEach(r -> getCurrent().addRequirementRefs(r));
        return this;

    }

    public WorkspaceConfiguration newFor(SingularRequirement... requirement) {
        Arrays
                .stream(requirement)
                .map(requirementConfiguration::getRequirementRef)
                .forEach(r -> getCurrent().addRequirementRefs(r));
        return this;
    }


    private BoxCofiguration getCurrent() {
        return itemBoxes.get(itemBoxes.size() - 1);
    }

    public static interface RequirementProvider extends IFunction<SingularRequirementBuilder, SingularRequirement> {
    }
}
