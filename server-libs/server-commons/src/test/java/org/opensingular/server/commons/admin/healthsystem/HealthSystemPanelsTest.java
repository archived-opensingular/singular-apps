package org.opensingular.server.commons.admin.healthsystem;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.junit.Test;
import org.opensingular.form.SIComposite;
import org.opensingular.form.SIList;
import org.opensingular.form.wicket.helpers.AssertionsWComponent;
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
public class HealthSystemPanelsTest extends SingularCommonsBaseTest {

    @Inject
    CommonsApplicationMock singularApplication;

    private SingularWicketTester tester;

    private void startPageTest() {
        tester = new SingularWicketTester(singularApplication);
        Page p = new HealthSystemPage();
        tester.startPage(p);
    }

    @WithUserDetails("vinicius.nunes")
    @Transactional
    @Test
    public void cachePanelTest(){
        startPageTest();

        tester.executeAjaxEvent(tester.getAssertionsForSubComp("buttonCache").getTarget(), "click");
        tester.executeAjaxEvent(tester.getAssertionsForSubComp("clearAllCaches").getTarget(), "click");
    }

    @WithUserDetails("vinicius.nunes")
    @Transactional
    @Test
    public void jobPanelTest(){
        startPageTest();

        tester.executeAjaxEvent(tester.getAssertionsForSubComp("buttonJobs").getTarget(), "click");
        tester.executeAjaxEvent(tester.getAssertionsForSubComp("runAllJobs").getTarget(), "click");
    }

    @WithUserDetails("vinicius.nunes")
    @Transactional
    @Test
    public void webPanelTest(){
        startPageTest();
        tester.executeAjaxEvent(tester.getAssertionsForSubComp("buttonWeb").getTarget(), "click");

        tester.executeAjaxEvent(tester.getAssertionsForSubComp("checkButtonWeb").getTarget(), "click");
        tester.executeAjaxEvent(tester.getAssertionsForSubComp("saveButtonWeb").getTarget(), "click");
    }
}
