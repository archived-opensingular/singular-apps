package org.opensingular.server.module.admin.extension.panel;

import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.opensingular.form.SFormUtil;
import org.opensingular.form.SType;
import org.opensingular.lib.wicket.util.datatable.BSDataTableBuilder;
import org.opensingular.lib.wicket.util.model.IReadOnlyModel;
import org.opensingular.server.module.SingularModuleConfiguration;
import org.opensingular.server.module.SingularRequirementRef;

import javax.inject.Inject;
import java.util.Iterator;
import java.util.List;

public class RequirementViewPanel extends Panel {

    @Inject
    private SingularModuleConfiguration singularModuleConfiguration;

    public RequirementViewPanel(String id) {
        super(id);
        addRequirementsTable();
    }

    private void addRequirementsTable() {
        add(new RequirementTableBuilder().build("table"));
    }

    private List<SingularRequirementRef> getRequirements() {
        return singularModuleConfiguration.getRequirements();
    }

    class RequirementTableBuilder extends
            BSDataTableBuilder<SingularRequirementRef, String, IColumn<SingularRequirementRef, String>> {
        RequirementTableBuilder() {
            super(new RequirementProvider());
            appendPropertyColumn("Nome", r -> r.getRequirement().getName());
            appendPropertyColumn("Form Principal", r -> SFormUtil
                    .getTypeName((Class<? extends SType<?>>) r.getRequirement().getMainForm()));
        }
    }

    class RequirementProvider extends SortableDataProvider<SingularRequirementRef, String> {

        @Override
        public Iterator<? extends SingularRequirementRef> iterator(long first, long count) {
            return getRequirements().subList((int) first, (int) (first + count)).iterator();
        }

        @Override
        public long size() {
            return getRequirements().size();
        }

        @Override
        public IModel<SingularRequirementRef> model(SingularRequirementRef object) {
            Long requirementKey;
            if (object != null) {
                requirementKey = object.getId();
            }
            else {
                requirementKey = null;
            }
            return (IReadOnlyModel<SingularRequirementRef>) () -> getRequirements().stream()
                    .filter(r -> r.getId().equals(requirementKey))
                    .findFirst()
                    .orElse(null);
        }
    }

}
