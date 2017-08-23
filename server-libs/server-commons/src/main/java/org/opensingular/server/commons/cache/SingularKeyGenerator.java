package org.opensingular.server.commons.cache;

import org.jetbrains.annotations.NotNull;
import org.springframework.cache.interceptor.SimpleKeyGenerator;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class SingularKeyGenerator extends SimpleKeyGenerator {

    @Override
    public Object generate(Object target, Method method, Object... params) {
        String methodName = method.getName();
        String methodReturnType = method.getReturnType().getName();
        Parameter[] methodParameters = method.getParameters();
        return generateKey(methodName, methodReturnType, Stream.of(methodParameters).map(Parameter::getName), params);
    }

    public static String generateKey(String methodName, String methodReturnType, String[] parameters, Object[] params) {
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