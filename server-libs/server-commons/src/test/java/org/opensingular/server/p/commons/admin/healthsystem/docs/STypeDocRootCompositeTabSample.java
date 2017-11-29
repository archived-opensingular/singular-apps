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

package org.opensingular.server.commons.admin.healthsystem.docs;

import org.opensingular.form.SIComposite;
import org.opensingular.form.SInfoType;
import org.opensingular.form.STypeComposite;
import org.opensingular.form.TypeBuilder;
import org.opensingular.form.type.core.STypeString;
import org.opensingular.form.type.core.attachment.STypeAttachment;
import org.opensingular.form.view.SViewTab;

import javax.annotation.Nonnull;

@SInfoType(name = "STypeDocRootCompositeTabSample", spackage = SPackageDocSample.class, label = "Super SType STypeDocRootCompositeTabSample")
public class STypeDocRootCompositeTabSample extends STypeComposite<SIComposite> {

    public static final String NOME = "nome";
    public static final String ANEXOS = "anexos";
    private STypeAttachment anexos;
    private STypeString nome;
    private STypeDocSample docTest;

    @Override
    protected void onLoadType(@Nonnull TypeBuilder tb) {
        nome = this.addFieldString(NOME);
        anexos = this.addFieldAttachment(ANEXOS);
        docTest = this.addField("docTest", STypeDocSample.class);


        SViewTab tabbed = new SViewTab();
        tabbed.addTab("anexo", "Anexos Tab").add(nome).add(anexos);
        tabbed.addTab("superType", "SuperType Tab").add(docTest);
        this.withView(tabbed);
    }
}
