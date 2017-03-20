package org.opensingular.server.commons.box.chain;


import org.opensingular.server.commons.box.decorator.ItemBoxDataDecorator;

public interface ItemBoxDataDecoratorChainFactory {

    ItemBoxDataDecoratorChain newChain();

    ItemBoxDataDecoratorChainFactory addDecorator(ItemBoxDataDecorator dataDecorator);

}