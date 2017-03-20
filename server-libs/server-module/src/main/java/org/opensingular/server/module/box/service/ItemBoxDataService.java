package org.opensingular.server.module.box.service;

import org.opensingular.server.commons.box.ItemBoxDataList;
import org.opensingular.server.commons.persistence.filter.QuickFilter;


public interface ItemBoxDataService {

    Long count(String boxId, QuickFilter filter);

    ItemBoxDataList search(String boxId, QuickFilter filter);

}