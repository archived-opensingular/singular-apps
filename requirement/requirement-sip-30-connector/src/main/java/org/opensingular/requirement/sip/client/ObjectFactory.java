
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

package org.opensingular.requirement.sip.client;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.opensingular.sip package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _ValueItemItemItem_QNAME = new QName("", "item");
    private final static QName _ValueItemItemValue_QNAME = new QName("", "value");
    private final static QName _ValueItemItemKey_QNAME = new QName("", "key");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.opensingular.sip
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ValueItem }
     * 
     */
    public ValueItem createValueItem() {
        return new ValueItem();
    }

    /**
     * Create an instance of {@link ValueItem.Item }
     * 
     */
    public ValueItem.Item createValueItemItem() {
        return new ValueItem.Item();
    }

    /**
     * Create an instance of {@link RetornoValidarLogin }
     * 
     */
    public RetornoValidarLogin createRetornoValidarLogin() {
        return new RetornoValidarLogin();
    }

    /**
     * Create an instance of {@link RetornoValidarLogin.ArrOrgaos }
     * 
     */
    public RetornoValidarLogin.ArrOrgaos createRetornoValidarLoginArrOrgaos() {
        return new RetornoValidarLogin.ArrOrgaos();
    }

    /**
     * Create an instance of {@link RetornoValidarLogin.ArrPermissoes }
     * 
     */
    public RetornoValidarLogin.ArrPermissoes createRetornoValidarLoginArrPermissoes() {
        return new RetornoValidarLogin.ArrPermissoes();
    }

    /**
     * Create an instance of {@link RetornoValidarLogin.ArrPermissoes.Item }
     * 
     */
    public RetornoValidarLogin.ArrPermissoes.Item createRetornoValidarLoginArrPermissoesItem() {
        return new RetornoValidarLogin.ArrPermissoes.Item();
    }

    /**
     * Create an instance of {@link RetornoAutenticarCompleto }
     * 
     */
    public RetornoAutenticarCompleto createRetornoAutenticarCompleto() {
        return new RetornoAutenticarCompleto();
    }

    /**
     * Create an instance of {@link Usuario }
     * 
     */
    public Usuario createUsuario() {
        return new Usuario();
    }

    /**
     * Create an instance of {@link ArrayOfUsuarios }
     * 
     */
    public ArrayOfUsuarios createArrayOfUsuarios() {
        return new ArrayOfUsuarios();
    }

    /**
     * Create an instance of {@link Permissao }
     * 
     */
    public Permissao createPermissao() {
        return new Permissao();
    }

    /**
     * Create an instance of {@link ArrayOfPermissoes }
     * 
     */
    public ArrayOfPermissoes createArrayOfPermissoes() {
        return new ArrayOfPermissoes();
    }

    /**
     * Create an instance of {@link ValueItem.Item.Value }
     * 
     */
    public ValueItem.Item.Value createValueItemItemValue() {
        return new ValueItem.Item.Value();
    }

    /**
     * Create an instance of {@link RetornoValidarLogin.ArrUnidadesPadrao }
     * 
     */
    public RetornoValidarLogin.ArrUnidadesPadrao createRetornoValidarLoginArrUnidadesPadrao() {
        return new RetornoValidarLogin.ArrUnidadesPadrao();
    }

    /**
     * Create an instance of {@link RetornoValidarLogin.ArrPropriedades }
     * 
     */
    public RetornoValidarLogin.ArrPropriedades createRetornoValidarLoginArrPropriedades() {
        return new RetornoValidarLogin.ArrPropriedades();
    }

    /**
     * Create an instance of {@link RetornoValidarLogin.ArrOrgaos.Item }
     * 
     */
    public RetornoValidarLogin.ArrOrgaos.Item createRetornoValidarLoginArrOrgaosItem() {
        return new RetornoValidarLogin.ArrOrgaos.Item();
    }

    /**
     * Create an instance of {@link RetornoValidarLogin.ArrPermissoes.Item.Value }
     * 
     */
    public RetornoValidarLogin.ArrPermissoes.Item.Value createRetornoValidarLoginArrPermissoesItemValue() {
        return new RetornoValidarLogin.ArrPermissoes.Item.Value();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "item", scope = ValueItem.Item.class)
    public JAXBElement<String> createValueItemItemItem(String value) {
        return new JAXBElement<String>(_ValueItemItemItem_QNAME, String.class, ValueItem.Item.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ValueItem.Item.Value }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "value", scope = ValueItem.Item.class)
    public JAXBElement<ValueItem.Item.Value> createValueItemItemValue(ValueItem.Item.Value value) {
        return new JAXBElement<ValueItem.Item.Value>(_ValueItemItemValue_QNAME, ValueItem.Item.Value.class, ValueItem.Item.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "key", scope = ValueItem.Item.class)
    public JAXBElement<String> createValueItemItemKey(String value) {
        return new JAXBElement<String>(_ValueItemItemKey_QNAME, String.class, ValueItem.Item.class, value);
    }

}
