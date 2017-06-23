package org.opensingular.server.commons.jackson;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.opensingular.lib.wicket.util.resource.DefaultIcons;
import org.opensingular.lib.wicket.util.resource.Icon;
import org.opensingular.server.commons.service.dto.BoxItemAction;
import org.opensingular.server.commons.service.dto.ItemBox;

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
