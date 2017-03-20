package org.opensingular.server.commons.box.chain;


import org.opensingular.server.commons.box.decorator.ItemBoxDataDecorator;
import org.opensingular.server.commons.persistence.filter.QuickFilter;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;

public class ItemBoxDataDecoratorChainImpl implements ItemBoxDataDecoratorChain {

    private Iterator<ItemBoxDataDecorator> decorators;

    public ItemBoxDataDecoratorChainImpl(Iterator<ItemBoxDataDecorator> decorators) {
        this.decorators = decorators;
    }

    @Override
    public void decorate(Map<String, Serializable> line, QuickFilter filter) {
        if (decorators.hasNext()) {
            decorators.next().decorate(line, filter, this);
        }
    }

}