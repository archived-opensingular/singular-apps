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

package org.opensingular.server.p.commons.admin.healthsystem.docs.wicket;

import org.opensingular.server.p.commons.admin.healthsystem.docs.DocFieldMetadata;

public class DocumentationRowFieldMetadata implements DocumentationRow {

    private String fieldName;
    private String enabled;
    private String fieldSize;
    private String docTypeAbbreviation;
    private String required;
    private String generalInformation;
    private String validationMessages;
    private String businessRules;

    public DocumentationRowFieldMetadata() {
    }

    public DocumentationRowFieldMetadata(String fieldName) {
        this.fieldName = fieldName;
    }

    public DocumentationRowFieldMetadata(DocFieldMetadata docFieldMetadata) {
        this.fieldName = docFieldMetadata.getFieldName();
        this.enabled = docFieldMetadata.isEnabled();
        this.fieldSize = docFieldMetadata.getFieldSize();
        this.docTypeAbbreviation = docFieldMetadata.getFieldTypeAbbreviation();
        this.required = docFieldMetadata.isRequired();
        this.generalInformation = docFieldMetadata.getGeneralInformation();
        this.validationMessages = docFieldMetadata.getMessages();
        this.businessRules = docFieldMetadata.getBusinessRules();
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getEnabled() {
        return enabled;
    }

    public String getFieldSize() {
        return fieldSize;
    }

    public String getDocTypeAbbreviation() {
        return docTypeAbbreviation;
    }

    public String getRequired() {
        return required;
    }

    public String getGeneralInformation() {
        return generalInformation;
    }

    public String getValidationMessages() {
        return validationMessages;
    }

    public String getBusinessRules() {
        return businessRules;
    }
}
