package org.opensingular.server.commons;

import org.opensingular.form.SIComposite;
import org.opensingular.form.SInfoType;
import org.opensingular.form.STypeComposite;
import org.opensingular.form.TypeBuilder;
import org.opensingular.form.type.core.STypeString;

import javax.annotation.Nonnull;

@SInfoType(label = "Foo", name = STypeFOO.NAME, spackage = SPackageFOO.class)
public class STypeFOO extends STypeComposite<SIComposite> {

    public static final String NAME = "StypeFoo";

    public static final String FULL_NAME = SPackageFOO.NAME + "." + NAME;
    public static final String FIELD_NOME = "nome";

    public STypeString nome;

    @Override
    protected void onLoadType(@Nonnull TypeBuilder tb) {
        nome = this.addFieldString(FIELD_NOME);
    }
}
