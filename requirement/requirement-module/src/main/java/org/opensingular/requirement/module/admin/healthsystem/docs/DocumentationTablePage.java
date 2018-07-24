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

package org.opensingular.requirement.module.admin.healthsystem.docs;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.opensingular.form.STypeComposite;
import org.opensingular.lib.commons.base.SingularException;
import org.opensingular.requirement.module.form.SingularServerSpringTypeLoader;

import javax.inject.Inject;
import java.io.CharArrayWriter;

public class DocumentationTablePage extends WebPage {

    @Inject
    private SingularServerSpringTypeLoader typeLoader;

    private Class<? extends STypeComposite> stypeClass;
    private boolean excel;

    public DocumentationTablePage(Class<? extends STypeComposite> stypeClass, boolean excel) {
        this.stypeClass = stypeClass;
        this.excel = excel;
    }

    @Override
    protected void onConfigure() {
        super.onConfigure();
        WebMarkupContainer container = new WebMarkupContainer("output") {
            @Override
            public void onComponentTagBody(MarkupStream markupStream, ComponentTag openTag) {
                try {
                    CharArrayWriter baos = new CharArrayWriter(0);
                    DocumentationDefinitionResolver.get().renderDocumentationHTML(typeLoader.loadTypeOrException(stypeClass), baos);
                    replaceComponentTagBody(markupStream, openTag, baos.toString());
                } catch (Exception e) {
                    throw SingularException.rethrow(e.getMessage(), e);
                }
            }
        };
        queue(container);
    }
}
