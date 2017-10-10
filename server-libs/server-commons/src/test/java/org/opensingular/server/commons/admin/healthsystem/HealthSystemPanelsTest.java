package org.opensingular.server.commons.admin.healthsystem;

import org.apache.wicket.Page;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.junit.Test;
import org.opensingular.form.wicket.helpers.SingularWicketTester;
import org.opensingular.server.commons.test.CommonsApplicationMock;
import org.opensingular.server.commons.test.SingularCommonsBaseTest;
import org.opensingular.server.commons.test.SingularServletContextTestExecutionListener;
import org.opensingular.server.p.commons.admin.healthsystem.HealthSystemPage;
import org.opensingular.server.p.commons.admin.healthsystem.extension.CacheAdminEntry;
import org.opensingular.server.p.commons.admin.healthsystem.extension.JobsAdminEntry;
import org.opensingular.server.p.commons.admin.healthsystem.extension.WebAdminEntry;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestExecutionListeners;

import javax.inject.Inject;
import javax.transaction.Transactional;

import static org.opensingular.server.p.commons.admin.healthsystem.HealthSystemPage.ENTRY_PATH_PARAM;

@TestExecutionListeners(listeners = {SingularServletContextTestExecutionListener.class}, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
public class HealthSystemPanelsTest extends SingularCommonsBaseTest {

    @Inject
    CommonsApplicationMock singularApplication;

    private SingularWicketTester tester;

    private void startPageTest(String key) {
        tester = new SingularWicketTester(singularApplication);
        Page p = new HealthSystemPage(new PageParameters().add(ENTRY_PATH_PARAM, key));
        tester.startPage(p);
    }

    @WithUserDetails("vinicius.nunes")
    @Transactional
    @Test
    public void cachePanelTest() {
        startPageTest(new CacheAdminEntry().getKey());
        tester.executeAjaxEvent(tester.getAssertionsForSubComp("clearAllCaches").getTarget(), "click");
    }

    @WithUserDetails("vinicius.nunes")
    @Transactional
    @Test
    public void jobPanelTest() {
        startPageTest(new JobsAdminEntry().getKey());
        tester.executeAjaxEvent(tester.getAssertionsForSubComp("runAllJobs").getTarget(), "click");
    }

    @WithUserDetails("vinicius.nunes")
    @Transactional
    @Test
    public void webPanelTest() {
        startPageTest(new WebAdminEntry().getKey());
        tester.executeAjaxEvent(tester.getAssertionsForSubComp("checkButtonWeb").getTarget(), "click");
        tester.executeAjaxEvent(tester.getAssertionsForSubComp("saveButtonWeb").getTarget(), "click");
    }
}
