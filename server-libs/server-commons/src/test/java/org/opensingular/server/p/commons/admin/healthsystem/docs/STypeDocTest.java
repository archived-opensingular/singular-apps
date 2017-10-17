package org.opensingular.server.p.commons.admin.healthsystem.docs;

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

@SInfoType(name = "STypeDocTest", spackage = SPackageDocTest.class, label = "Super SType Test")
public class STypeDocTest extends STypeComposite<SIComposite> {

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
        enderecos.setView(SViewListByMasterDetail::new);

        this.setView(() -> new SViewByBlock()
                .newBlock(ANEXOS)
                .add(anexinhos)
                .add(anexinho)
                .newBlock(OUTROS_DADOS)
                .add(idade)
                .add(enderecos)
                .getView());
    }
}
