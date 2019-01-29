package org.opensingular.requirement.connector.sei30.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SEIUtilTest {

    @Test
    public void truncate() {
        String str = "Teste de String truncada";
        int maxLenght = 23;
        String expected = "Teste de String trun...";

        assertEquals(SEIUtil.truncate(str, maxLenght).length(), maxLenght);
        assertEquals(SEIUtil.truncate(str, maxLenght), expected);

        assertEquals(SEIUtil.truncate(str, maxLenght + 1).length(), maxLenght + 1);
        assertEquals(SEIUtil.truncate(str, maxLenght + 1), str);
    }

}