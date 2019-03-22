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

package org.opensingular.requirement.sip.util;

import java.nio.charset.Charset;
import java.util.Base64;

public class SIPUtil {

	private static final String UTF8 = "UTF-8";


	public static String encodePassword(String password) {

		byte[] passBytes = password.getBytes(Charset.forName(UTF8));
		byte[] inverterPassBytes = new byte[passBytes.length];
		
		for (int i = 0; i < passBytes.length; i++) {
			inverterPassBytes[i] = (byte) ~ passBytes[i];
		}
		
		return new String(Base64.getEncoder().encode(inverterPassBytes), Charset.forName(UTF8));
	}

	public static String decodePassword(String password) {

		byte[] passDecoded = Base64.getDecoder().decode(password.getBytes(Charset.forName(UTF8)));
		byte[] inverterPassBytes = new byte[passDecoded.length];
		
		for (int i = 0; i < passDecoded.length; i++) {
			inverterPassBytes[i] = (byte) ~ passDecoded[i];
		}
		return new String(inverterPassBytes, Charset.forName(UTF8));
	}

	
}
