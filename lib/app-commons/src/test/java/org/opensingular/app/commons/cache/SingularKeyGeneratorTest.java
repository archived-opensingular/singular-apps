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

package org.opensingular.app.commons.cache;

import org.junit.Test;
import org.opensingular.app.commons.cache.SingularKeyGenerator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class SingularKeyGeneratorTest {
    private SingularKeyGenerator singularKeyGenerator= new SingularKeyGenerator();

    @Test
    public void testSameMethodWithParameters() throws Exception {
        String key_1 = singularKeyGenerator.internalGenerateKey("findByKey", "MyDTO", new String[]{"Integer"}, new Integer[]{1});
        String key_2 = singularKeyGenerator.internalGenerateKey("findByKey", "MyDTO", new String[]{"Integer"}, new Integer[]{2});
        String key_3 = singularKeyGenerator.internalGenerateKey("findByKey", "MyDTO", new String[]{"Integer"}, new Integer[]{3});
        assertNotEquals(key_1, key_2);
        assertNotEquals(key_1, key_3);
        assertNotEquals(key_2, key_3);
    }

    @Test
    public void testManyMethods() throws Exception {
        String key_1 = singularKeyGenerator.internalGenerateKey("findEntityByKey", "MyEntity", new String[]{"Integer"}, new Integer[]{0});
        String key_2 = singularKeyGenerator.internalGenerateKey("findDTOByKey", "MyDTO", new String[]{"Integer"}, new Integer[]{0});
        String key_3 = singularKeyGenerator.internalGenerateKey("getName", "String", null, null);
        assertNotEquals(key_1, key_2);
        assertNotEquals(key_1, key_3);
        assertNotEquals(key_2, key_3);
    }

    @Test
    public void testSameMethodWithSameParams() throws Exception {
        String key_1 = singularKeyGenerator.internalGenerateKey("findEntityByKey", "MyEntity", new String[]{"Integer"}, new Integer[]{0});
        String key_2 = singularKeyGenerator.internalGenerateKey("findEntityByKey", "MyEntity", new String[]{"Integer"}, new Integer[]{0});
        assertEquals(key_1, key_2);
    }
}