package org.opensingular.server.p.commons.admin.healthsystem.docs;

import org.junit.Assert;
import org.junit.Test;
import org.opensingular.form.SDictionary;
import org.opensingular.form.SType;
import org.opensingular.server.p.commons.admin.healthsystem.docs.wicket.DocumentationRow;
import org.opensingular.server.p.commons.admin.healthsystem.docs.wicket.DocumentationRowBlockSeparator;
import org.opensingular.server.p.commons.admin.healthsystem.docs.wicket.DocumentationRowFieldMetadata;

import java.util.LinkedHashSet;
import java.util.List;

public class DocBuilderTest {

    private DocumentationMetadataBuilder builderFor(Class<? extends SType<?>> clazz) {
        SDictionary dictionary = SDictionary.create();
        SType<?> type = dictionary.getType(clazz);
        return new DocumentationMetadataBuilder(type);
    }

    @Test
    public void buildTest() throws Exception {
        LinkedHashSet<DocTable> saida = builderFor(STypeDocRootCompositeTabTest.class).getMetadata();
        for (DocTable docTable : saida) {
            System.out.println("----" + docTable.getName() + "----");
            for (DocBlock docBlock : docTable.getBlockList()) {
                System.out.println("####" + docBlock.getBlockName() + "#####");
                for (DocFieldMetadata fieldMetadata : docBlock.getMetadataList()) {
                    System.out.println("    " + fieldMetadata.getFieldName() + "    ");
                }
            }
        }


        Assert.assertEquals(2, saida.size());
        Assert.assertEquals(3, saida.stream().flatMap(s -> s.getBlockList().stream()).count());
        Assert.assertEquals(STypeDocTest.ANEXINHOS, saida.toArray(new DocTable[0])[0].getBlockList().toArray(new DocBlock[0])[0].getMetadataList().toArray(new DocFieldMetadata[0])[0].getFieldName());
        Assert.assertEquals(STypeDocTest.ANEXINHO, saida.toArray(new DocTable[0])[0].getBlockList().toArray(new DocBlock[0])[0].getMetadataList().toArray(new DocFieldMetadata[0])[1].getFieldName());
        Assert.assertEquals(STypeDocTest.IDADE, saida.toArray(new DocTable[0])[0].getBlockList().toArray(new DocBlock[0])[1].getMetadataList().toArray(new DocFieldMetadata[0])[0].getFieldName());
        Assert.assertEquals(STypeDocTest.ENDERECOS, saida.toArray(new DocTable[0])[0].getBlockList().toArray(new DocBlock[0])[1].getMetadataList().toArray(new DocFieldMetadata[0])[1].getFieldName());
        Assert.assertEquals("CEP", saida.toArray(new DocTable[0])[1].getBlockList().toArray(new DocBlock[0])[0].getMetadataList().toArray(new DocFieldMetadata[0])[0].getFieldName());
        Assert.assertEquals("Logradouro", saida.toArray(new DocTable[0])[1].getBlockList().toArray(new DocBlock[0])[0].getMetadataList().toArray(new DocFieldMetadata[0])[1].getFieldName());
        Assert.assertEquals("Número", saida.toArray(new DocTable[0])[1].getBlockList().toArray(new DocBlock[0])[0].getMetadataList().toArray(new DocFieldMetadata[0])[2].getFieldName());
        Assert.assertEquals("Complemento", saida.toArray(new DocTable[0])[1].getBlockList().toArray(new DocBlock[0])[0].getMetadataList().toArray(new DocFieldMetadata[0])[3].getFieldName());
        Assert.assertEquals("Bairro", saida.toArray(new DocTable[0])[1].getBlockList().toArray(new DocBlock[0])[0].getMetadataList().toArray(new DocFieldMetadata[0])[4].getFieldName());
        Assert.assertEquals("Cidade", saida.toArray(new DocTable[0])[1].getBlockList().toArray(new DocBlock[0])[0].getMetadataList().toArray(new DocFieldMetadata[0])[5].getFieldName());
        Assert.assertEquals("Estado", saida.toArray(new DocTable[0])[1].getBlockList().toArray(new DocBlock[0])[0].getMetadataList().toArray(new DocFieldMetadata[0])[6].getFieldName());
        Assert.assertEquals("País", saida.toArray(new DocTable[0])[1].getBlockList().toArray(new DocBlock[0])[0].getMetadataList().toArray(new DocFieldMetadata[0])[7].getFieldName());


    }

    @Test
    public void streamLinedTest() throws Exception {
        List<DocumentationMetadataBuilder.StreamLinedMetadata> meta = builderFor(STypeDocRootCompositeTabTest.class).getStreamLinedMetadata();

        for (DocumentationMetadataBuilder.StreamLinedMetadata lines : meta) {
            System.out.println("------------" + lines.getTableName());
            for (DocumentationRow documentationRow : lines.getDocumentationRows()) {
                if (documentationRow instanceof DocumentationRowBlockSeparator) {
                    System.out.println("-" + ((DocumentationRowBlockSeparator) documentationRow).getBlockName());
                } else if (documentationRow instanceof DocumentationRowFieldMetadata) {
                    System.out.println("--" + ((DocumentationRowFieldMetadata) documentationRow).getFieldName());
                }
            }
        }
    }
}
