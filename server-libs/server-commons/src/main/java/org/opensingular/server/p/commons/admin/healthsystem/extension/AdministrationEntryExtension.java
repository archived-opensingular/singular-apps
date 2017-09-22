package org.opensingular.server.p.commons.admin.healthsystem.extension;

import org.apache.wicket.markup.html.panel.Panel;
import org.opensingular.lib.commons.base.SingularUtil;
import org.opensingular.lib.commons.extension.SingularExtension;

import java.io.Serializable;

public interface AdministrationEntryExtension extends SingularExtension, Serializable {

    /**
     * @return the name of the entry
     */
    String name();

    /**
     * Make the panel to be rendered when the entry is acessed
     *
     * @param id, the wicket id
     * @return the panel
     */
    Panel makePanel(String id);


    default String getKey(){
        return SingularUtil.convertToJavaIdentity(name(), true);
    }
}