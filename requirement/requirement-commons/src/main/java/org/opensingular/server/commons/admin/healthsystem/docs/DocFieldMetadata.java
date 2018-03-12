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

import org.opensingular.form.SType;
import org.opensingular.lib.commons.util.Loggable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public class DocFieldMetadata implements Loggable {

    public static class DocFieldValue<T> {

        public static final DocFieldValue<SType> ROOT_STYPE = new DocFieldValue<>(SType.class, "ROOT_STYPE");

        public static final DocFieldValue<SType> STYPE = new DocFieldValue<>(SType.class, "STYPE");

        public static final DocFieldValue<String> FIELD_NAME = new DocFieldValue<>(String.class, "FIELD_NAME");

        public static final DocFieldValue<String> FIELD_SUBTITLE = new DocFieldValue<>(String.class, "FIELD_SUBTITLE");

        public static final DocFieldValue<Boolean> REQUIRED = new DocFieldValue<>(Boolean.class, "REQUIRED");

        public static final DocFieldValue<Boolean> ENABLED = new DocFieldValue<>(Boolean.class, "ENABLED");

        public static final DocFieldValue<Boolean> HAS_ENABLING_RULE = new DocFieldValue<>(Boolean.class, "HAS_ENABLING_RULE");

        public static final DocFieldValue<Boolean> HAS_VALIDATION_RULE = new DocFieldValue<>(Boolean.class, "HAS_VALIDATION_RULE");

        public static final DocFieldValue<Boolean> HAS_VISIBILITY_RULE = new DocFieldValue<>(Boolean.class, "HAS_VISIBILITY_RULE");

        public static final DocFieldValue<Boolean> HAS_REQUIRED_RULE = new DocFieldValue<>(Boolean.class, "HAS_REQUIRED_RULE");

        public static final DocFieldValue<Boolean> HAS_UPDATE_LISTENER = new DocFieldValue<>(Boolean.class, "HAS_UPDATE_LISTENER");

        public static final DocFieldValue<TreeSet> DEPENDENT_STYPES_FIELDS_NAME = new DocFieldValue<>(TreeSet.class, "DEPENDENT_STYPES_FIELDS_NAME");

        public static final DocFieldValue<String> MASK = new DocFieldValue<>(String.class, "MASK");

        public static final DocFieldValue<Integer> MAX_SIZE = new DocFieldValue<>(Integer.class, "MAX_SIZE");

        public static final DocFieldValue<Integer> MIN_SIZE = new DocFieldValue<>(Integer.class, "MIN_SIZE");

        public static final DocFieldValue<List> ENUM_SELECTION_OPTIONS = new DocFieldValue<>(List.class, "ENUM_SELECTION_OPTIONS");

        public static final DocFieldValue<HTMLComponentType> HTML_COMPONENT_TYPE = new DocFieldValue<>(HTMLComponentType.class, "HTML_COMPONENT_TYPE");

        public static final DocFieldValue<Integer> FIELD_LENGTH = new DocFieldValue<>(Integer.class, "FIELD_LENGTH");

        public static final DocFieldValue<Long> MAX_UPLOAD_SIZE_IN_BYTES = new DocFieldValue<>(Long.class, "MAX_UPLOAD_SIZE_IN_BYTES");


        private Class<T> valueType;
        private String key;

        private DocFieldValue(Class<T> valueType, String key) {
            this.valueType = valueType;
            this.key = key;

        }

        public Class<T> getColumnType() {
            return valueType;
        }

        public String getKey() {
            return key;
        }
    }


    private Map<DocFieldValue, Object> values = new HashMap<>();


    public DocFieldMetadata() {

    }

    public DocFieldMetadata(SType<?> rootType, SType<?> type, String fieldName, String subtitle, boolean required, boolean enabled,
                            boolean hasEnablingRule, boolean hasValidationRule, boolean hasRequiredRule,
                            boolean hasVisibilityRule, boolean hasUpdateListener, TreeSet<String> dependentFieldsDescription, String mask,
                            Integer maxSize, Integer minSize, List<String> enumSelectOptions,
                            HTMLComponentType componentType, Integer fieldLength, Long maxUploadSizeInBytes) {
        this.values.put(DocFieldValue.ROOT_STYPE, rootType);
        this.values.put(DocFieldValue.STYPE, type);
        this.values.put(DocFieldValue.FIELD_NAME, fieldName);
        this.values.put(DocFieldValue.FIELD_SUBTITLE, subtitle);
        this.values.put(DocFieldValue.REQUIRED, required);
        this.values.put(DocFieldValue.ENABLED, enabled);
        this.values.put(DocFieldValue.HAS_ENABLING_RULE, hasEnablingRule);
        this.values.put(DocFieldValue.HAS_VALIDATION_RULE, hasValidationRule);
        this.values.put(DocFieldValue.HAS_REQUIRED_RULE, hasRequiredRule);
        this.values.put(DocFieldValue.HAS_VISIBILITY_RULE, hasVisibilityRule);
        this.values.put(DocFieldValue.HAS_UPDATE_LISTENER, hasUpdateListener);
        this.values.put(DocFieldValue.DEPENDENT_STYPES_FIELDS_NAME, dependentFieldsDescription);
        this.values.put(DocFieldValue.MASK, mask);
        this.values.put(DocFieldValue.MAX_SIZE, maxSize);
        this.values.put(DocFieldValue.MIN_SIZE, minSize);
        this.values.put(DocFieldValue.ENUM_SELECTION_OPTIONS, enumSelectOptions);
        this.values.put(DocFieldValue.HTML_COMPONENT_TYPE, componentType);
        this.values.put(DocFieldValue.FIELD_LENGTH, fieldLength);
        this.values.put(DocFieldValue.MAX_UPLOAD_SIZE_IN_BYTES, maxUploadSizeInBytes);
    }

    public <T> Object getValue(DocFieldValue<T> field) {
        return values.get(field);
    }

}
