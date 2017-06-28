package org.opensingular.server.commons.test;

import org.apache.wicket.Page;
import org.junit.runner.RunWith;
import org.opensingular.form.wicket.helpers.AssertionsWComponent;
import org.opensingular.form.wicket.helpers.SingularWicketTester;
import org.opensingular.lib.commons.lambda.IConsumer;
import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.server.commons.STypeFOO;
import org.opensingular.server.commons.form.FormAction;
import org.opensingular.server.commons.wicket.view.form.FormPage;
import org.opensingular.server.commons.wicket.view.util.ActionContext;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = CommonsConfigurationMock.class, loader = SingularCommonsContextLoader.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@Transactional
@Commit
public abstract class SingularCommonsBaseTest implements Loggable {

    public FormPage sendPetition(SingularWicketTester tester, String formName, IConsumer<Page> formFiller) {
        ActionContext context = new ActionContext();
        context.setFormName(formName);
        context.setFormAction(FormAction.FORM_FILL);
        FormPage p = new FormPage(context);
        tester.startPage(p);
        tester.assertRenderedPage(FormPage.class);

        formFiller.accept(p);
        tester.executeAjaxEvent(new AssertionsWComponent(p).getSubCompomentWithId("send-btn").getTarget(), "click");
        tester.executeAjaxEvent(new AssertionsWComponent(p).getSubCompomentWithId("confirm-btn").getTarget(), "click");
        return p;
    }

}
