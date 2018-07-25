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

package org.opensingular.requirement.commons;

import javax.inject.Inject;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.opensingular.form.SFormUtil;
import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.requirement.module.SingularModuleConfigurationBean;
import org.opensingular.requirement.module.SingularRequirement;
import org.opensingular.requirement.module.persistence.dao.form.RequirementDefinitionDAO;
import org.opensingular.requirement.module.persistence.entity.form.RequirementDefinitionEntity;
import org.opensingular.requirement.module.service.SingularModuleContextLoader;
import org.opensingular.requirement.module.test.ModuleConfigurationMock;
import org.opensingular.singular.pet.module.foobar.stuff.STypeFoo;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = ModuleConfigurationMock.class, loader = SingularModuleContextLoader.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@Transactional
@Commit
public abstract class SingularCommonsBaseTest implements Loggable {

    @Inject
    protected SessionFactory sessionFactory;

    @Inject
    protected PlatformTransactionManager transactionManager;


    @Inject
    private RequirementDefinitionDAO<RequirementDefinitionEntity> requirementDefinitionDAO;



    @Inject
    private SingularModuleConfigurationBean singularModuleConfiguration;

    protected Session session;

    @Before
    public void setUp() {
        session = sessionFactory.getCurrentSession();
    }

    protected RequirementDefinitionEntity getRequirementDefinition() {
        return requirementDefinitionDAO.getOrException(getCodRequirementDefinition());
    }

    protected Long getCodRequirementDefinition() {
        return singularModuleConfiguration.getRequirements()
                .stream()
                .filter(s -> {
                            String name  = s.getMainForm().getSimpleName();
                            String name2 = SFormUtil.getTypeSimpleName(STypeFoo.class).get();
                            return name.equals(name2);
                        }
                )
                .findFirst()
                .map(SingularRequirement::getDefinitionCod)
                .orElse(null);

    }
}
