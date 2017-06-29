package org.opensingular.server.commons.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.opensingular.lib.wicket.util.resource.Icon;

import java.io.IOException;

public class IconJsonSerializer extends JsonSerializer<Icon> {

    @Override
    public void serialize(Icon value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("cssClass", value.getCssClass());
        gen.writeEndObject();
    }

}