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

package org.opensingular.requirement.commons.jackson;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.opensingular.lib.commons.ui.Icon;
import org.opensingular.lib.wicket.util.resource.DefaultIcons;
import org.opensingular.requirement.module.jackson.IconJsonDeserializer;
import org.opensingular.requirement.module.jackson.IconJsonSerializer;
import org.opensingular.requirement.module.jackson.SingularObjectMapper;
import org.opensingular.requirement.module.service.dto.BoxItemAction;
import org.opensingular.requirement.module.service.dto.ItemBox;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class IconJsonSerializationTest {

    private SingularObjectMapper mapper;

    @Before
    public void setUp() throws Exception {
        mapper = new SingularObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    static class IconTest {

        private Icon icon;

        @JsonSerialize(using = IconJsonSerializer.class)
        public Icon getIcon() {
            return icon;
        }

        @JsonDeserialize(using = IconJsonDeserializer.class)
        public IconTest setIcon(Icon icon) {
            this.icon = icon;
            return this;
        }
    }

    @Test
    public void testSerialization() throws Exception {
        IconTest it = new IconTest();
        it.setIcon(DefaultIcons.ARROW_DOWN);
        String serializedValue = mapper.writeValueAsString(it);
        assertEquals("{\"icon\":{\"cssClass\":\"fa fa-arrow-down\"}}", serializedValue);
        IconTest iconTest = mapper.readValue(serializedValue, IconTest.class);
        assertEquals(DefaultIcons.ARROW_DOWN.getCssClass(), iconTest.getIcon().getCssClass());
    }

    @Test
    public void testSerializationBoxItemAction() throws Exception {
        BoxItemAction bia = new BoxItemAction();
        bia.setIcon(DefaultIcons.ARROW_DOWN);
        String serializedValue = mapper.writeValueAsString(bia);
        assertThat(serializedValue, Matchers.containsString("{\"cssClass\":\"fa fa-arrow-down\"}"));
        BoxItemAction boxitemActionDesarialized = mapper.readValue(serializedValue, BoxItemAction.class);
        assertEquals(DefaultIcons.ARROW_DOWN.getCssClass(), boxitemActionDesarialized.getIcon().getCssClass());
    }

    @Test
    public void testSerializationItemBox() throws Exception {
        ItemBox itemBox = new ItemBox();
        itemBox.setIcone(DefaultIcons.ARROW_DOWN);
        String serializedValue = mapper.writeValueAsString(itemBox);
        assertThat(serializedValue, Matchers.containsString("{\"cssClass\":\"fa fa-arrow-down\"}"));
        ItemBox itemBoxDeserialized = mapper.readValue(serializedValue, ItemBox.class);
        assertEquals(DefaultIcons.ARROW_DOWN.getCssClass(), itemBoxDeserialized.getIcone().getCssClass());
    }

}
