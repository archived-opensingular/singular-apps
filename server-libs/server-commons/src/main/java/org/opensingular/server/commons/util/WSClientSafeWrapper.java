package org.opensingular.server.commons.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.opensingular.lib.commons.base.SingularException;
import org.opensingular.server.commons.exception.SingularServerIntegrationException;

import javax.xml.ws.soap.SOAPFaultException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * * Classe WSClientSafeWrapper.
 *
 * Deprecated, use org.opensingular.lib.commons.util.WSClientSafeWrapper instead
 */
@Deprecated
public class WSClientSafeWrapper {

    /**
     * Tolerancia a falha: reinstancia o cliente em caso de erro. Nao retorna
     * valores default
     * <p>
     * Essa estratégia permite fazer o cache do cliente (cuja criação é custosa) e ao mesmo tempo
     * permite que o endpoint possa ser alterado em tempo de execução sem reiniciar a aplicação.
     *
     * @param <T>     um generic type
     * @param wsIface um ws iface
     * @param factory um factory
     * @return um objeto do tipo T
     */
    @SuppressWarnings("unchecked")
    public static <T> T wrap(final Class<T> wsIface, final String humanName, final WSClientFactory<T> factory) {
        return (T) Proxy.newProxyInstance(wsIface.getClassLoader(), new Class[]{wsIface}, new InvocationHandler() {

            private final Logger log = Logger.getLogger(getClass());
            private T ref = factory.getReference();

            @Override
            public Object invoke(Object proxy, Method method, Object[] args)
                    throws Throwable {
                boolean isDefaultObjectMethod = false;
                try {
                    isDefaultObjectMethod = isDefaultObjectMethod(method);
                    if (isDefaultObjectMethod){
                        return method.invoke(ref, args);
                    }
                    log.warn(String.format("CHAMADA A WEB-SERVICE: %s OPERACAO: %s ", wsIface.getName(), method.getName()));
                    ExecutorService executor = Executors.newCachedThreadPool();
                    Callable<Object> task = new Callable<Object>() {
                        @Override
                        public Object call() throws InvocationTargetException, IllegalAccessException {
                            return method.invoke(ref, args);
                        }
                    };
                    Future<Object> future = executor.submit(task);
                    try {
                        return future.get(45, TimeUnit.SECONDS);
                    } catch (TimeoutException ex) {
                        log.fatal("WEB-SERVICE NÃO RESPONDEU A TEMPO (45 segundos)");
                        throw SingularServerIntegrationException.rethrow(humanName, ex);
                    } finally {
                        future.cancel(true);
                    }
                } catch (SingularException e) {
                    throw e;
                } catch (Exception e) {
                    ref = factory.getReference();
                    log.fatal(e.getMessage(), e);
                    throw SingularServerIntegrationException.rethrow(humanName, extrairSOAPFaultMessage(e), e);
                } finally {
                    if (!isDefaultObjectMethod) {
                        log.warn(String.format("RETORNO DE WEB-SERVICE: %s OPERACAO: %s ", wsIface.getName(), method.getName()));
                    }
                }
            }

            private String extrairSOAPFaultMessage(Exception e) {
                Throwable cause = e.getCause();
                while (cause != null) {
                    if (cause instanceof SOAPFaultException) {
                        return cause.getMessage();
                    }
                    cause = cause.getCause();
                }

                return StringUtils.EMPTY;
            }
        });
    }

    private static boolean isDefaultObjectMethod(Method method) {
        return Arrays.asList(Object.class.getMethods()).contains(method);
    }

    /**
     * Fábrica para criação de objetos do tipo WSClient.
     *
     * @param <T> um generic type
     */
    public interface WSClientFactory<T> {

        /**
         * Obtém uma referência de reference.
         *
         * @return uma referência de reference
         */
        public T getReference();
    }



    public static String getAdressWithoutWsdl(String adress){
        int lastIndex = adress.lastIndexOf("?wsdl");
        if (lastIndex > 0) {
            return adress.substring(0, lastIndex);
        }
        return adress;
    }
}
