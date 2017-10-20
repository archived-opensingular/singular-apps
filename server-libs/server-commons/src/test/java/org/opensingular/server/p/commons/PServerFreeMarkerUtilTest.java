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

package org.opensingular.server.p.commons;

import freemarker.cache.StringTemplateLoader;
import org.junit.Before;
import org.junit.Test;
import org.opensingular.form.*;
import org.opensingular.form.type.core.STypeString;

import javax.annotation.Nonnull;
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

    @Nonnull
    private PServerFreeMarkerUtilTest.MyDTO createDTO(String myPropertyFieldValue) {
        MyDTO myDTO = new MyDTO();
        myDTO.myProperty = myPropertyFieldValue;
        return myDTO;
    }

    @Nonnull
    private SIComposite createCompositeInstance(String propertyFieldValue) {
        STypeComposite<SIComposite> composite = createRoot();
        composite.addField("property", STypeString.class);
        SIComposite siComposite = composite.newInstance();
        siComposite.setValue("property", propertyFieldValue);
        return siComposite;
    }

    private STypeComposite<SIComposite> createRoot() {
        return SDictionary.create().createNewPackage("foo.bar").createCompositeType("composite");
    }


    @Test
    public void testWithRootThatContainsASIList() throws Exception {
        stringTemplateLoader.putTemplate("testWithRootThatContainsASIList", "<#list root.composites as composite>${composite.nome}<#sep>, </#list>");
        Map<String, Object> model = new HashMap<>();

        STypeComposite<SIComposite> root = createRoot();
        STypeList<STypeComposite<SIComposite>, SIComposite> composites = root.addFieldListOfComposite("composites", "composite");
        STypeComposite<SIComposite> composite = composites.getElementsType();
        composite.addField("nome", STypeString.class);

        SIComposite iRoot = root.newInstance();
        SIList<SIComposite> iComposites = (SIList<SIComposite>) iRoot.getField("composites");
        iComposites.addNew().setValue("nome", "Danilo");
        iComposites.addNew().setValue("nome", "Francielle");

        model.put("root",  iRoot);
        assertEquals("Danilo, Francielle", pServerFreeMarkerUtil.doMergeWithFreemarker("testWithRootThatContainsASIList", model));
    }

    @Test
    public void testWithSIList() throws Exception {
        stringTemplateLoader.putTemplate("testWithSIList", "<#list composites as composite>${composite.nome}<#sep>, </#list>");
        Map<String, Object> model = new HashMap<>();

        STypeComposite<SIComposite> root = createRoot();
        STypeList<STypeComposite<SIComposite>, SIComposite> composites = root.addFieldListOfComposite("composites", "composite");
        STypeComposite<SIComposite> composite = composites.getElementsType();
        composite.addField("nome", STypeString.class);

        SIComposite iRoot = root.newInstance();
        SIList<SIComposite> iComposites = (SIList<SIComposite>) iRoot.getField("composites");
        iComposites.addNew().setValue("nome", "Danilo");
        iComposites.addNew().setValue("nome", "Francielle");

        model.put("composites",  iComposites);
        assertEquals("Danilo, Francielle", pServerFreeMarkerUtil.doMergeWithFreemarker("testWithSIList", model));
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