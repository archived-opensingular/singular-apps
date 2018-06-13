package org.opensingular.requirement.sei30.features;

import javax.annotation.Nonnull;

import org.opensingular.form.SIComposite;

public class SIModeloSEI extends SIComposite {

    @Nonnull
    @Override
    public STypeModeloSEI getType() {
        return (STypeModeloSEI) super.getType();
    }

    public String getProtocoloModelo() {
        return getField(getType().protocoloModelo).getValue();
    }

}
