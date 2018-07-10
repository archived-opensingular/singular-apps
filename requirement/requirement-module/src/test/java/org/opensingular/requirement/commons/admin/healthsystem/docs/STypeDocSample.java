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

import org.opensingular.form.SIComposite;
import org.opensingular.form.SInfoType;
import org.opensingular.form.STypeAttachmentList;
import org.opensingular.form.STypeComposite;
import org.opensingular.form.STypeList;
import org.opensingular.form.TypeBuilder;
import org.opensingular.form.type.core.STypeInteger;
import org.opensingular.form.type.core.attachment.STypeAttachment;
import org.opensingular.form.type.country.brazil.STypeAddress;
import org.opensingular.form.view.SViewByBlock;
import org.opensingular.form.view.SViewListByMasterDetail;

import javax.annotation.Nonnull;

@SInfoType(name = "STypeDocSample", spackage = SPackageDocSample.class, label = "Super SType Test")
public class STypeDocSample extends STypeComposite<SIComposite> {

    public static final String ANEXINHOS = "anexinhos";
    public static final String ANEXINHO = "anexinho";
    public static final String IDADE = "idade";
    public static final String ENDERECOS = "enderecos";
    public static final String ANEXOS = "Anexos";
    public static final String OUTROS_DADOS = "Outros Dados";
    private STypeAttachment anexinho;
    private STypeAttachmentList anexinhos;
    private STypeInteger idade;
    private STypeList<STypeAddress, SIComposite> enderecos;

    @Override
    protected void onLoadType(@Nonnull TypeBuilder tb) {
        anexinho = this.addFieldAttachment(ANEXINHO);
        anexinhos = this.addFieldListOfAttachment(ANEXINHOS, "anexo");
        idade = this.addFieldInteger(IDADE);
        enderecos = this.addFieldListOf(ENDERECOS, STypeAddress.class);
        enderecos.withView(SViewListByMasterDetail::new);

        this.withView(() -> new SViewByBlock()
                .newBlock(ANEXOS)
                .add(anexinhos)
                .add(anexinho)
                .newBlock(OUTROS_DADOS)
                .add(idade)
                .add(enderecos)
                .getView());
    }
}
