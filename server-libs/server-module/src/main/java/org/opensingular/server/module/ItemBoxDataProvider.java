package org.opensingular.server.module;

import org.opensingular.server.commons.persistence.filter.QuickFilter;
import org.opensingular.server.commons.service.dto.DatatableField;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface ItemBoxDataProvider {


    List<Map<String, Serializable>> search(QuickFilter filter);

    Long count(QuickFilter filter);

    void configureLineActions(ItemBoxData line);

    List<DatatableField> datatableColumns();


}
