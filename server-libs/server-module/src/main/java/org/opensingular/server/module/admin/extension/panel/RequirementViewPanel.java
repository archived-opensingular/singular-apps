package org.opensingular.server.module.admin.extension.panel;

import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.opensingular.lib.wicket.util.datatable.BSDataTableBuilder;
import org.opensingular.server.module.SingularModuleConfiguration;
import org.opensingular.server.module.SingularRequirementRef;
import org.opensingular.server.module.admin.extension.RequirementDTO;

import javax.inject.Inject;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

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

    private class RequirementTableBuilder extends BSDataTableBuilder<RequirementDTO, String, IColumn<RequirementDTO, String>> {
        RequirementTableBuilder() {
            super(new RequirementProvider());
            appendPropertyColumn("Nome", RequirementDTO::getName);
            appendPropertyColumn("Form Principal", RequirementDTO::getMainFormName);
        }
    }

    private class RequirementProvider extends SortableDataProvider<RequirementDTO, String> {
        @Override
        public Iterator<RequirementDTO> iterator(long first, long count) {
            return getRequirements().stream().map(RequirementDTO::new)
                    .collect(Collectors.toList())
                    .subList((int) first, (int) (first + count)).iterator();
        }

        @Override
        public long size() {
            return getRequirements().size();
        }

        @Override
        public IModel<RequirementDTO> model(RequirementDTO requirementDTO) {
            return Model.of(requirementDTO);
        }
    }


}
