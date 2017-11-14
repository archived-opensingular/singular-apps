package br.gov.antaq.sip.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SIPUtilTest {

	@Test
	public void encodeTest() {
		String encodePassword = SIPUtil.encodePassword("123456");
		assertEquals("zs3My8rJ", encodePassword);
	}
	
	@Test
	public void decodeTest() {
		String decodePassword = SIPUtil.decodePassword("zs3My8rJ");
		assertEquals("123456", decodePassword);
		
	}
}
