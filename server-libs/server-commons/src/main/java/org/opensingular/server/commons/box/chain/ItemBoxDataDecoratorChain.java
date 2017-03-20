package org.opensingular.server.commons.box.chain;

import org.opensingular.server.commons.persistence.filter.QuickFilter;

import java.io.Serializable;
import java.util.Map;

public interface ItemBoxDataDecoratorChain {

    void decorate(Map<String, Serializable> line, QuickFilter filter);

}