/*
 * Copyright (C) 2016 Singular Studios (a.k.a Atom Tecnologia) - www.opensingular.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
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
