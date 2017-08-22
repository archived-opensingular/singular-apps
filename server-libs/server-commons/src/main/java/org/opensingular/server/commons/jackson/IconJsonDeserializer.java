package org.opensingular.server.commons.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.node.TextNode;
import org.opensingular.lib.commons.ui.Icon;

import java.io.IOException;

public class IconJsonDeserializer extends JsonDeserializer<Icon> {

    @Override
    public Icon deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        TreeNode jsonNode = p.getCodec().readTree(p);
        String   cssClass = ((TextNode) jsonNode.get("cssClass")).asText();
        return () -> cssClass;
    }

}