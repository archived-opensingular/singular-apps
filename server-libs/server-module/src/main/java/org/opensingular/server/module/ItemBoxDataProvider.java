package org.opensingular.server.module;

import org.opensingular.server.commons.persistence.filter.QuickFilter;

import java.util.List;

public interface ItemBoxDataProvider {


    List<ItemBoxData> search(QuickFilter filter);


    Long count(QuickFilter filter);


}
