package org.opensingular.server.module.box.service;

import org.opensingular.server.commons.box.chain.ItemBoxDataDecoratorChainFactory;
import org.opensingular.server.commons.persistence.filter.QuickFilter;
import org.opensingular.server.module.ItemBoxDataProvider;
import org.opensingular.server.module.SingularModuleConfiguration;
import org.opensingular.server.module.workspace.ItemBoxFactory;
import org.springframework.web.bind.annotation.PathVariable;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Named
public class ItemBoxDataServiceImpl implements ItemBoxDataService {

    private final SingularModuleConfiguration singularModuleConfiguration;


    @Inject
    public ItemBoxDataServiceImpl(SingularModuleConfiguration singularModuleConfiguration) {
        this.singularModuleConfiguration = singularModuleConfiguration;
    }

    private Optional<ItemBoxDataProvider> getItemBoxDataProvider(@PathVariable String boxId) {
        return singularModuleConfiguration.getItemBoxFactory(boxId).map(ItemBoxFactory::getDataProvider);
    }

    private Optional<ItemBoxDataDecoratorChainFactory> getItemBoxDataDecoratorChainFactory(@PathVariable String boxId) {
        return getItemBoxDataProvider(boxId).map(ItemBoxDataProvider::itemBoxDataDecoratorChainFactory);
    }


    @Override
    public Long count(String boxId, QuickFilter filter) {
        return getItemBoxDataProvider(boxId).map(provider -> provider.count(filter)).orElse(0L);
    }

    @Override
    public List<Map<String, Serializable>> search(String boxId, QuickFilter filter) {
        return searchAndApplyChain(boxId, filter);
    }

    private List<Map<String, Serializable>> searchAndApplyChain(String boxId, QuickFilter filter) {
        return applyDecoratorChain(boxId, findProviderAndSearch(boxId, filter), filter);
    }

    private List<Map<String, Serializable>> findProviderAndSearch(String boxId, QuickFilter filter) {
        return getItemBoxDataProvider(boxId).map(provider -> provider.search(filter)).orElse(Collections.emptyList());
    }

    private List<Map<String, Serializable>> applyDecoratorChain(String boxId, List<Map<String, Serializable>> lines, QuickFilter filter) {
        lines.forEach(line -> getItemBoxDataDecoratorChainFactory(boxId)
                .map(ItemBoxDataDecoratorChainFactory::newChain)
                .ifPresent(chain -> chain.decorate(line, filter)));
        return lines;
    }

}