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

package org.opensingular.requirement.module.admin.healthsystem.docs;

import java.io.Writer;

import org.opensingular.form.SType;
import org.opensingular.lib.commons.context.SingularContext;
import org.opensingular.lib.commons.context.SingularSingletonStrategy;
import org.opensingular.lib.commons.scan.SingularClassPathScanner;
import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.requirement.module.admin.healthsystem.docs.presentation.DefaultDocumentationDefinition;
import org.opensingular.requirement.module.admin.healthsystem.docs.presentation.DocumentationDefinition;

public class DocumentationDefinitionResolver implements Loggable {

    private DocumentationDefinition documentationDefinition;

    public static DocumentationDefinitionResolver get(){
        return ((SingularSingletonStrategy) SingularContext.get()).singletonize(DocumentationDefinitionResolver.class, DocumentationDefinitionResolver::new);
    }

    private DocumentationDefinitionResolver() {
        try {
            documentationDefinition = SingularClassPathScanner
                    .get()
                    .findSubclassesOf(DocumentationDefinition.class)
                    .stream()
                    .filter(s -> s.equals(DefaultDocumentationDefinition.class))
                    .findFirst()
                    .orElse(DefaultDocumentationDefinition.class)
                    .newInstance();
        } catch (Exception e) {
            getLogger().error(e.getMessage(), e);
        }
    }

    public void renderDocumentationHTML(SType<?> type, Writer writer) {
        documentationDefinition.getRenderer().renderTables(type, documentationDefinition.getColumns(), writer);
    }
}
