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

package org.opensingular.requirement.sip.client;

import org.apache.commons.lang3.builder.RecursiveToStringStyle;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.junit.Ignore;
import org.junit.Test;
import org.opensingular.requirement.sip.util.SIPUtil;

import javax.xml.ws.BindingProvider;

@Ignore
public class ServiceTest {

    @Test
    public void testLogin(){
        SipService      sipService  = new SipService();
        SipPortType     sipPortType = sipService.getSipPortType();
        BindingProvider bp          = (BindingProvider) sipPortType;
        bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,"http://sei/sip/ws/SipWS.php");

        RetornoAutenticarCompleto autenticarCompleto = sipPortType.autenticarCompleto("0","vinicius.nunes", SIPUtil.encodePassword("123456"),"SEI","ANTAQ");
        RetornoValidarLogin validarLogin = sipPortType.validarLogin(autenticarCompleto.getIdLogin(), autenticarCompleto.getIdSistema(), autenticarCompleto.getIdUsuario(), autenticarCompleto.getHashAgente());

        System.out.println(new ReflectionToStringBuilder(validarLogin, new RecursiveToStringStyle()).toString());

    }
}
