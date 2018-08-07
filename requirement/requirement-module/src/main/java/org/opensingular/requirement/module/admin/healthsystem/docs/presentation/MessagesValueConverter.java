/*
 * Copyright (C) 2016 Singular Studios (a.k.a Atom Tecnologia) - www.opensingular.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.opensingular.requirement.module.admin.healthsystem.docs.presentation;

import java.io.CharArrayReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Properties;
import java.util.regex.Pattern;
import javax.annotation.Nullable;

import org.opensingular.requirement.module.admin.healthsystem.docs.DocFieldMetadata;

public class MessagesValueConverter extends DefaultValueConverter {

    public static final String DOCUMENTATION_MESSAGES_UTF8_PROPERTIES = "documentation-messages.utf8.properties";
    public static final String DEFAULT_DOCUMENTATION_MESSAGES_UTF8_PROPERTIES = "default-" + DOCUMENTATION_MESSAGES_UTF8_PROPERTIES;
    private static final MessagesValueConverter DEFAULT = new MessagesValueConverter();
    private Properties messages;

    public MessagesValueConverter(Properties messages) {
        this.messages = messages;
    }

    public static MessagesValueConverter getDefault() {
        return DEFAULT;
    }

    private MessagesValueConverter() {
        try {
            messages = new Properties();
            loadDefaultMessages(messages);
            loadUserMessages(messages);
        } catch (Exception e) {
            getLogger().error(e.getMessage(), e);
        }
    }

    private void loadDefaultMessages(Properties messages) throws IOException {
        messages.load(findProperties(DEFAULT_DOCUMENTATION_MESSAGES_UTF8_PROPERTIES));
    }

    private void loadUserMessages(Properties messages) throws IOException {
        messages.load(findProperties(DOCUMENTATION_MESSAGES_UTF8_PROPERTIES));
    }

    private Reader findProperties(String name) throws IOException {
        URL u = Thread.currentThread()
                .getContextClassLoader()
                .getResource(name);
        if (u != null) {
            getLogger().info("Using documentation properties: {}", name);
            return new InputStreamReader(u.openStream(), Charset.forName("UTF-8"));
        } else {
            /*empty reader*/
            return new CharArrayReader(new char[0]);
        }
    }

    @Override
    public String format(DocFieldMetadata.DocFieldValue<?> fieldValue, Object value) {
        String defaultValue = defaultToString(value);
        String message = findMessage(fieldValue, defaultValue);
        return filterMessage(defaultValue, message);
    }

    protected String findMessage(DocFieldMetadata.DocFieldValue<?> fieldValue, String defaultValue) {
        String propertyName = fieldValue.getKey() + "." + defaultValue;
        String message = messages.getProperty(propertyName);
        if (message == null) {
            message = messages.getProperty(fieldValue.getKey());
        }
        return message;
    }

    protected String filterMessage(String defaultValue, @Nullable String message) {
        if (message == null) {
            return defaultValue;
        } else if (defaultValue == null) {
            return message;
        } else {
            return message.replaceAll(Pattern.quote("{}"), defaultValue);
        }
    }
}
