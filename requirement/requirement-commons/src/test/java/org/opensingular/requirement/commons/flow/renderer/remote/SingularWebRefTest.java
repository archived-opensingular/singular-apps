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

package org.opensingular.requirement.commons.flow.renderer.remote;

import org.apache.commons.lang3.NotImplementedException;
import org.junit.Assert;
import org.junit.Test;
import org.opensingular.requirement.commons.flow.SingularWebRef;
import org.opensingular.requirement.commons.admin.healthsystem.HealthSystemPage;

public class SingularWebRefTest {

    private SingularWebRef ref = new SingularWebRef(HealthSystemPage.class);

    @Test
    public void getPageClassTest(){
        Assert.assertEquals(HealthSystemPage.class, ref.getPageClass());
    }

    @Test(expected = NotImplementedException.class)
    public void getNomeTest(){
        ref.getName();
    }

    @Test(expected = NotImplementedException.class)
    public void getNomeCurtoTest(){
        ref.getShortName();
    }

    @Test(expected = NotImplementedException.class)
    public void getPathTest(){
        ref.getPath();
    }

    @Test(expected = NotImplementedException.class)
    public void getPathIconeTest(){
        ref.getIconPath();
    }

    @Test(expected = NotImplementedException.class)
    public void getPathIconePequenoTest(){
        ref.getSmallIconPath();
    }

    @Test(expected = NotImplementedException.class)
    public void getConfirmacaoTest(){
        ref.getConfirmationMessage();
    }

    @Test(expected = NotImplementedException.class)
    public void isPossuiDireitoAcessoTest(){
        ref.hasPermission();
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
        ref.isPopup();
    }

    @Test(expected = NotImplementedException.class)
    public void isSeAplicaAoContextoTest(){
        ref.appliesToContext();
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
    public void generateHtmlTest(){
        ref.generateHtml("html to generate");
    }
}
