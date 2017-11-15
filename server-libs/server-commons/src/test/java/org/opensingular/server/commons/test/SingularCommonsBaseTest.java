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

package org.opensingular.server.commons.test;

import org.apache.wicket.Page;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.opensingular.flow.persistence.entity.ModuleEntity;
import org.opensingular.form.persistence.entity.FormTypeEntity;
import org.opensingular.form.wicket.helpers.AssertionsWComponent;
import org.opensingular.form.wicket.helpers.SingularWicketTester;
import org.opensingular.lib.commons.lambda.IConsumer;
import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.server.commons.form.FormAction;
import org.opensingular.server.commons.persistence.dao.form.RequirementDefinitionDAO;
import org.opensingular.server.commons.persistence.entity.form.RequirementDefinitionEntity;
import org.opensingular.server.commons.wicket.view.form.FormPage;
import org.opensingular.server.commons.wicket.view.util.ActionContext;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Nonnull;
import javax.inject.Inject;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = CommonsConfigurationMock.class, loader = SingularCommonsContextLoader.class)
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

    protected RequirementDefinitionEntity requirementDefinitionEntity;

    protected Session session;

    @Before
    public void setUp() {
        session = sessionFactory.getCurrentSession();
        createRequirementIfNeeded();
    }

    private void createRequirementIfNeeded() {
        new TransactionTemplate(transactionManager).execute(status -> {
            requirementDefinitionEntity = requirementDefinitionDAO.findByUniqueProperty("name", "Requirement");
            if (requirementDefinitionEntity == null) {
                requirementDefinitionEntity = new RequirementDefinitionEntity();
                requirementDefinitionEntity.setFormType(createFormTypeIfNeeded());
                requirementDefinitionEntity.setModule(getModule());
                requirementDefinitionEntity.setName("Requirement");
                session.save(requirementDefinitionEntity);
            }

            return null;
        });
    }

    @Nonnull
    private FormTypeEntity createFormTypeIfNeeded() {
        FormTypeEntity formTypeEntity = (FormTypeEntity) session.createCriteria(FormTypeEntity.class).list().stream().findFirst().orElse(null);
        if (formTypeEntity == null) {
            formTypeEntity = new FormTypeEntity();
            formTypeEntity.setAbbreviation("Form");
            formTypeEntity.setLabel("Form");
            formTypeEntity.setCacheVersionNumber(1l);
            session.saveOrUpdate(formTypeEntity);
        }
        return formTypeEntity;
    }

    public ModuleEntity getModule() {
        return (ModuleEntity) session.get(ModuleEntity.class, "GRUPO_TESTE");
    }

    public FormPage sendRequirement(SingularWicketTester tester, String formName, IConsumer<Page> formFiller) {
        ActionContext context = new ActionContext();
        context.setFormName(formName);
        context.setFormAction(FormAction.FORM_FILL);
        context.setRequirementDefinitionId(requirementDefinitionEntity.getCod());
        FormPage p = new FormPage(context);
        tester.startPage(p);
        tester.assertRenderedPage(FormPage.class);

        formFiller.accept(p);
        tester.executeAjaxEvent(new AssertionsWComponent(p).getSubComponentWithId("send-btn").getTarget(), "click");
        tester.executeAjaxEvent(new AssertionsWComponent(p).getSubComponentWithId("confirm-btn").getTarget(), "click");
        return p;
    }

}
