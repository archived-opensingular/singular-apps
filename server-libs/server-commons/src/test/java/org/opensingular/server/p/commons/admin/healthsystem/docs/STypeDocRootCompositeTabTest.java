package org.opensingular.server.p.commons.admin.healthsystem.docs;

import org.opensingular.form.SIComposite;
import org.opensingular.form.SInfoType;
import org.opensingular.form.STypeAttachmentList;
import org.opensingular.form.STypeComposite;
import org.opensingular.form.STypeList;
import org.opensingular.form.TypeBuilder;
import org.opensingular.form.type.core.STypeInteger;
import org.opensingular.form.type.core.STypeString;
import org.opensingular.form.type.core.attachment.STypeAttachment;
import org.opensingular.form.type.country.brazil.STypeAddress;
import org.opensingular.form.view.SViewByBlock;
import org.opensingular.form.view.SViewListByMasterDetail;
import org.opensingular.form.view.SViewTab;

import javax.annotation.Nonnull;

@SInfoType(name = "STypeDocRootCompositeTabTest", spackage = SPackageDocTest.class, label = "Super SType STypeDocRootCompositeTabTest")
public class STypeDocRootCompositeTabTest extends STypeComposite<SIComposite> {

    private STypeAttachment anexos;
    private STypeString nome;
    private STypeDocTest docTest;

    @Override
    protected void onLoadType(@Nonnull TypeBuilder tb) {
        nome = this.addFieldString("nome");
        anexos = this.addFieldAttachment("anexos");
        docTest = this.addField("docTest", STypeDocTest.class);


        SViewTab tabbed = new SViewTab();
        tabbed.addTab("anexo", "Anexos Tab").add(nome).add(anexos);
        tabbed.addTab("superType", "SuperType Tab").add(docTest);
        this.withView(tabbed);
    }
}
