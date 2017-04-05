package org.opensingular.server.module.box.service;

import org.opensingular.server.commons.box.ItemBoxData;
import org.opensingular.server.commons.box.ItemBoxDataList;
import org.opensingular.server.commons.box.filter.ItemBoxDataFilter;
import org.opensingular.server.commons.persistence.filter.QuickFilter;
import org.opensingular.server.module.ItemBoxDataProvider;
import org.opensingular.server.module.SingularModuleConfiguration;
import org.opensingular.server.module.box.filter.ItemBoxDataFilterCollector;
import org.opensingular.server.module.workspace.ItemBoxFactory;
import org.springframework.web.bind.annotation.PathVariable;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Named
public class ItemBoxDataServiceImpl implements ItemBoxDataService {

    @Inject
    private SingularModuleConfiguration singularModuleConfiguration;

    @Inject
    private ItemBoxDataFilterCollector filterCollector;

    @Override
    public Long count(String boxId, QuickFilter filter) {
        return getItemBoxDataProvider(boxId).map(provider -> provider.count(filter)).orElse(0L);
    }

    @Override
    public ItemBoxDataList search(String boxId, QuickFilter filter) {
        return new ItemBoxDataList().setItemBoxDataList(searchAndApplyFilters(boxId, filter));
    }

    private Optional<ItemBoxDataProvider> getItemBoxDataProvider(String boxId) {
        return getItemBoxFactory(boxId).map(ItemBoxFactory::getDataProvider);
    }

    private Optional<ItemBoxFactory> getItemBoxFactory(String boxId) {
        return singularModuleConfiguration.getItemBoxFactory(boxId);
    }

    private List<ItemBoxData> searchAndApplyFilters(String boxId, QuickFilter filter) {
        List<ItemBoxData> itemBoxDatas = convertMapToItemBoxData(findProviderAndSearch(boxId, filter));
        if (itemBoxDatas != null) {
            applyFilters(boxId, itemBoxDatas, filter);
        }
        return itemBoxDatas;
    }

    private List<ItemBoxData> convertMapToItemBoxData(List<Map<String, Serializable>> searchResult) {
        return searchResult.stream()
                .map(map -> new ItemBoxData().setRawMap(map)).collect(Collectors.toList());
    }

    private List<Map<String, Serializable>> findProviderAndSearch(String boxId, QuickFilter filter) {
        return getItemBoxDataProvider(boxId).map(provider -> provider.search(filter)).orElse(Collections.emptyList());
    }

    private void applyFilters(String boxId, List<ItemBoxData> tableItens, QuickFilter filter) {
        getItemBoxFactory(boxId).ifPresent(factory -> {
            List<ItemBoxDataFilter> filters = filterCollector.getFilterList(factory);
            for (ItemBoxData itemBoxData : tableItens) {
                filters.forEach(f -> f.doFilter(boxId, itemBoxData, filter));
            }
        });
    }


}