package org.opensingular.server.commons.admin.healthsystem;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.opensingular.form.SIComposite;
import org.opensingular.form.SIList;
import org.opensingular.form.SInstance;
import org.opensingular.form.wicket.helpers.SingularWicketTester;
import org.opensingular.server.commons.test.CommonsApplicationMock;
import org.opensingular.server.commons.test.SingularCommonsBaseTest;
import org.opensingular.server.commons.test.SingularServletContextTestExecutionListener;
import org.opensingular.server.p.commons.admin.healthsystem.HealthSystemPage;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestExecutionListeners;

import javax.inject.Inject;
import javax.transaction.Transactional;

@TestExecutionListeners(listeners = {SingularServletContextTestExecutionListener.class}, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
public class SDbHealthTest extends SingularCommonsBaseTest {
    @Inject
    CommonsApplicationMock singularApplication;

    private SingularWicketTester tester;

    private void reachDbPanel(){
        tester = new SingularWicketTester(singularApplication);
        HealthSystemPage healthSystemPage = new HealthSystemPage();
        tester.startPage(healthSystemPage);
        tester.executeAjaxEvent(tester.getAssertionsForSubComp("buttonDb").isNotNull().getTarget(), "click");
    }

    @WithUserDetails("vinicius.nunes")
    @Transactional
    @Test
    @Ignore("DELFINO")
    public void validateTablesWithErrorTest(){
        reachDbPanel();
        tester.executeAjaxEvent(tester.getAssertionsForSubComp("checkButtonDB").isNotNull().getTarget(), "click");

        SInstance panelDB = tester.getAssertionsInstance().getTarget();
        Assert.assertEquals(0, ((SIList)( ((SIComposite) panelDB).getAllFields().get(0))).get(0).getValidationErrors().size());
    }

}
