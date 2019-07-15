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

package org.opensingular.app.commons.cache;

import org.springframework.cache.interceptor.SimpleKeyGenerator;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class SingularKeyGenerator extends SimpleKeyGenerator {

    public static final String SINGULAR_KEY_GENERATOR = "singularKeyGenerator";

    @Override
    public Object generate(Object target, Method method, Object... params) {
        String      methodName       = method.getName();
        String      methodReturnType = method.getReturnType().getName();
        Parameter[] methodParameters = method.getParameters();
        String[]    parameters       = Stream.of(methodParameters).map(Parameter::getName).collect(Collectors.toList()).toArray(new String[]{});
        return internalGenerateKey(methodName, methodReturnType, parameters, params);
    }

    public static String internalGenerateKey(String methodName, String methodReturnType, String[] parameters, Object[] params) {
        int paramsHashCode = 0;
        if (params != null) {
            paramsHashCode = Arrays.hashCode(params);
        }
        BigInteger big = BigInteger.ZERO;
        big = big.add(BigInteger.valueOf(methodName.hashCode()));
        big = big.add(BigInteger.valueOf(methodReturnType.hashCode()));
        big = big.add(BigInteger.valueOf(Arrays.hashCode(parameters)));
        big = big.add(BigInteger.valueOf(paramsHashCode));
        return methodName + big.hashCode();
    }

}