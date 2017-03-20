package org.opensingular.server.module.box.filter;

import org.opensingular.server.commons.box.decorator.ActionPermissionItemBoxDataFilter;
import org.opensingular.server.commons.box.decorator.ItemBoxDataFilter;
import org.opensingular.server.commons.box.decorator.PetitionActionAppenderItemBoxDataFilter;
import org.opensingular.server.commons.box.decorator.TaskActionAppenderItemBoxDataFilter;
import org.opensingular.server.module.workspace.ItemBoxFactory;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

@Named
public class ItemBoxDataFiltersFactory {

    @Inject
    private ActionPermissionItemBoxDataFilter actionPermissionItemBoxDataFilter;

    @Inject
    private TaskActionAppenderItemBoxDataFilter taskActionAppenderItemBoxDataFilter;

    @Inject
    private PetitionActionAppenderItemBoxDataFilter petitionActionAppenderItemBoxDataFilter;

    public List<ItemBoxDataFilter> getFilters(ItemBoxFactory itemBoxDataProvider) {
        List<ItemBoxDataFilter> filters = new ArrayList<>();
        filters.add(taskActionAppenderItemBoxDataFilter);
        filters.add(petitionActionAppenderItemBoxDataFilter);
        filters.add(actionPermissionItemBoxDataFilter);
        filters.addAll(itemBoxDataProvider.getDataProvider().getFilters());
        return filters;
    }

}