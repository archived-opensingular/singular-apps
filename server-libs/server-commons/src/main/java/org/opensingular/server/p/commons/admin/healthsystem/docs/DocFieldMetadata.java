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

import org.opensingular.form.SType;
import org.opensingular.lib.commons.util.Loggable;

import java.util.List;
import java.util.TreeSet;

public class DocFieldMetadata implements Loggable {

    private SType<?> rootType;
    private SType<?> type;
    private String fieldName;
    private String subtitle;
    private boolean required;
    private boolean enabled;
    private boolean hasEnablingRule;
    private boolean hasValidationRule;
    private boolean hasRequiredRule;
    private boolean hasVisibilityRule;
    private boolean hasUpdateListener;
    private TreeSet<String> dependentFieldsDescription;
    private String mask;
    private Integer maxSize;
    private Integer minSize;
    private List<String> enumSelectOptions;
    private HTMLComponentType componentType;
    private Integer fieldLength;
    private Long maxUploadSizeInBytes;

    public DocFieldMetadata() {

    }

    public DocFieldMetadata(SType<?> rootType, SType<?> type, String fieldName, String subtitle, boolean required, boolean enabled,
                            boolean hasEnablingRule, boolean hasValidationRule, boolean hasRequiredRule,
                            boolean hasVisibilityRule, boolean hasUpdateListener, TreeSet<String> dependentFieldsDescription, String mask,
                            Integer maxSize, Integer minSize, List<String> enumSelectOptions,
                            HTMLComponentType componentType, Integer fieldLength, Long maxUploadSizeInBytes) {
        this.rootType = rootType;
        this.type = type;
        this.fieldName = fieldName;
        this.subtitle = subtitle;
        this.required = required;
        this.enabled = enabled;
        this.hasEnablingRule = hasEnablingRule;
        this.hasValidationRule = hasValidationRule;
        this.hasRequiredRule = hasRequiredRule;
        this.hasVisibilityRule = hasVisibilityRule;
        this.hasUpdateListener = hasUpdateListener;
        this.dependentFieldsDescription = dependentFieldsDescription;
        this.mask = mask;
        this.maxSize = maxSize;
        this.minSize = minSize;
        this.enumSelectOptions = enumSelectOptions;
        this.componentType = componentType;
        this.fieldLength = fieldLength;
        this.maxUploadSizeInBytes = maxUploadSizeInBytes;
    }


    public TreeSet<String> getDependentFieldsDescription() {
        return dependentFieldsDescription;
    }

    public SType<?> getRootType() {
        return rootType;
    }

    public SType<?> getType() {
        return type;
    }

    public String getFieldName() {
        return fieldName;
    }

    public boolean isRequired() {
        return required;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isHasEnablingRule() {
        return hasEnablingRule;
    }

    public boolean isHasValidationRule() {
        return hasValidationRule;
    }

    public boolean isHasRequiredRule() {
        return hasRequiredRule;
    }

    public boolean isHasVisibilityRule() {
        return hasVisibilityRule;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getMask() {
        return mask;
    }

    public Integer getMaxSize() {
        return maxSize;
    }

    public Integer getMinSize() {
        return minSize;
    }

    public List<String> getEnumSelectOptions() {
        return enumSelectOptions;
    }

    public HTMLComponentType getComponentType() {
        return componentType;
    }

    public Integer getFieldLength() {
        return fieldLength;
    }

    public Long getMaxUploadSizeInBytes() {
        return maxUploadSizeInBytes;
    }

    public boolean isHasUpdateListener() {
        return hasUpdateListener;
    }

}
