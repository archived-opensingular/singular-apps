package org.opensingular.studio.core.panel.button;

import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.link.DownloadLink;
import org.apache.wicket.model.IModel;

import java.io.File;

public abstract class HeaderRightDownloadButton implements IHeaderRightButton {

    protected abstract IModel<File> getFile();

    @Override
    public AbstractLink createButton(String id) {
        return new DownloadLink(id, getFile());
    }
}
