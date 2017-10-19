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

package org.opensingular.server.p.commons.admin.healthsystem.docs;

import com.google.common.base.Joiner;
import org.opensingular.form.SType;
import org.opensingular.form.STypeAttachmentList;
import org.opensingular.form.STypeSimple;
import org.opensingular.form.converter.EnumSInstanceConverter;
import org.opensingular.form.provider.ProviderContext;
import org.opensingular.form.type.basic.SPackageBasic;
import org.opensingular.form.type.core.STypeDate;
import org.opensingular.form.type.core.STypeDateTime;
import org.opensingular.form.type.core.attachment.STypeAttachment;
import org.opensingular.form.type.core.attachment.STypeAttachmentImage;
import org.opensingular.form.view.SMultiSelectionByCheckboxView;
import org.opensingular.form.view.SViewListByMasterDetail;
import org.opensingular.form.view.ViewResolver;
import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.server.p.commons.admin.healthsystem.DocumentationMetadataUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.opensingular.server.p.commons.admin.healthsystem.DocumentationMetadataUtil.*;

/**
 * Translates some metadatas from {@link SType} to human-readable documentation info.
 * The information is gathered only form form input fields as defined by {{@link #isFormInputField()}}
 */
public class DocumentationFieldMetadataBuilder implements Loggable {

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
                    initRequired(type),
                    initEnabled(type),
                    initEnablingRule(type),
                    initValidationRule(type),
                    initRequiredRule(type),
                    initVisibilityRule(type),
                    resolveDependsOn(rootType, type),
                    initMask(type),
                    initMaxSize(type),
                    initMinSize(type),
                    null, null, null, null

                    );
        } else {
            docFieldMetadata = null;
        }
    }

    private Integer initMinSize(SType<?> type) {
        return getAttribute(type, SPackageBasic.ATR_MINIMUM_SIZE).orElse(null);
    }

    private Integer initMaxSize(SType<?> type) {
        return getAttribute(type, SPackageBasic.ATR_MAXIMUM_SIZE).orElse(null);
    }

    private String initMask(SType<?> type) {
        return getAttribute(type, SPackageBasic.ATR_BASIC_MASK).orElse(null);
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

//
//    private String initBusinessRules(SType<?> type) {
//        if (getAttribute(type, SPackageBasic.ATR_UPDATE_LISTENER).isPresent()) {
//            return "Ver regra de negócio";
//        }
//        return EMPTY_VALUE;
//    }

    private boolean initHiddenForDocumentation(SType<?> type) {
        return DocumentationMetadataUtil.isHiddenForDocumentation(type);
    }

    @SuppressWarnings("unchecked")
    private String initGeneralInformation(SType<?> type, SType<?> rootType) {
        List<String> observacoes = new ArrayList<>();
        getAttribute(type, SPackageBasic.ATR_DEPENDS_ON_FUNCTION).ifPresent(v ->
                observacoes.add(" Depende de: " + Joiner.on(", ").join(resolveDependsOn(rootType, type)))
        );
        getAttribute(type, SPackageBasic.ATR_SUBTITLE).ifPresent(v ->
                observacoes.add(" Subtítulo: " + v)
        );

        if (selection && type.asAtrProvider().getConverter() instanceof EnumSInstanceConverter) {
            List<String> dominio = (List<String>) type
                    .asAtrProvider()
                    .getProvider()
                    .load(new ProviderContext())
                    .stream()
                    .map(e -> type.asAtrProvider().getDisplayFunction().apply((Serializable) e))
                    .collect(Collectors.toList());
            observacoes.add("Domínio: " + Joiner.on(", ").join(dominio));
        }
        return Joiner.on(",").join(observacoes);
    }

    private Boolean initRequired(SType<?> s) {
        return getAttribute(s, SPackageBasic.ATR_REQUIRED).orElse(Boolean.FALSE);
    }


    private String initTypeAbbreviation(SType<?> s) {
        if (selection) {
            if (s.isList()) {
                if (ViewResolver.resolveView(s) instanceof SMultiSelectionByCheckboxView) {
                    return "CKB";
                } else {
                    return "CBM";
                }
            } else {
                return "CB";
            }
        } else if (s instanceof STypeDate) {
            return "DT";
        } else if (s instanceof STypeDateTime) {
            return "DH";
        } else if (s instanceof STypeAttachmentImage) {
            return "IM";
        } else if (s instanceof STypeAttachment) {
            return "AN";
        } else if (s instanceof STypeAttachmentList) {
            return "ANM";
        } else {
            return "CT";
        }
    }

    private Boolean initEnabled(SType<?> s) {
        return getAttribute(s, SPackageBasic.ATR_ENABLED).orElse(Boolean.TRUE);
    }

    private String initSize(SType<?> type) {
        if (!selection && initUpload(type)) {
            SType<?> uploadType = type;
            if (type instanceof STypeAttachmentList) {
                uploadType = ((STypeAttachmentList) type).getElementsType();
            }
            Optional<Long> value = getAttribute(uploadType, SPackageBasic.ATR_MAX_FILE_SIZE);
            if (value.isPresent()) {
                return value.get() / (1024 * 1024) + " MB";
            }
        } else {
            Optional<Integer> maxlength = getAttribute(type, SPackageBasic.ATR_MAX_LENGTH);
            if (maxlength.isPresent()) {
                return String.valueOf(maxlength.get());
            }
        }
        return null;
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
