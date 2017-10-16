package org.opensingular.server.p.commons.admin.healthsystem.docs;

import org.opensingular.form.SType;
import org.opensingular.lib.commons.util.Loggable;

import java.io.Serializable;

public class DocFieldMetadata implements Serializable, Loggable {

    private SType<?> rootType;
    private SType<?> type;
    private String fieldName;
    private String enabled;
    private String fieldSize;
    private String docTypeAbbreviation;
    private String required;
    private String generalInformation;
    private String validationMessages;
    private String businessRules;

    public DocFieldMetadata() {

    }

    public DocFieldMetadata(SType<?> rootType, SType<?> type, String fieldName, String enabled, String fieldSize, String docTypeAbbreviation, String required, String generalInformation, String validationMessages, String businessRules) {
        this.rootType = rootType;
        this.type = type;
        this.fieldName = fieldName;
        this.enabled = enabled;
        this.fieldSize = fieldSize;
        this.docTypeAbbreviation = docTypeAbbreviation;
        this.required = required;
        this.generalInformation = generalInformation;
        this.validationMessages = validationMessages;
        this.businessRules = businessRules;
    }


    public String isRequired() {
        return required;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String isEnabled() {
        return enabled;
    }

    public String getFieldSize() {
        return fieldSize;
    }

    public String getFieldTypeAbbreviation() {
        return docTypeAbbreviation;
    }

    public String getGeneralInformation() {
        return generalInformation;
    }

    public String getBusinessRules() {
        return businessRules;
    }

    public String getMessages() {
        return validationMessages;
    }

    public SType<?> getRootType() {
        return rootType;
    }

    public SType<?> getType() {
        return type;
    }
}
