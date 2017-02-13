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

package org.opensingular.server.module.admin.healthPanel.stypes;

import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import org.opensingular.form.SIComposite;
import org.opensingular.form.SInfoType;
import org.opensingular.form.STypeComposite;
import org.opensingular.form.STypeList;
import org.opensingular.form.TypeBuilder;
import org.opensingular.form.type.core.STypeString;
import org.opensingular.form.view.SViewListByTable;

@SInfoType(spackage = SSystemHealthPackage.class, newable = true, name = SWebHealth.TYPE_NAME)
public class SWebHealth extends STypeComposite<SIComposite> {
	public static final String TYPE_NAME = "webhealth";
	public static final String TYPE_FULL_NAME = SSystemHealthPackage.PACKAGE_NAME+"."+TYPE_NAME;
	
	@Override
	protected void onLoadType(TypeBuilder tb) {
		
        STypeList<STypeComposite<SIComposite>, SIComposite> urlsList = this.addFieldListOfComposite("urls", "urlsList");
        urlsList.setView(()->new SViewListByTable());
        
        STypeComposite<SIComposite> tabela = urlsList.getElementsType();
        
        STypeString urlField = tabela.addFieldString("url");
		urlField
	        .asAtr()
	        	.label("Url")
	        	.maxLength(100)
	        .asAtrBootstrap()
	        	.colPreference(3);
		
		urlField.addInstanceValidator(validatable->{
			String url = validatable.getInstance().getValue();
			try {
				if(url.contains("ldap://") || url.contains("ldaps://")){
					Hashtable<String, String> ldapInfo = new Hashtable<>();
					ldapInfo.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
					ldapInfo.put(Context.PROVIDER_URL, url);
					
					// nao tem como definir o tempo limite pra tentar conectar
					DirContext dirContext = new InitialDirContext(ldapInfo);
					dirContext.close();

				}else if(url.contains("tcp://")){
					url = url.replace("tcp://", "");
					String[] piecesSocketPath = url.split(":");
					Socket testClient = new Socket(piecesSocketPath[0], Integer.valueOf(piecesSocketPath[piecesSocketPath.length-1]));
					testClient.close();

				}else if(url.contains("udp://")){
					url = url.replace("udp://", "");
					String[] piecesUrl = url.split(":");
//					
//					InetSocketAddress address = new InetSocketAddress(
//									InetAddress.getByName(piecesUrl[0]), 
//									Integer.valueOf(piecesUrl[piecesUrl.length-1]));
//					
//					DatagramSocket server = new DatagramSocket();
//					
//					String msg = "Singular test connection";
//					DatagramPacket connectionMsg = new DatagramPacket(msg.getBytes(), msg.getBytes().length, address);
//					server.send(connectionMsg);
//					server.close();
					
					url = "http://"+piecesUrl[0];
					URLConnection openConnection = new URL(url).openConnection();
					openConnection.setConnectTimeout(2000);
					openConnection.connect();
					
				}else{
					// file, ftp, gopher, http, https, jar, mailto, netdoc
					URLConnection openConnection = new URL(url).openConnection();
					openConnection.setConnectTimeout(2000);
					openConnection.connect();
				}
			} catch (Exception e) {
				validatable.error(e.getMessage());
				e.printStackTrace();
			}
		});
	}
}