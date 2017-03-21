package org.opensingular.server.module;

import org.opensingular.server.commons.box.ItemBoxData;
import org.opensingular.server.commons.box.factory.BoxItemActionList;
import org.opensingular.server.commons.box.filter.ItemBoxDataFilter;
import org.opensingular.server.commons.persistence.filter.QuickFilter;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public interface ItemBoxDataProvider {

    List<Map<String, Serializable>> search(QuickFilter filter);

    Long count(QuickFilter filter);

    BoxItemActionList getLineActions(ItemBoxData line, QuickFilter filter);

    default List<ItemBoxDataFilter> getFilters(){
        return Collections.emptyList();
    }

}