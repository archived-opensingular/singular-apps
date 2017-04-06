package org.opensingular.server.commons;

import org.opensingular.form.PackageBuilder;
import org.opensingular.form.SInfoPackage;
import org.opensingular.form.SPackage;

import javax.annotation.Nonnull;


@SInfoPackage(name = SPackageFOO.NAME)
public class SPackageFOO extends SPackage {

    public static final String NAME = "foooooo";

    @Override
    protected void onLoadPackage(@Nonnull PackageBuilder pb) {
        super.onLoadPackage(pb);
        pb.createType(STypeFOO.class);
    }
}
