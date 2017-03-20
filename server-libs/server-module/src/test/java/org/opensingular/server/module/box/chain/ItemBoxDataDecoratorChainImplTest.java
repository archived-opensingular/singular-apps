package org.opensingular.server.module.box.chain;

import org.junit.Test;
import org.opensingular.server.commons.box.chain.ItemBoxDataDecoratorChain;
import org.opensingular.server.commons.box.chain.ItemBoxDataDecoratorChainFactory;
import org.opensingular.server.commons.box.chain.ItemBoxDataDecoratorChainFactoryImpl;
import org.opensingular.server.commons.box.decorator.ItemBoxDataDecorator;
import org.opensingular.server.commons.persistence.filter.QuickFilter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertTrue;


public class ItemBoxDataDecoratorChainImplTest {

    @Test
    public void testDecorate() {

        ItemBoxDataDecoratorChainFactory factory = new ItemBoxDataDecoratorChainFactoryImpl();

        MockItemBoxDataDecorator firstDecorator = new MockItemBoxDataDecorator();
        MockItemBoxDataDecorator secoundDecorator = new MockItemBoxDataDecorator();

        factory.addDecorator(firstDecorator);
        factory.addDecorator(secoundDecorator);

        ItemBoxDataDecoratorChain chain = factory.newChain();

        Map<String, Serializable> line = new HashMap<>();

        chain.decorate(line, new QuickFilter());

        assertTrue(firstDecorator.isAcessed());
        assertTrue(secoundDecorator.isAcessed());
    }

}

class MockItemBoxDataDecorator implements ItemBoxDataDecorator {

    private boolean acessed = false;

    public boolean isAcessed() {
        return acessed;
    }

    @Override
    public void decorate(Map<String, Serializable> line, QuickFilter filter, ItemBoxDataDecoratorChain chain) {
        this.acessed = true;
        chain.decorate(line, filter);
    }
}