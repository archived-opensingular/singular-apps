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

package org.opensingular.server.commons.flow.metadata;

import org.opensingular.flow.core.property.MetaDataRef;

import java.io.Serializable;

public class RequirementHistoryTaskMetaDataValue implements Serializable{

    public static final RequirementHistoryTaskMetaDataKey
            KEY = new RequirementHistoryTaskMetaDataKey(RequirementHistoryTaskMetaDataKey.class.getName(), RequirementHistoryTaskMetaDataValue.class);
    public static final RequirementHistoryTaskMetaDataValue ON = new RequirementHistoryTaskMetaDataValue();

    RequirementHistoryTaskMetaDataValue() {
    }

    public static class RequirementHistoryTaskMetaDataKey extends MetaDataRef<RequirementHistoryTaskMetaDataValue> {
        private RequirementHistoryTaskMetaDataKey(String name, Class<RequirementHistoryTaskMetaDataValue> valueClass) {
            super(name, valueClass);
        }
    }

}
