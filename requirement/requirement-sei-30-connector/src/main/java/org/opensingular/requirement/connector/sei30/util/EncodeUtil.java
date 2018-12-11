package org.opensingular.requirement.connector.sei30.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class EncodeUtil {

    private EncodeUtil() {
    }

    public static String encodeToBase64(File file) throws IOException {
        return encodeToBase64(new FileInputStream(file), (int) file.length());
    }

    public static String encodeToBase64(InputStream fis, int size) throws IOException {
        byte[] bytes = IOUtils.toByteArray(fis);
        fis.close();
        return new String(Base64.encodeBase64(bytes));
    }

    public static String encodeToBase64(String value) {
        return StringUtils.isNotEmpty(value) ? Base64.encodeBase64String(value.getBytes(StandardCharsets.UTF_8)) : null;
    }

}
