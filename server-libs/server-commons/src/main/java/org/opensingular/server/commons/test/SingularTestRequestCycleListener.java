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

package org.opensingular.server.commons.test;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.InvocationHandler;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.apache.wicket.protocol.http.servlet.ServletWebRequest;
import org.apache.wicket.request.cycle.AbstractRequestCycleListener;
import org.apache.wicket.request.cycle.RequestCycle;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class SingularTestRequestCycleListener extends AbstractRequestCycleListener {


    @Override
    public void onBeginRequest(RequestCycle cycle) {
        HttpServletRequest mockHttpServletRequest  = (HttpServletRequest) cycle.getRequest().getContainerRequest();
        HttpServletRequest superPoweredMockRequest = getSuperPoweredHttpRequest(mockHttpServletRequest);
        ServletWebRequest  superPoweredRequest     = getSuperPoweredRequest((ServletWebRequest) cycle.getRequest(), superPoweredMockRequest);
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
            public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy)
                    throws InvocationTargetException, IllegalAccessException {
                if ("setContextPath".equals(method.getName())) {
                    contextPath = (String) objects[0];
                    return null;
                }
                if ("getContextPath".equals(method.getName())) {
                    return contextPath;
                }
                if ("setPathInfo".equals(method.getName())) {
                    pathInfo = (String) objects[0];
                    return null;
                }
                if ("getPathInfo".equals(method.getName())) {
                    return pathInfo;
                }
                return method.invoke(httpServletRequest, objects);
            }


        });
        return (HttpServletRequest) enhancer.create();
    }

    private ServletWebRequest getSuperPoweredRequest(ServletWebRequest request, HttpServletRequest superPoweredMockRequest) {
        if (isSuperPowered(request)) {
            return request;
        }
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(SingularServletWebRequest.class);
        enhancer.setInterfaces(new Class[]{SuperPowered.class});
        enhancer.setCallback(new InvocationHandler() {
            @Override
            public Object invoke(Object o, Method method, Object[] objects)
                    throws InvocationTargetException, IllegalAccessException {
                if ("getContainerRequest".equals(method.getName())) {
                    return superPoweredMockRequest;
                }
                return method.invoke(request, objects);
            }
        });
        return (ServletWebRequest) enhancer.create();
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

    /**
     * Dummy class to provide no-arg constructor for ServletWebRequest
     */
    public static class SingularServletWebRequest extends ServletWebRequest {

        public SingularServletWebRequest() {
            super(new org.springframework.mock.web.MockHttpServletRequest(), "");
        }

    }

}
