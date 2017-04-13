package org.opensingular.server.commons.flow.renderer.remote;

import org.apache.commons.lang3.NotImplementedException;
import org.junit.Assert;
import org.junit.Test;
import org.opensingular.server.commons.flow.SingularWebRef;
import org.opensingular.server.p.commons.admin.healthsystem.HealthSystemPage;

public class SingularWebRefTest {

    private SingularWebRef ref = new SingularWebRef(HealthSystemPage.class);

    @Test
    public void getPageClassTest(){
        Assert.assertEquals(HealthSystemPage.class, ref.getPageClass());
    }

    @Test(expected = NotImplementedException.class)
    public void getNomeTest(){
        ref.getNome();
    }

    @Test(expected = NotImplementedException.class)
    public void getNomeCurtoTest(){
        ref.getNomeCurto();
    }

    @Test(expected = NotImplementedException.class)
    public void getPathTest(){
        ref.getPath();
    }

    @Test(expected = NotImplementedException.class)
    public void getPathIconeTest(){
        ref.getPathIcone();
    }

    @Test(expected = NotImplementedException.class)
    public void getPathIconePequenoTest(){
        ref.getPathIconePequeno();
    }

    @Test(expected = NotImplementedException.class)
    public void getConfirmacaoTest(){
        ref.getConfirmacao();
    }

    @Test(expected = NotImplementedException.class)
    public void isPossuiDireitoAcessoTest(){
        ref.isPossuiDireitoAcesso();
    }

    @Test(expected = NotImplementedException.class)
    public void isJsTest(){
        ref.isJs();
    }

    @Test(expected = NotImplementedException.class)
    public void getJsTest(){
        ref.getJs();
    }

    @Test(expected = NotImplementedException.class)
    public void isAbrirEmNovaJanelaTest(){
        ref.isAbrirEmNovaJanela();
    }

    @Test(expected = NotImplementedException.class)
    public void isSeAplicaAoContextoTest(){
        ref.isSeAplicaAoContexto();
    }

    @Test(expected = NotImplementedException.class)
    public void getModalViewDefTest(){
        ref.getModalViewDef();
    }

    @Test(expected = NotImplementedException.class)
    public void addParamTest(){
        ref.addParam("param", "value");
    }

    @Test(expected = NotImplementedException.class)
    public void gerarHtmlTest(){
        ref.gerarHtml("html to generate");
    }
}
