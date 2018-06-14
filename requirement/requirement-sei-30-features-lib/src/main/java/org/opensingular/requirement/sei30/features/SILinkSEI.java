package org.opensingular.requirement.sei30.features;

import javax.annotation.Nonnull;

import org.opensingular.form.SIComposite;

public class SILinkSEI extends SIComposite {

    @Nonnull
    @Override
    public STypeLinkSEI getType() {
        return (STypeLinkSEI) super.getType();
    }

    public String getProtocolo() {
        return getField(getType().protocolo).getValue();
    }

}
