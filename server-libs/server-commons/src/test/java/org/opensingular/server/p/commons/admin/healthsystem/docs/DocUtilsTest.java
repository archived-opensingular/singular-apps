package org.opensingular.server.p.commons.admin.healthsystem.docs;

import org.junit.Assert;
import org.junit.Test;
import org.opensingular.form.SDictionary;
import org.opensingular.server.p.commons.admin.healthsystem.DocumentationMetadataUtil;

public class DocUtilsTest {

    @Test
    public void testDocumentationVisibility() throws Exception {
        SDictionary dictionary = SDictionary.create();
        STypeUtilsSample type = dictionary.getType(STypeUtilsSample.class);

        Assert.assertTrue(DocumentationMetadataUtil.isHiddenForDocumentation(type.definicao));
        Assert.assertTrue(DocumentationMetadataUtil.isHiddenForDocumentation(type.idade));
        Assert.assertTrue(DocumentationMetadataUtil.isHiddenForDocumentation(type.nome));
        Assert.assertTrue(DocumentationMetadataUtil.isHiddenForDocumentation(type.observacoes));
        Assert.assertTrue(DocumentationMetadataUtil.isHiddenForDocumentation(type.observacoesOcultas));
        Assert.assertFalse(DocumentationMetadataUtil.isHiddenForDocumentation(type.visivel));

    }
}
