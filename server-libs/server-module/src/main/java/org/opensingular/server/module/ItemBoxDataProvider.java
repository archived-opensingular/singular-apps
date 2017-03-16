package org.opensingular.server.module;

import org.opensingular.server.commons.persistence.filter.QuickFilter;

import java.util.List;
import java.util.Map;

public interface ItemBoxDataProvider {


    List<Map<String, Object>> search(QuickFilter filter);

    Long count(QuickFilter filter);

    void configureLineActions(ItemBoxData line);


}
