/*
 * Copyright (C) 2016 Singular Studios (a.k.a Atom Tecnologia) - www.opensingular.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.opensingular.requirement.module.wicket;

import org.opensingular.form.PackageBuilder;
import org.opensingular.form.SIComposite;
import org.opensingular.form.SInfoPackage;
import org.opensingular.form.SInfoType;
import org.opensingular.form.SPackage;
import org.opensingular.form.STypeComposite;
import org.opensingular.form.STypeList;
import org.opensingular.form.TypeBuilder;
import org.opensingular.form.type.core.STypeString;
import org.opensingular.form.view.SViewListByMasterDetail;

import javax.annotation.Nonnull;


@SInfoPackage(name = SPackageFOO.NAME)
public class SPackageFOO extends SPackage {

    public static final String NAME = "foooooo";

    @Override
    protected void onLoadPackage(@Nonnull PackageBuilder pb) {
        super.onLoadPackage(pb);
        pb.createType(STypeFOO.class);
    }

    @SInfoType(label = "Foo", name = STypeFOO.NAME, spackage = SPackageFOO.class)
    public static class STypeFOO extends STypeComposite<SIComposite> {

        public static final String NAME = "STypeFoo";

        public static final String FULL_NAME = SPackageFOO.NAME + "." + NAME;
        public static final String FIELD_NOME = "nome";

        public STypeString nome;

        @Override
        protected void onLoadType(@Nonnull TypeBuilder tb) {
            nome = this.addFieldString(FIELD_NOME);
        }
    }

    @SInfoType(label = "FooModalWithAnnotations", name = STypeFOOModal.NAME, spackage = SPackageFOO.class)
    public static class STypeFOOModal extends STypeComposite<SIComposite> {

        public static final String NAME = "StypeFooModal";

        public static final String FULL_NAME = SPackageFOO.NAME + "." + NAME;
        public static final String FIELD_NOME = "nome";

        public STypeList<STypeComposite<SIComposite>, SIComposite> pessoas;

        @Override
        protected void onLoadType(@Nonnull TypeBuilder tb) {
            pessoas = this.addFieldListOfComposite("pessoas", "pessoa");

            STypeComposite<SIComposite> pessoa = pessoas.getElementsType();
            STypeString nome = pessoa.addFieldString("nome");

            pessoas.withView(new SViewListByMasterDetail()
                    .col(nome, "Nome")
            );

            nome.asAtrAnnotation().setAnnotated();
        }
    }
}
