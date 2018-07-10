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

package org.opensingular.requirement.commons.admin.healthsystem.docs;

import org.junit.Assert;
import org.junit.Test;
import org.opensingular.form.SDictionary;
import org.opensingular.requirement.module.admin.healthsystem.docs.presentation.DefaultDocumentationDefinition;

import java.io.CharArrayWriter;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DefaultFormDocumentationColumnRendererTest {

    public static final Charset CHARSET = Charset.forName("UTF-8");

    private CharArrayWriter getTestWriter() {
        CharArrayWriter charArrayWriter = new CharArrayWriter();
        charArrayWriter.append("<meta charset=\"UTF-8\"><style>td {border: solid black 1px; text-align: center;} tr {border: solid black 1px;} table { width: 95%; border: solid black 1px; margin-bottom: 20px; border-collapse: collapse;}</style>");
        charArrayWriter.flush();
        return charArrayWriter;
    }


    @Test
    public void testOutputTables() throws Exception {
        SDictionary dictionary = SDictionary.create();
        STypeDocRootCompositeTabSample sampleSType = dictionary.getType(STypeDocRootCompositeTabSample.class);

        CharArrayWriter caw = getTestWriter();
        try (CharArrayWriter caw2 = caw) {
            DefaultDocumentationDefinition documentationDefinitionSample = new DefaultDocumentationDefinition();
            documentationDefinitionSample.getRenderer().renderTables(sampleSType, documentationDefinitionSample.getColumns(), caw2);
        }
        Pattern pattern = Pattern.compile("</table>");
        Matcher matcher = pattern.matcher(caw.toString());
        int count = 0;
        while (matcher.find()) {
            count++;
        }
        Assert.assertEquals(4, count);

    }


}
