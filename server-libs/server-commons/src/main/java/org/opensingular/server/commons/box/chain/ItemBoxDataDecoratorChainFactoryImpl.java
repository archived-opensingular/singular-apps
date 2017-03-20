package org.opensingular.server.commons.box.chain;

import org.opensingular.server.commons.box.decorator.ItemBoxDataDecorator;

import java.util.ArrayList;
import java.util.List;


public class ItemBoxDataDecoratorChainFactoryImpl implements ItemBoxDataDecoratorChainFactory{

    private List<ItemBoxDataDecorator> decorators;

    public ItemBoxDataDecoratorChainFactoryImpl() {
        this.decorators = new ArrayList<>();
    }

    @Override
    public ItemBoxDataDecoratorChain newChain() {
        return new ItemBoxDataDecoratorChainImpl(decorators.iterator());
    }

    @Override
    public ItemBoxDataDecoratorChainFactory addDecorator(ItemBoxDataDecorator decorator) {
        decorators.add(decorator);
        return this;
    }

    public List<ItemBoxDataDecorator> getDecorators() {
        return decorators;
    }

}