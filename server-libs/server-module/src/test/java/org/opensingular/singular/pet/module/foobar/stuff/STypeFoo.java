package org.opensingular.singular.pet.module.foobar.stuff;

import org.opensingular.form.SIComposite;
import org.opensingular.form.SInfoType;
import org.opensingular.form.STypeComposite;
import org.opensingular.form.TypeBuilder;
import org.opensingular.form.type.core.STypeString;

@SInfoType(name = "StypeFoo", newable = true, label = "Foo form", spackage = SPackageFoo.class)
public class STypeFoo extends STypeComposite<SIComposite> {

    STypeString fooName;

    @Override
    protected void onLoadType(TypeBuilder tb) {
        fooName = this.addFieldString("fooName");
    }
}
