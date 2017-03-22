package org.opensingular.server.module.rest;

import org.opensingular.server.commons.box.ItemBoxData;
import org.opensingular.server.commons.box.ItemBoxDataList;
import org.opensingular.server.commons.persistence.filter.QuickFilter;
import org.opensingular.server.module.ItemBoxDataProvider;
import org.opensingular.server.module.SingularModuleConfiguration;
import org.opensingular.server.module.box.service.ItemBoxDataService;
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

    private final ItemBoxDataService itemBoxDataService;

    @Inject
    public ServerREST(ItemBoxDataService itemBoxDataService) {
        this.itemBoxDataService = itemBoxDataService;
    }

    @RequestMapping(value = "/count/{boxId}", method = RequestMethod.POST)
    public Long count(@PathVariable String boxId, @RequestBody QuickFilter filter) {
        return itemBoxDataService.count(boxId, filter);
    }

    @RequestMapping(value = "/search/{boxId}", method = RequestMethod.POST)
    public ItemBoxDataList search(@PathVariable String boxId, @RequestBody QuickFilter filter) {
        return itemBoxDataService.search(boxId, filter);
    }

}