package org.opensingular.requirement.sei30.features;

import javax.annotation.Nonnull;

import org.opensingular.form.SIComposite;

public class SILinkSei extends SIComposite {

    @Nonnull
    @Override
    public STypeLinkSei getType() {
        return (STypeLinkSei) super.getType();
    }

    public String getProtocolo() {
        return getField(getType().protocolo).getValue();
    }

}
