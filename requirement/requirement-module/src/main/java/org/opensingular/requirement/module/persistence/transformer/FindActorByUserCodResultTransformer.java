/*
 *
 *  * Copyright (C) 2016 Singular Studios (a.k.a Atom Tecnologia) - www.opensingular.com
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  * http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package org.opensingular.requirement.module.persistence.transformer;

import org.hibernate.transform.ResultTransformer;
import org.opensingular.flow.persistence.entity.Actor;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

public class FindActorByUserCodResultTransformer implements ResultTransformer {

    private static final String COD         = "cod";
    private static final String COD_USUARIO = "codUsuario";
    private static final String NOME        = "nome";
    private static final String EMAIL       = "email";


    @Override
    public Object transformTuple(Object[] objects, String[] strings) {

        if (objects == null || objects.length == 0) {
            return null;
        }

        Actor actor = new Actor();

        for (int i = 0; i < strings.length; i += 1) {
            Object rawObject = objects[i];
            switch (strings[i]) {
                case COD:
                    actor.setCod(castToInteger(rawObject));
                    break;
                case COD_USUARIO:
                    actor.setCodUsuario(String.valueOf(rawObject));
                    break;
                case NOME:
                    actor.setNome((String) rawObject);
                    break;
                case EMAIL:
                    actor.setEmail((String) rawObject);
                    break;
                default:
            }
        }

        return actor;
    }

    private Integer castToInteger(Object rawObject) {
        Integer value = null;
        if (rawObject instanceof BigDecimal) {
            value = ((BigDecimal) rawObject).intValue();
        }
        if (rawObject instanceof BigInteger) {
            value = ((BigInteger) rawObject).intValue();
        }
        if (rawObject instanceof Integer) {
            value = (Integer) rawObject;
        }
        return value;
    }

    @Override
    public List transformList(List list) {
        return list;
    }


}
