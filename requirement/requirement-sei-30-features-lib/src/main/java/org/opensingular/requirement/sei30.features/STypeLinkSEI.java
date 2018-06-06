package org.opensingular.requirement.sei30.features;

import javax.annotation.Nonnull;

import org.opensingular.form.SInfoType;
import org.opensingular.form.STypeComposite;
import org.opensingular.form.TypeBuilder;
import org.opensingular.form.type.core.STypeString;

@SInfoType(spackage = SEIFeaturePackage.class, label = "Propriedades do Link")
public class STypeLinkSEI extends STypeComposite<SILinkSEI> {

    public STypeString protocolo;

    public STypeLinkSEI() {
        super(SILinkSEI.class);
    }

    @Override
    protected void onLoadType(@Nonnull TypeBuilder tb) {
        protocolo = this.addFieldString("protocolo");
        protocolo.asAtr().label("Protocolo");

    }
}
