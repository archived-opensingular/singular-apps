package org.opensingular.requirement.sei30.features;

import javax.annotation.Nonnull;

import org.opensingular.form.SInfoType;
import org.opensingular.form.STypeComposite;
import org.opensingular.form.TypeBuilder;
import org.opensingular.form.type.core.STypeString;

@SInfoType(spackage = SEIFeaturePackage.class, label = "Propriedades do Modelo")
public class STypeModeloSEI extends STypeComposite<SIModeloSEI> {

    public STypeString protocoloModelo;

    public STypeModeloSEI() {
        super(SIModeloSEI.class);
    }

    @Override
    protected void onLoadType(@Nonnull TypeBuilder tb) {
        protocoloModelo = this.addFieldString("protocoloModelo");
        protocoloModelo.asAtr()
                .basicMask("9{1,7}")
                .maxLength(7)
                .required()
                .label("Documento Modelo");
    }
}
