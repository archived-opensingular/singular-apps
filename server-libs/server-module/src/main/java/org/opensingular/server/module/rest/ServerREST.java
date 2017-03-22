package org.opensingular.server.module.rest;

import org.opensingular.server.commons.box.ItemBoxDataList;
import org.opensingular.server.commons.persistence.filter.QuickFilter;
import org.opensingular.server.module.box.service.ItemBoxDataService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

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