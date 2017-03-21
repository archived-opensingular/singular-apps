package org.opensingular.server.commons.box.filter;

import org.opensingular.server.commons.box.ItemBoxData;
import org.opensingular.server.commons.persistence.filter.QuickFilter;

public interface ItemBoxDataFilter {

    void doFilter(String boxId, ItemBoxData itemBoxData, QuickFilter filter);

}