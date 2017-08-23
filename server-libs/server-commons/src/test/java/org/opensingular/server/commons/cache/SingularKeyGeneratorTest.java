package org.opensingular.server.commons.cache;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class SingularKeyGeneratorTest {

    @Test
    public void testSameMethodWithParameters() throws Exception {
        String key_1 = SingularKeyGenerator.internalGenerateKey("findByKey", "MyDTO", new String[]{"Integer"}, new Integer[]{1});
        String key_2 = SingularKeyGenerator.internalGenerateKey("findByKey", "MyDTO", new String[]{"Integer"}, new Integer[]{2});
        String key_3 = SingularKeyGenerator.internalGenerateKey("findByKey", "MyDTO", new String[]{"Integer"}, new Integer[]{3});
        assertNotEquals(key_1, key_2);
        assertNotEquals(key_1, key_3);
        assertNotEquals(key_2, key_3);
    }

    @Test
    public void testManyMethods() throws Exception {
        String key_1 = SingularKeyGenerator.internalGenerateKey("findEntityByKey", "MyEntity", new String[]{"Integer"}, new Integer[]{0});
        String key_2 = SingularKeyGenerator.internalGenerateKey("findDTOByKey", "MyDTO", new String[]{"Integer"}, new Integer[]{0});
        String key_3 = SingularKeyGenerator.internalGenerateKey("getName", "String", null, null);
        assertNotEquals(key_1, key_2);
        assertNotEquals(key_1, key_3);
        assertNotEquals(key_2, key_3);
    }

    @Test
    public void testSameMethodWithSameParams() throws Exception {
        String key_1 = SingularKeyGenerator.internalGenerateKey("findEntityByKey", "MyEntity", new String[]{"Integer"}, new Integer[]{0});
        String key_2 = SingularKeyGenerator.internalGenerateKey("findEntityByKey", "MyEntity", new String[]{"Integer"}, new Integer[]{0});
        assertEquals(key_1, key_2);
    }
}