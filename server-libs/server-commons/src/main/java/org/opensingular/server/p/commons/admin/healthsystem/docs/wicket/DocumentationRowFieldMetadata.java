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
