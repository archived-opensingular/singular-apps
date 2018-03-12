package br.gov.antaq.sip.util;

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
