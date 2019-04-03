package org.opensingular.studio.core.panel;

import org.apache.wicket.markup.html.link.DownloadLink;
import org.apache.wicket.model.IModel;

import java.io.File;

public abstract class HeaderRightDownloadLink extends DownloadLink implements CrudListContent.HeaderRightButton {

    public HeaderRightDownloadLink(IModel<File> model) {
        super("ignoredId", model);
    }

}
