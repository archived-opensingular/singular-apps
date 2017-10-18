package org.opensingular.server.p.commons.admin.healthsystem.docs;

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
