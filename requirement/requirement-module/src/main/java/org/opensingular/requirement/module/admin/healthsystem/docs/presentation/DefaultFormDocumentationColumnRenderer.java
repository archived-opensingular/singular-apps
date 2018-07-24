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

package org.opensingular.requirement.module.admin.healthsystem.docs.presentation;

import com.google.common.base.Joiner;
import org.apache.commons.lang3.StringUtils;
import org.opensingular.requirement.module.admin.healthsystem.docs.DocBlock;
import org.opensingular.requirement.module.admin.healthsystem.docs.DocFieldMetadata;
import org.opensingular.requirement.module.admin.healthsystem.docs.DocTable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Define a documentation column. One documentation column can display multiple metadata information
 */
public class DefaultFormDocumentationColumnRenderer implements FormDocumentationColumnRenderer {


    private String columnName;
    private LinkedHashSet<DocFieldMetadata.DocFieldValue<?>> fieldValues     = new LinkedHashSet<>();
    private String                                           fieldsSeparator = "<BR>";
    private FormFieldValueConverter                          converter       = MessagesValueConverter.getDefault();

    public DefaultFormDocumentationColumnRenderer(String columnName, DocFieldMetadata.DocFieldValue<?>... fieldValues) {
        this.columnName = columnName;
        this.fieldValues.addAll(Arrays.asList(fieldValues));
    }

    public DefaultFormDocumentationColumnRenderer(String columnName, FormFieldValueConverter converter, DocFieldMetadata.DocFieldValue<?>... fieldValues) {
        this.columnName = columnName;
        this.converter = converter;
        this.fieldValues.addAll(Arrays.asList(fieldValues));
    }


    public DefaultFormDocumentationColumnRenderer(String columnName, String fieldsSeparator, DocFieldMetadata.DocFieldValue<?>... fieldValues) {
        this.columnName = columnName;
        this.fieldsSeparator = fieldsSeparator;
        this.fieldValues.addAll(Arrays.asList(fieldValues));
    }

    public DefaultFormDocumentationColumnRenderer(String columnName, String fieldsSeparator, FormFieldValueConverter converter, DocFieldMetadata.DocFieldValue<?>... fieldValues) {
        this.columnName = columnName;
        this.fieldsSeparator = fieldsSeparator;
        this.converter = converter;
        this.fieldValues.addAll(Arrays.asList(fieldValues));
    }

    public String getColumnName() {
        return columnName;
    }

    protected LinkedHashSet<DocFieldMetadata.DocFieldValue<?>> getFieldValues() {
        return fieldValues;
    }

    protected String getFieldsSeparator() {
        return fieldsSeparator;
    }

    protected FormFieldValueConverter getConverter() {
        return converter;
    }

    /**
     * Render column using the given converter and separator
     * this method can be override for further customization
     *
     * @param fieldMetadata
     * @return
     */
    public String renderColumn(DocTable table, DocBlock block, DocFieldMetadata fieldMetadata) {
        List<String> strings = new ArrayList<>(3);
        for (DocFieldMetadata.DocFieldValue<?> docFieldValue : getFieldValues()) {
            strings.add(StringUtils.defaultString(getConverter().format(docFieldValue, fieldMetadata.getValue(docFieldValue)),""));
        }
        return Joiner.on(getFieldsSeparator()).join(strings.stream().filter(StringUtils::isNotBlank).collect(Collectors.toList()));
    }
}
