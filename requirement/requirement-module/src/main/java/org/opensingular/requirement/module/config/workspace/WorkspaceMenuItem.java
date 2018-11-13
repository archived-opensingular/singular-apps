package org.opensingular.requirement.module.config.workspace;

import org.apache.wicket.markup.html.panel.Panel;
import org.opensingular.lib.commons.ui.Icon;

import java.io.Serializable;

/**
 * Interface of menu itens
 */
public interface WorkspaceMenuItem extends Serializable {
    /**
     * Create the content when the item is accessed
     *
     * @param id the WICKET id
     */
    Panel newContent(String id);

    /**
     * Get the menu Icon
     */
    Icon getIcon();


    /**
     * Get the menu item name
     */
    String getName();

    /**
     *
     */
    String getDescription();

    /**
     *
     */
    String getHelpText();


    /**
     *
     */
    default boolean isVisible(){
        return true;
    }

    /**
     *
     * @return
     */
    default boolean showContentTitle(){
        return true;
    }
}