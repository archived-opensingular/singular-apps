package org.opensingular.server.p.commons.admin.healthsystem.docs;

import org.opensingular.form.SIComposite;
import org.opensingular.form.SInfoType;
import org.opensingular.form.STypeComposite;
import org.opensingular.form.TypeBuilder;
import org.opensingular.form.type.basic.AtrDOC;
import org.opensingular.form.type.core.STypeInteger;
import org.opensingular.form.type.core.STypeString;

import javax.annotation.Nonnull;

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
