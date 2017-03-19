package org.opensingular.server.module.rest;

import org.opensingular.server.commons.persistence.filter.QuickFilter;
import org.opensingular.server.module.ItemBoxDataProvider;
import org.opensingular.server.module.SingularModuleConfiguration;
import org.opensingular.server.module.workspace.ItemBoxFactory;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/rest/flow")
public class ServerREST {

    private final SingularModuleConfiguration singularModuleConfiguration;

    @Inject
    public ServerREST(SingularModuleConfiguration singularModuleConfiguration) {
        this.singularModuleConfiguration = singularModuleConfiguration;
    }

    private Optional<ItemBoxDataProvider> getItemBoxDataProvider(@PathVariable String boxId) {
        return singularModuleConfiguration.getItemBoxFactory(boxId).map(ItemBoxFactory::getDataProvider);
    }

    @RequestMapping(value = "/count/{boxId}", method = RequestMethod.POST)
    public Long count(@PathVariable String boxId, @RequestBody QuickFilter filter) {
        return getItemBoxDataProvider(boxId).map(provider -> provider.count(filter)).orElse(0L);
    }

    @RequestMapping(value = "/search/{boxId}", method = RequestMethod.POST)
    public List<Map<String, Serializable>> search(@PathVariable String boxId, @RequestBody QuickFilter filter) {
        return getItemBoxDataProvider(boxId).map(provider -> provider.search(filter)).orElse(Collections.emptyList());
    }

}