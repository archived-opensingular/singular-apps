package org.opensingular.server.module.box.filter;

import org.opensingular.server.commons.box.filter.ItemBoxDataFilter;
import org.opensingular.server.module.workspace.ItemBoxFactory;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

@Named
public class ItemBoxDataFilterCollector {

    @Inject
    private ActionPermissionItemBoxDataFilter actionPermissionItemBoxDataFilter;

    @Inject
    private ActionAppenderItemBoxDataFilter actionAppenderItemBoxDataFilter;

    public List<ItemBoxDataFilter> getFilterList(ItemBoxFactory itemBoxDataProvider) {
        List<ItemBoxDataFilter> filters = new ArrayList<>();
        filters.add(actionAppenderItemBoxDataFilter);
        filters.add(actionPermissionItemBoxDataFilter);
        filters.addAll(itemBoxDataProvider.getDataProvider().getFilters());
        return filters;
    }

}