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

package org.opensingular.server.commons.admin.healthsystem.docs.presentation;

import org.apache.commons.lang3.StringUtils;
import org.opensingular.server.commons.admin.healthsystem.docs.DocFieldMetadata;
import org.opensingular.server.commons.admin.healthsystem.docs.DocTable;
import org.opensingular.server.commons.admin.healthsystem.docs.DocBlock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultDocumentationDefinition implements DocumentationDefinition {


    @Override
    public FormDocumentationRenderer getRenderer() {
        return new DefaultFormDocumentationRenderer();
    }

    @Override
    public List<FormDocumentationColumnRenderer> getColumns() {
        List<FormDocumentationColumnRenderer> columns = new ArrayList<>();
        columns.add(newNumberPerTableColumnRenderer());
        columns.add(new DefaultFormDocumentationColumnRenderer("Nome do Campo", DocFieldMetadata.DocFieldValue.FIELD_NAME));
        columns.add(new DefaultFormDocumentationColumnRenderer("Tipo", DocFieldMetadata.DocFieldValue.HTML_COMPONENT_TYPE));
        columns.add(newAlternativeColumnRenderer("Obrigatório", getEnablingRuleConverter(DocFieldMetadata.DocFieldValue.HAS_REQUIRED_RULE), DocFieldMetadata.DocFieldValue.HAS_REQUIRED_RULE, DocFieldMetadata.DocFieldValue.REQUIRED));
        columns.add(newAlternativeColumnRenderer("Habilitado", getEnablingRuleConverter(DocFieldMetadata.DocFieldValue.HAS_ENABLING_RULE), DocFieldMetadata.DocFieldValue.HAS_ENABLING_RULE, DocFieldMetadata.DocFieldValue.ENABLED));
        columns.add(newAlternativeColumnRenderer("Tamanho", getFieldSizeConverter(), DocFieldMetadata.DocFieldValue.MAX_UPLOAD_SIZE_IN_BYTES, DocFieldMetadata.DocFieldValue.FIELD_LENGTH));
        columns.add(new DefaultFormDocumentationColumnRenderer("Regras", DocFieldMetadata.DocFieldValue.HAS_ENABLING_RULE, DocFieldMetadata.DocFieldValue.HAS_REQUIRED_RULE, DocFieldMetadata.DocFieldValue.HAS_UPDATE_LISTENER, DocFieldMetadata.DocFieldValue.HAS_VISIBILITY_RULE));
        columns.add(new DefaultFormDocumentationColumnRenderer("Mensagens", DocFieldMetadata.DocFieldValue.HAS_VALIDATION_RULE));
        columns.add(new DefaultFormDocumentationColumnRenderer("Domínio / Máscara / Hint / Demais observações", DocFieldMetadata.DocFieldValue.ENUM_SELECTION_OPTIONS, DocFieldMetadata.DocFieldValue.MASK, DocFieldMetadata.DocFieldValue.FIELD_SUBTITLE, DocFieldMetadata.DocFieldValue.DEPENDENT_STYPES_FIELDS_NAME, DocFieldMetadata.DocFieldValue.MAX_SIZE, DocFieldMetadata.DocFieldValue.MIN_SIZE));
        return columns;
    }

    private FormFieldValueConverter getEnablingRuleConverter(DocFieldMetadata.DocFieldValue<?> toOverride) {
        return new DefaultValueConverter() {
            @Override
            public String format(DocFieldMetadata.DocFieldValue<?> fieldValue, Object value) {
                if (fieldValue.equals(toOverride)) {
                    if ((Boolean)value) {
                        return "Sim";
                    }
                }
                return MessagesValueConverter.getDefault().format(fieldValue, value);
            }
        };
    }

    private FormDocumentationColumnRenderer newNumberPerTableColumnRenderer() {
        return new DefaultFormDocumentationColumnRenderer("Número") {
            private Map<DocTable, Integer> map = new HashMap<>();

            @Override
            public String renderColumn(DocTable table, DocBlock block, DocFieldMetadata fieldMetadata) {
                map.putIfAbsent(table, 1);
                int count = map.get(table);
                if (fieldMetadata != null) {
                    map.put(table, count + 1);
                    return String.valueOf(count);
                } else {
                    return block.getBlockName();
                }
            }
        };
    }

    private FormDocumentationColumnRenderer newAlternativeColumnRenderer(String label, FormFieldValueConverter converter, DocFieldMetadata.DocFieldValue<?> mainValue, DocFieldMetadata.DocFieldValue<?> secondaryValue) {
        return new DefaultFormDocumentationColumnRenderer(label, converter, mainValue, secondaryValue) {
            @Override
            public String renderColumn(DocTable table, DocBlock block, DocFieldMetadata fieldMetadata) {
                String result = null;
                if (fieldMetadata.getValue(mainValue) != null) {
                    result = StringUtils.defaultString(getConverter().format(mainValue, fieldMetadata.getValue(mainValue)), "");
                }
                if (StringUtils.isBlank(result)) {
                    result = StringUtils.defaultString(getConverter().format(secondaryValue, fieldMetadata.getValue(secondaryValue)), "");
                }
                return result;
            }
        };
    }


    private FormFieldValueConverter getFieldSizeConverter() {
        return new DefaultValueConverter() {
            @Override
            public String format(DocFieldMetadata.DocFieldValue<?> fieldValue, Object value) {
                String result;
                if (fieldValue.equals(DocFieldMetadata.DocFieldValue.MAX_UPLOAD_SIZE_IN_BYTES) && value != null) {
                    result = (Long) value / (1024 * 1024) + "MB";
                } else {
                    result = super.format(fieldValue, value);
                }
                if (StringUtils.isBlank(result)) {
                    result = "N/A";
                }
                return result;
            }
        };
    }

}
