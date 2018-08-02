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

package org.opensingular.requirement.module.wicket.view.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.opensingular.requirement.module.exception.SingularServerException;

public class ParameterHttpSerializer {

    private static final Charset ENCODING = StandardCharsets.UTF_8;

    public static String encode(LinkedHashMap<String, String> params) {
        try {
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, String> e : params.entrySet()) {
                if (sb.length() > 0) {
                    sb.append('&');
                }
                if (e.getValue() != null) {
                    sb.append(String.format("%s=%s", URLEncoder.encode(e.getKey(), ENCODING.name()), URLEncoder.encode(e.getValue(), ENCODING.name())));
                } else {
                    sb.append(String.format("%s", URLEncoder.encode(e.getKey(), ENCODING.name())));
                }
            }
            return sb.toString();
        } catch (Exception e) {
            throw SingularServerException.rethrow(e.getMessage(), e);
        }
    }

    public static LinkedHashMap<String, String> decode(String query) {
        try {
            if (StringUtils.isEmpty(query)){
                return new LinkedHashMap<>();
            }
            String cleanQueryString = clearQueryString(query);
            LinkedHashMap<String, String> decoded = new LinkedHashMap<>();
            String[] params = cleanQueryString.split("&");
            for (String paramString : params) {
                Pair<String, String> param = parseParamValue(paramString);
                decoded.put(param.getKey(), param.getValue());
            }
            return decoded;
        } catch (Exception e) {
            throw SingularServerException.rethrow(e.getMessage(), e);
        }
    }

    private static Pair<String, String> parseParamValue(String paramString) throws UnsupportedEncodingException {
        String[] param = paramString.split("=");
        String   key   = URLDecoder.decode(param[0], ENCODING.name());
        String   value = null;
        if (param.length > 1) {
            StringBuilder sb = new StringBuilder();
            for (int i = 1; i < param.length; i++) {
                sb.append(param[i]);
            }
            value = URLDecoder.decode(sb.toString(), ENCODING.name());
        }
        return Pair.of(key, value);
    }

    private static String clearQueryString(String queryString) {
        String result = queryString;
        //Remove url fragment - usually anchor links
        if (queryString.contains("#")) {
            result = result.substring(0, result.indexOf('#'));
        }
        return result;
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
        return URLEncoder.encode(
                new String(
                        Base64.getUrlEncoder().encode(
                                compressAlgorithm(s.getBytes(ENCODING))
                        ), ENCODING
                ),
                ENCODING.name());
    }

    private static String decompress(String s) throws UnsupportedEncodingException {
        return new String(
                decompressAlgorithm(
                        Base64.getUrlDecoder().decode(
                                URLDecoder.decode(s, ENCODING.name())
                                        .getBytes(ENCODING)
                        )
                ),
                ENCODING);
    }

    private static byte[] compressAlgorithm(byte[] bytes) {
        return bytes;
    }

    private static byte[] decompressAlgorithm(byte[] bytes) {
        return bytes;
    }


}
