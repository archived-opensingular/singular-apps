package org.opensingular.singular.pet.module.foobar.stuff;

import org.opensingular.form.PackageBuilder;
import org.opensingular.form.SPackage;

public class SPackageFoo extends SPackage {

    @Override
    protected void onLoadPackage(PackageBuilder pb) {
        super.onLoadPackage(pb);
        pb.createType(STypeFoo.class);
    }
}
