package org.opensingular.server.p.commons;

import freemarker.cache.StringTemplateLoader;
import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;
import org.opensingular.form.SDictionary;
import org.opensingular.form.SIComposite;
import org.opensingular.form.STypeComposite;
import org.opensingular.form.type.core.STypeString;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class PServerFreeMarkerUtilTest {

    StringTemplateLoader stringTemplateLoader;
    PServerFreeMarkerUtil pServerFreeMarkerUtil;

    @Before
    public void setUp() throws Exception {
        stringTemplateLoader = new StringTemplateLoader();
        pServerFreeMarkerUtil = PServerFreeMarkerUtil.getNewInstance(c -> c.setTemplateLoader(stringTemplateLoader));

    }

    @Test
    public void testWithPojo() throws Exception {
        stringTemplateLoader.putTemplate("testWithPojo", "${dto.myProperty}");
        Map<String, Object> model = new HashMap<>();
        model.put("dto", createDTO("value"));
        assertEquals("value", pServerFreeMarkerUtil.doMergeWithFreemarker("testWithPojo", model));
    }

    @Test
    public void testWithInstance() throws Exception {
        stringTemplateLoader.putTemplate("testWithInstance", "${composite.property}");
        Map<String, Object> model = new HashMap<>();
        model.put("composite",  createCompositeInstance("value"));
        assertEquals("value", pServerFreeMarkerUtil.doMergeWithFreemarker("testWithInstance", model));
    }


    @Test
    public void testWithInstanceAndPojo() throws Exception {
        stringTemplateLoader.putTemplate("testWithInstanceAndPojo", "${myDTO.myProperty} - ${composite.property}");
        Map<String, Object> model = new HashMap<>();
        model.put("composite",  createCompositeInstance("INSTANCE"));
        model.put("myDTO", createDTO("POJO"));
        assertEquals("POJO - INSTANCE", pServerFreeMarkerUtil.doMergeWithFreemarker("testWithInstanceAndPojo", model));
    }

    @NotNull
    private PServerFreeMarkerUtilTest.MyDTO createDTO(String myPropertyFieldValue) {
        MyDTO myDTO = new MyDTO();
        myDTO.myProperty = myPropertyFieldValue;
        return myDTO;
    }

    @NotNull
    private SIComposite createCompositeInstance(String propertyFieldValue) {
        STypeComposite<SIComposite> composite = SDictionary.create().createNewPackage("foo.bar").createCompositeType("composite");
        composite.addField("property", STypeString.class);
        SIComposite siComposite = composite.newInstance();
        siComposite.setValue("property", propertyFieldValue);
        return siComposite;
    }

    public static class MyDTO {
        private String myProperty;
        public String getMyProperty() {
            return myProperty;
        }
        public void setMyProperty(String myProperty) {
            this.myProperty = myProperty;
        }
    }
}