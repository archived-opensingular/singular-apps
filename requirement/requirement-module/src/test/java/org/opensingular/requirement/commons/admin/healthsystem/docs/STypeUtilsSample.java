/*
 *
 *  * Copyright (C) 2016 Singular Studios (a.k.a Atom Tecnologia) - www.opensingular.com
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  * http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package org.opensingular.requirement.commons.admin.healthsystem.docs;

import javax.annotation.Nonnull;

import org.opensingular.form.SIComposite;
import org.opensingular.form.SInfoType;
import org.opensingular.form.STypeComposite;
import org.opensingular.form.TypeBuilder;
import org.opensingular.form.type.basic.AtrDOC;
import org.opensingular.form.type.core.STypeInteger;
import org.opensingular.form.type.core.STypeString;

@SInfoType(name = "STypeUtilsSample", spackage = SPackageDocSample.class, label = "Super SType Util Test")
public class STypeUtilsSample extends STypeComposite<SIComposite> {


    public STypeString nome;
    public STypeString definicao;
    public STypeInteger idade;
    public STypeString observacoes;
    public STypeString observacoesOcultas;
    public STypeString visivel;


    @Override
    protected void onLoadType(@Nonnull TypeBuilder tb) {
        nome = this.addFieldString("nome");
        nome.asAtr().exists(false);
        definicao = this.addFieldString("definicao");
        definicao.asAtr().visible(false);
        idade = this.addFieldInteger("idade");
        idade.as(AtrDOC::new).hiddenForDocumentation();
        observacoes = this.addFieldString("observacoes");
        observacoes.asAtr().visible(true);
        observacoes.asAtr().exists(false);

        observacoesOcultas = this.addFieldString("observacoesOcultas");
        observacoesOcultas.as(AtrDOC::new).hiddenForDocumentation();
        observacoesOcultas.asAtr().visible(true);
        observacoesOcultas.asAtr().exists(true);

        visivel = this.addFieldString("visivel");
    }
}
