package org.opensingular.server.commons.admin.healthsystem.stypes;

import org.opensingular.form.SIComposite;

public class SIPdfHealth extends SIComposite {

    public String getEndpoint() {
        return getField(getType().endpoint).getValue();
    }

    public String getHtmlToExport() {
        return getField(getType().htmlToExport).getValue();
    }

    @Override
    public STypePdfHealth getType() {
        return (STypePdfHealth) super.getType();
    }
}
