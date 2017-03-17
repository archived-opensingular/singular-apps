package org.opensingular.server.commons.wicket.view.util;

import org.opensingular.server.commons.exception.SingularServerException;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

public class ParameterHttpSerializer {

    private static final String ENCODING = "UTF-8";

    public static String encode(LinkedHashMap<String, String> params) {
        try {
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, String> e : params.entrySet()) {
                if (e.getValue() != null) {
                    if (sb.length() > 0) {
                        sb.append("&");
                    }
                    sb.append(String.format("%s=%s", URLEncoder.encode(e.getKey(), ENCODING), URLEncoder.encode(e.getValue(), ENCODING)));
                }
            }
            return sb.toString();
        } catch (Exception e) {
            throw SingularServerException.rethrow(e.getMessage(), e);
        }
    }

    public static LinkedHashMap<String, String> decode(String query) {
        try {
            LinkedHashMap<String, String> decoded = new LinkedHashMap<>();
            String[] params = query.split("&");
            for (String s : params) {
                String[] value = s.split("=");
                decoded.put(URLDecoder.decode(value[0], ENCODING), URLDecoder.decode(value[1], ENCODING));
            }
            return decoded;
        } catch (Exception e) {
            throw SingularServerException.rethrow(e.getMessage(), e);
        }
    }

    public static String encodeAndCompress(LinkedHashMap<String, String> params) {
        try {
            return compress(encode(params));
        } catch (Exception e) {
            throw SingularServerException.rethrow(e.getMessage(), e);
        }
    }


    public static Map<String, String> decodeCompressed(String query) {
        try {
            return decode(decompress(query));
        } catch (Exception e) {
            throw SingularServerException.rethrow(e.getMessage(), e);
        }
    }

    private static String compress(String s) throws UnsupportedEncodingException {
        return URLEncoder.encode(new String(Base64.getEncoder().encode(compressAlgorithm(s.getBytes(ENCODING))), ENCODING), ENCODING);
    }

    private static String decompress(String s) throws UnsupportedEncodingException {
        return new String(decompressAlgorithm(Base64.getDecoder().decode(URLDecoder.decode(s, ENCODING).getBytes(ENCODING))), ENCODING);
    }

    private static byte[] compressAlgorithm(byte[] bytes) {
        return bytes;
    }

    private static byte[] decompressAlgorithm(byte[] bytes) {
        return bytes;
    }


}
