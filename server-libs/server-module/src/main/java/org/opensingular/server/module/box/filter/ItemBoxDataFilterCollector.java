package org.opensingular.server.module.box.filter;

import org.opensingular.server.commons.box.filter.ActionPermissionItemBoxDataFilter;
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

    public List<ItemBoxDataFilter> getFilterList(ItemBoxFactory itemBoxDataProvider) {
        List<ItemBoxDataFilter> filters = new ArrayList<>();
        filters.addAll(itemBoxDataProvider.getDataProvider().getFilters());
        filters.add(actionPermissionItemBoxDataFilter);
        return filters;
    }

}