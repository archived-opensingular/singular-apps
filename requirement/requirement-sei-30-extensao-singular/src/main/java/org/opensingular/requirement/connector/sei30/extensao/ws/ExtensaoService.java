
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

package org.opensingular.requirement.connector.sei30.extensao.ws;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;
import java.net.URL;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebServiceClient(name = "extensaoService", targetNamespace = "extensaons", wsdlLocation = "wsextensao.wsdl")
public class ExtensaoService
    extends Service
{

    private final static URL EXTENSAOSERVICE_WSDL_LOCATION;
    private final static WebServiceException EXTENSAOSERVICE_EXCEPTION;
    private final static QName EXTENSAOSERVICE_QNAME = new QName("extensaons", "extensaoService");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = Thread.currentThread().getContextClassLoader().getResource("wsextensao.wsdl");
        } catch (Exception ex) {
            e = new WebServiceException(ex);
        }
        EXTENSAOSERVICE_WSDL_LOCATION = url;
        EXTENSAOSERVICE_EXCEPTION = e;
    }

    public ExtensaoService() {
        super(__getWsdlLocation(), EXTENSAOSERVICE_QNAME);
    }

    public ExtensaoService(WebServiceFeature... features) {
        super(__getWsdlLocation(), EXTENSAOSERVICE_QNAME, features);
    }

    public ExtensaoService(URL wsdlLocation) {
        super(wsdlLocation, EXTENSAOSERVICE_QNAME);
    }

    public ExtensaoService(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, EXTENSAOSERVICE_QNAME, features);
    }

    public ExtensaoService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public ExtensaoService(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns ExtensaoPortType
     */
    @WebEndpoint(name = "extensaoPortService")
    public ExtensaoPortType getExtensaoPortService() {
        return super.getPort(new QName("extensaons", "extensaoPortService"), ExtensaoPortType.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns ExtensaoPortType
     */
    @WebEndpoint(name = "extensaoPortService")
    public ExtensaoPortType getExtensaoPortService(WebServiceFeature... features) {
        return super.getPort(new QName("extensaons", "extensaoPortService"), ExtensaoPortType.class, features);
    }

    private static URL __getWsdlLocation() {
        if (EXTENSAOSERVICE_EXCEPTION!= null) {
            throw EXTENSAOSERVICE_EXCEPTION;
        }
        return EXTENSAOSERVICE_WSDL_LOCATION;
    }

}
