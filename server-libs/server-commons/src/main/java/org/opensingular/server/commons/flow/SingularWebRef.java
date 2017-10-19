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

package org.opensingular.server.commons.flow;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.wicket.markup.html.WebPage;
import org.opensingular.lib.commons.net.Lnk;
import org.opensingular.lib.commons.net.ModalViewDef;
import org.opensingular.lib.commons.net.WebRef;

public class SingularWebRef implements WebRef {

    private Class<? extends WebPage> page;

    public SingularWebRef(Class<? extends WebPage> page) {
        this.page = page;
    }

    public Class<? extends WebPage> getPageClass() {
        return page;
    }


    @Override
    public String getName() {
        throw notImplementedException();
    }


    @Override
    public String getShortName() {
        throw notImplementedException();
    }


    @Override
    public Lnk getPath() {
        throw notImplementedException();
    }


    @Override
    public String getIconPath() {
        throw notImplementedException();
    }


    @Override
    public String getSmallIconPath() {
        throw notImplementedException();
    }


    @Override
    public String getConfirmationMessage() {
        throw notImplementedException();
    }


    @Override
    public boolean hasPermission() {
        throw notImplementedException();
    }


    @Override
    public boolean isJs() {
        throw notImplementedException();
    }


    @Override
    public String getJs() {
        throw notImplementedException();
    }


    @Override
    public boolean isPopup() {
        throw notImplementedException();
    }


    @Override
    public boolean appliesToContext() {
        throw notImplementedException();
    }


    @Override
    public ModalViewDef getModalViewDef() {
        throw notImplementedException();
    }


    @Override
    public WebRef addParam(String name, Object value) {
        throw notImplementedException();
    }


    @Override
    public String generateHtml(String urlApp) {
        throw notImplementedException();
    }

    private NotImplementedException notImplementedException() {
        return new NotImplementedException("Método não implementado, não é necessário para o singular.");
    }
}
