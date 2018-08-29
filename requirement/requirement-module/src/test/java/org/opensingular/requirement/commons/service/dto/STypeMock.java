package org.opensingular.requirement.commons.service.dto;

import javax.annotation.Nonnull;

import org.opensingular.form.SIComposite;
import org.opensingular.form.SInfoType;
import org.opensingular.form.STypeComposite;
import org.opensingular.form.TypeBuilder;
import org.opensingular.form.type.core.STypeString;

@SInfoType(spackage = MockPackage.class)
public class STypeMock extends STypeComposite<SIComposite> {

    public STypeString nome;
    public STypeString nomeMae;


    @Override
    protected void onLoadType(@Nonnull TypeBuilder tb) {
        this.asAtrAnnotation().setAnnotated();
        nome = addFieldString("nome");
        nomeMae = addFieldString("nomeMae");
        nome.asAtr().label("Nome").asAtrBootstrap().colPreference(6);
        nomeMae.asAtr().label("Nome Mãe").asAtrBootstrap().colPreference(6);
    }
}
