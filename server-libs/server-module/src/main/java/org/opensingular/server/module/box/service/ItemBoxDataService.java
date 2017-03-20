package org.opensingular.server.module.box.service;

import org.opensingular.server.commons.persistence.filter.QuickFilter;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface ItemBoxDataService {

    Long count(String boxId, QuickFilter filter);

    List<Map<String, Serializable>> search(String boxId, QuickFilter filter);

}