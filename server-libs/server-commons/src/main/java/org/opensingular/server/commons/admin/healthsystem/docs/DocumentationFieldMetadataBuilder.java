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

package org.opensingular.server.commons.admin.healthsystem.docs;

import org.apache.commons.lang3.StringUtils;
import org.opensingular.form.SType;
import org.opensingular.form.STypeAttachmentList;
import org.opensingular.form.STypeSimple;
import org.opensingular.form.converter.EnumSInstanceConverter;
import org.opensingular.form.provider.ProviderContext;
import org.opensingular.form.type.basic.SPackageBasic;
import org.opensingular.form.type.core.STypeDate;
import org.opensingular.form.type.core.STypeDateTime;
import org.opensingular.form.type.core.STypeTime;
import org.opensingular.form.type.core.attachment.STypeAttachment;
import org.opensingular.form.view.SMultiSelectionByCheckboxView;
import org.opensingular.form.view.SMultiSelectionByPicklistView;
import org.opensingular.form.view.SView;
import org.opensingular.form.view.SViewListByMasterDetail;
import org.opensingular.form.view.ViewResolver;
import org.opensingular.form.wicket.behavior.InputMaskBehavior;
import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.server.commons.admin.healthsystem.DocumentationMetadataUtil;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.opensingular.server.commons.admin.healthsystem.DocumentationMetadataUtil.*;

/**
 * Translates some metadatas from {@link SType} to human-readable documentation info.
 * The information is gathered only form form input fields as defined by {{@link #isFormInputField()}}
 */
class DocumentationFieldMetadataBuilder implements Loggable {

    private DocFieldMetadata docFieldMetadata;

    private boolean modal;
    private boolean selection;
    private boolean simple;
    private boolean upload;
    private boolean hiddenForDocumentation;


    public DocumentationFieldMetadataBuilder(SType<?> rootType, SType<?> type) {
        this.modal = initModal(type);
        this.selection = initSelection(type);
        this.simple = initSimpleType(type);
        this.upload = initUpload(type);
        this.hiddenForDocumentation = initHiddenForDocumentation(type);
        if (isFormInputField()) {
            docFieldMetadata = new DocFieldMetadata(
                    rootType,
                    type,
                    initFieldName(type),
                    initFieldSubtitle(type),
                    initRequired(type),
                    initEnabled(type),
                    initEnablingRule(type),
                    initValidationRule(type),
                    initRequiredRule(type),
                    initBusinessRules(type),
                    initVisibilityRule(type),
                    resolveDependsOn(rootType, type),
                    initMask(type),
                    initMaxSize(type),
                    initMinSize(type),
                    initEnumSelectDomain(type),
                    initHtmlComponentType(type),
                    initFieldLength(type),
                    initMaxUploadInBytes(type));
        } else {
            docFieldMetadata = null;
        }
    }

    private boolean initBusinessRules(SType<?> type) {
        return getAttribute(type, SPackageBasic.ATR_UPDATE_LISTENER).isPresent();
    }

    private String initFieldSubtitle(SType<?> type) {
        return StringUtils.defaultString(getAttribute(type, SPackageBasic.ATR_SUBTITLE).orElse(null), null);
    }

    private Long initMaxUploadInBytes(SType<?> type) {
        if (upload) {
            SType<?> uploadType = type;
            if (type instanceof STypeAttachmentList) {
                uploadType = ((STypeAttachmentList) type).getElementsType();
            }
            return getAttribute(uploadType, SPackageBasic.ATR_MAX_FILE_SIZE).orElse(null);
        }
        return null;
    }

    private Integer initFieldLength(SType<?> type) {
        return getAttribute(type, SPackageBasic.ATR_MAX_LENGTH).orElse(null);
    }

    private HTMLComponentType initHtmlComponentType(SType<?> s) {
        if (selection) {
            return resolveSelectionType(s);
        } else if (s instanceof STypeDate) {
            return HTMLComponentType.DATE;
        } else if (s instanceof STypeDateTime) {
            return HTMLComponentType.DATETIME;
        } else if (s instanceof STypeTime) {
            return HTMLComponentType.TIME;
        } else if (s instanceof STypeAttachment) {
            return HTMLComponentType.UPLOAD;
        } else if (s instanceof STypeAttachmentList) {
            return HTMLComponentType.MULTI_UPLOAD;
        } else {
            return HTMLComponentType.INPUT_FIELD;
        }
    }

    private HTMLComponentType resolveSelectionType(SType<?> s) {
        if (s.isList()) {
            SView view = ViewResolver.resolveView(s);
            if (view instanceof SMultiSelectionByCheckboxView) {
                return HTMLComponentType.CHECKBOX;
            } else if (view instanceof SMultiSelectionByPicklistView) {
                return HTMLComponentType.PICK_LIST;
            } else {
                return HTMLComponentType.MULTI_SELECT;
            }
        } else {
            return HTMLComponentType.SELECT;
        }
    }

    @SuppressWarnings("unchecked")
    private List<String> initEnumSelectDomain(SType<?> type) {
        if (selection && type.asAtrProvider().getConverter() instanceof EnumSInstanceConverter) {
            return (List<String>) type
                    .asAtrProvider()
                    .getProvider()
                    .load(new ProviderContext())
                    .stream()
                    .map(e -> type.asAtrProvider().getDisplayFunction().apply((Serializable) e))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    private Integer initMinSize(SType<?> type) {
        return getAttribute(type, SPackageBasic.ATR_MINIMUM_SIZE).orElse(null);
    }

    private Integer initMaxSize(SType<?> type) {
        return getAttribute(type, SPackageBasic.ATR_MAXIMUM_SIZE).orElse(null);
    }

    private String initMask(SType<?> type) {
        return StringUtils.defaultString(getAttribute(type, SPackageBasic.ATR_BASIC_MASK).map(InputMaskBehavior.Masks::valueOf).map(InputMaskBehavior.Masks::getMask).orElse(null), null);
    }

    private Boolean initVisibilityRule(SType<?> type) {
        return getAttribute(type, SPackageBasic.ATR_VISIBLE_FUNCTION).isPresent();
    }

    private Boolean initRequiredRule(SType<?> type) {
        return getAttribute(type, SPackageBasic.ATR_REQUIRED_FUNCTION).isPresent();
    }

    private Boolean initValidationRule(SType<?> type) {
        return !type.getValidators().isEmpty();
    }

    private Boolean initEnablingRule(SType<?> type) {
        return getAttribute(type, SPackageBasic.ATR_ENABLED_FUNCTION).isPresent();
    }

    private String initFieldName(SType<?> type) {
        return getLabelForType(type);
    }

    /**
     * Check if the current {@link SType} represents an input field on the html form.
     * If the Stype is permanently disabled or do not exists ({@link SPackageBasic#ATR_EXISTS})
     * it is not considered a Form Input Field. Note that not only {@link STypeSimple} can be form inputs,
     * some composite are considered atomic form fields, ex: {@link STypeAttachment}, {@link org.opensingular.form.STypeComposite} with {@link org.opensingular.form.view.SViewSelectionBySelect} views etc.
     *
     * @return true if {@link SType}  is considered a form input field, false otherwise
     */
    public boolean isFormInputField() {
        boolean isHtmlFormComponent = selection || upload || simple || modal;
        return !hiddenForDocumentation && isHtmlFormComponent;
    }

    /**
     * @return Object contained all metadatas collected during this builder construction.
     */
    public DocFieldMetadata getDocFieldMetadata() {
        return docFieldMetadata;
    }

    private boolean initModal(SType<?> type) {
        return ViewResolver.resolveView(type) instanceof SViewListByMasterDetail;
    }

    private boolean initHiddenForDocumentation(SType<?> type) {
        return DocumentationMetadataUtil.isHiddenForDocumentation(type);
    }

    private Boolean initRequired(SType<?> s) {
        return getAttribute(s, SPackageBasic.ATR_REQUIRED).orElse(Boolean.FALSE);
    }


    private Boolean initEnabled(SType<?> s) {
        return getAttribute(s, SPackageBasic.ATR_ENABLED).orElse(Boolean.TRUE);
    }


    private boolean initSelection(SType<?> type) {
        return type.asAtrProvider().getProvider() != null;
    }


    private boolean initUpload(SType<?> type) {
        return type instanceof STypeAttachment || type instanceof STypeAttachmentList;
    }

    private boolean initSimpleType(SType<?> type) {
        return type instanceof STypeSimple;
    }

}
