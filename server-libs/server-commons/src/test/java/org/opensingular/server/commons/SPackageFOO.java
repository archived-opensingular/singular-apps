package org.opensingular.server.commons;

import org.opensingular.form.PackageBuilder;
import org.opensingular.form.SInfoPackage;
import org.opensingular.form.SPackage;

import javax.annotation.Nonnull;


@SInfoPackage(name = "foooooo")
public class SPackageFOO extends SPackage {

    @Override
    protected void onLoadPackage(@Nonnull PackageBuilder pb) {
        super.onLoadPackage(pb);
        pb.createType(STypeFOO.class);
    }
}
