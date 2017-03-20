package org.opensingular.server.module.box.chain;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.opensingular.server.commons.box.chain.ItemBoxDataDecoratorChainFactoryImpl;
import org.opensingular.server.commons.box.decorator.ItemBoxDataDecorator;

import static org.junit.Assert.*;


public class ItemBoxDataDecoratorChainFactoryImplTest {

    private ItemBoxDataDecoratorChainFactoryImpl itemBoxDataDecoratorChainFactory;

    @Before
    public void setUp() {
        itemBoxDataDecoratorChainFactory = new ItemBoxDataDecoratorChainFactoryImpl();
    }

    @Test
    public void testAddDecorator() throws Exception {
        ItemBoxDataDecorator myDecorator = Mockito.mock(ItemBoxDataDecorator.class);
        itemBoxDataDecoratorChainFactory.addDecorator(myDecorator);
        assertThat(itemBoxDataDecoratorChainFactory.getDecorators(), Matchers.hasSize(1));
        assertThat(itemBoxDataDecoratorChainFactory.getDecorators().get(0), Matchers.is(myDecorator));
    }

}