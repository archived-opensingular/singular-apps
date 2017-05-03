package org.opensingular.server.module.workspace;

import org.opensingular.server.commons.config.IServerContext;
import org.opensingular.server.commons.service.dto.DatatableField;
import org.opensingular.server.commons.service.dto.ItemBox;
import org.opensingular.server.module.BoxItemDataProvider;

import java.util.List;

/**
 * Factory responsible for build one item box with its listings, custom actions and controllers
 */
public interface ItemBoxFactory {

    /**
     * Checks is this {@link ItemBoxFactory} can be used under the given {@link IServerContext}
     * this method can be called multiple times.
     * @param context
     *  the current {@link IServerContext}
     * @return
     *  true if this factory can be used under the give {@param context}, false otherwise
     */
    boolean appliesTo(IServerContext context);

    /**
     * Builds an {@link ItemBox}. This method do not need to check if the {@param context} is supported, the current
     * {@link IServerContext} can be used to decide about minor changes in the {@param ItemBox} for each different
     * context
     * @param context
     *   the current {@link IServerContext}
     * @return
     *  An proper configured ItemBox
     */
    ItemBox build(IServerContext context);


    BoxItemDataProvider getDataProvider();

    List<DatatableField> getDatatableFields();

}
