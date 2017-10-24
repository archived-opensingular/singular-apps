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

import org.junit.Assert;
import org.junit.Test;
import org.opensingular.form.SDictionary;
import org.opensingular.form.SType;

import java.util.LinkedHashSet;

import static org.opensingular.server.p.commons.admin.healthsystem.docs.DocFieldMetadata.DocFieldValue.*;
public class DocBuilderTest {

    private DocumentationMetadataBuilder builderFor(Class<? extends SType<?>> clazz) {
        SDictionary dictionary = SDictionary.create();
        SType<?> type = dictionary.getType(clazz);
        return new DocumentationMetadataBuilder(type);
    }

    @Test
    public void buildTest() throws Exception {
        LinkedHashSet<DocTable> saida = builderFor(STypeDocRootCompositeTabSample.class).getMetadata();
        for (DocTable docTable : saida) {
            System.out.println("----" + docTable.getName() + "----");
            for (DocBlock docBlock : docTable.getBlockList()) {
                System.out.println("####" + docBlock.getBlockName() + "#####");
                for (DocFieldMetadata fieldMetadata : docBlock.getMetadataList()) {
                    System.out.println("    " + fieldMetadata.getValue(FIELD_NAME) + "    ");
                }
            }
        }


        Assert.assertEquals(4, saida.size());
        Assert.assertEquals(6, saida.stream().flatMap(s -> s.getBlockList().stream()).count());
        Assert.assertEquals(STypeDocRootCompositeTabSample.NOME, saida.toArray(new DocTable[0])[0].getBlockList().toArray(new DocBlock[0])[0].getMetadataList().toArray(new DocFieldMetadata[0])[0].getValue(FIELD_NAME));
        Assert.assertEquals(STypeDocRootCompositeTabSample.ANEXOS, saida.toArray(new DocTable[0])[0].getBlockList().toArray(new DocBlock[0])[0].getMetadataList().toArray(new DocFieldMetadata[0])[1].getValue(FIELD_NAME));
        Assert.assertEquals(STypeDocSample.ANEXINHOS, saida.toArray(new DocTable[0])[1].getBlockList().toArray(new DocBlock[0])[0].getMetadataList().toArray(new DocFieldMetadata[0])[0].getValue(FIELD_NAME));
        Assert.assertEquals(STypeDocSample.ANEXINHO, saida.toArray(new DocTable[0])[1].getBlockList().toArray(new DocBlock[0])[0].getMetadataList().toArray(new DocFieldMetadata[0])[1].getValue(FIELD_NAME));
        Assert.assertEquals(STypeDocSample.IDADE, saida.toArray(new DocTable[0])[1].getBlockList().toArray(new DocBlock[0])[1].getMetadataList().toArray(new DocFieldMetadata[0])[0].getValue(FIELD_NAME));
        Assert.assertEquals("CEP", saida.toArray(new DocTable[0])[2].getBlockList().toArray(new DocBlock[0])[0].getMetadataList().toArray(new DocFieldMetadata[0])[1].getValue(FIELD_NAME));
        Assert.assertEquals("Logradouro", saida.toArray(new DocTable[0])[2].getBlockList().toArray(new DocBlock[0])[0].getMetadataList().toArray(new DocFieldMetadata[0])[2].getValue(FIELD_NAME));
        Assert.assertEquals("Número", saida.toArray(new DocTable[0])[2].getBlockList().toArray(new DocBlock[0])[0].getMetadataList().toArray(new DocFieldMetadata[0])[3].getValue(FIELD_NAME));
        Assert.assertEquals("Complemento", saida.toArray(new DocTable[0])[2].getBlockList().toArray(new DocBlock[0])[0].getMetadataList().toArray(new DocFieldMetadata[0])[4].getValue(FIELD_NAME));
        Assert.assertEquals("Bairro", saida.toArray(new DocTable[0])[2].getBlockList().toArray(new DocBlock[0])[0].getMetadataList().toArray(new DocFieldMetadata[0])[5].getValue(FIELD_NAME));
        Assert.assertEquals("Cidade", saida.toArray(new DocTable[0])[2].getBlockList().toArray(new DocBlock[0])[0].getMetadataList().toArray(new DocFieldMetadata[0])[6].getValue(FIELD_NAME));
        Assert.assertEquals("Estado", saida.toArray(new DocTable[0])[2].getBlockList().toArray(new DocBlock[0])[0].getMetadataList().toArray(new DocFieldMetadata[0])[7].getValue(FIELD_NAME));
        Assert.assertEquals("País", saida.toArray(new DocTable[0])[2].getBlockList().toArray(new DocBlock[0])[0].getMetadataList().toArray(new DocFieldMetadata[0])[8].getValue(FIELD_NAME));
    }



}
