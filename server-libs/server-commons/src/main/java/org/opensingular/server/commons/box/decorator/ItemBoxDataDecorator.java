package org.opensingular.server.commons.box.decorator;

import org.opensingular.server.commons.box.chain.ItemBoxDataDecoratorChain;
import org.opensingular.server.commons.persistence.filter.QuickFilter;

import java.io.Serializable;
import java.util.Map;

public interface ItemBoxDataDecorator {

    void decorate(Map<String, Serializable> line, QuickFilter filter,  ItemBoxDataDecoratorChain chain);

}