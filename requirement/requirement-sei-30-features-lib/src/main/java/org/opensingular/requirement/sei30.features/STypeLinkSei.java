package org.opensingular.requirement.sei30.features;

import javax.annotation.Nonnull;

import org.opensingular.form.SInfoType;
import org.opensingular.form.STypeComposite;
import org.opensingular.form.TypeBuilder;
import org.opensingular.form.type.core.STypeString;

@SInfoType(spackage = SeiFeaturePackage.class)
public class STypeLinkSei  extends STypeComposite<SILinkSei> {

    public STypeString protocolo;

    public STypeLinkSei() {
        super(SILinkSei.class);
    }

    @Override
    protected void onLoadType(@Nonnull TypeBuilder tb) {
        protocolo = this.addFieldString("protocolo");
        protocolo.asAtr().label("Protocolo");

    }
}
