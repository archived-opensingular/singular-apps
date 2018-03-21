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

package org.opensingular.server.commons.service;

import java.util.Optional;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.Test;
import org.opensingular.flow.persistence.dao.ModuleDAO;
import org.opensingular.flow.persistence.entity.ModuleEntity;
import org.opensingular.server.commons.persistence.dao.ParameterDAO;
import org.opensingular.server.commons.persistence.entity.parameter.ParameterEntity;
import org.opensingular.server.commons.test.SingularCommonsBaseTest;
import org.springframework.test.annotation.Rollback;

public class ParameterServiceTest extends SingularCommonsBaseTest {
    @Inject
    private ParameterService parameterService;

    @Inject
    private ParameterDAO parameterDAO;

    @Inject
    private ModuleDAO moduleDAO;

    @Test
    @Transactional
    @Rollback
    public void testFindByNameAndModule(){
        ModuleEntity groupEntity = new ModuleEntity();
        groupEntity.setCod("1");
        groupEntity.setName("groupName");
        groupEntity.setConnectionURL("connectionUrl Test");
        moduleDAO.save(groupEntity);

        ParameterEntity entity = new ParameterEntity();
        entity.setName("testParameter");
        entity.setModule(groupEntity);
        entity.setValue("valor teste");

        parameterDAO.save(entity);

        Optional<ParameterEntity> testParameter = parameterService
                .findByNameAndModule("testParameter", groupEntity.getCod());
        Assert.assertEquals(entity, testParameter.get());

        testParameter = parameterService.findByNameAndModule("testParameter", groupEntity.getCod());
        Assert.assertEquals(entity, testParameter.get());

        Assert.assertEquals(entity.getName(), testParameter.get().getName());
        Assert.assertEquals(entity.getCod(), testParameter.get().getCod());
    }
}
