package org.opensingular.server.commons.test;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.InvocationHandler;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.cycle.AbstractRequestCycleListener;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.http.WebRequest;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

public class SingularTestRequestCycleListener extends AbstractRequestCycleListener {

    @Override
    public void onBeginRequest(RequestCycle cycle) {
        HttpServletRequest mockHttpServletRequest  = (HttpServletRequest) cycle.getRequest().getContainerRequest();
        HttpServletRequest superPoweredMockRequest = getSuperPoweredHttpRequest(mockHttpServletRequest);
        Request            superPoweredRequest     = getSuperPoweredRequest((WebRequest) cycle.getRequest(), superPoweredMockRequest);
        cycle.setRequest(superPoweredRequest);
        ContextUtil.prepareRequest(superPoweredMockRequest);
    }

    private HttpServletRequest getSuperPoweredHttpRequest(HttpServletRequest httpServletRequest) {
        if (isSuperPowered(httpServletRequest)) {
            return httpServletRequest;
        }
        Enhancer enhancer = new Enhancer();
        enhancer.setInterfaces(new Class[]{HttpServletRequest.class, SuperPoweredHttpServletRequest.class});
        enhancer.setCallback(new MethodInterceptor() {


            private String contextPath;
            private String pathInfo;

            @Override
            public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                if (method.getName().equals("setContextPath")) {
                    contextPath = (String) objects[0];
                    return null;
                }
                if (method.getName().equals("getContextPath")) {
                    return contextPath;
                }
                if (method.getName().equals("setPathInfo")) {
                    pathInfo = (String) objects[0];
                    return null;
                }
                if (method.getName().equals("getPathInfo")) {
                    return pathInfo;
                }
                return method.invoke(httpServletRequest, objects);
            }


        });
        return (HttpServletRequest) enhancer.create();
    }

    private WebRequest getSuperPoweredRequest(WebRequest request, HttpServletRequest superPoweredMockRequest) {
        if (isSuperPowered(request)) {
            return request;
        }
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(WebRequest.class);
        enhancer.setInterfaces(new Class[]{SuperPowered.class});
        enhancer.setCallback(new InvocationHandler() {
            @Override
            public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
                if (method.getName().equals("getContainerRequest")) {
                    return superPoweredMockRequest;
                }
                return method.invoke(request, objects);
            }
        });
        return (WebRequest) enhancer.create();
    }

    public boolean isSuperPowered(Object o) {
        return o instanceof SuperPowered;
    }

    public interface SuperPowered {

    }

    public interface SuperPoweredHttpServletRequest extends SuperPowered {

        void setContextPath(String path);

        void setPathInfo(String path);
    }


}
