package org.opensingular.server.commons.admin.healthsystem;

import org.junit.Assert;
import org.junit.Test;
import org.opensingular.form.SIComposite;
import org.opensingular.form.SIList;
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
public class SWebHealthTest extends SingularCommonsBaseTest {

    @Inject
    CommonsApplicationMock singularApplication;

    private SingularWicketTester tester;

    private SIComposite reachWebPanel(){
        tester = new SingularWicketTester(singularApplication);
        HealthSystemPage healthSystemPage = new HealthSystemPage();
        tester.startPage(healthSystemPage);
        tester.executeAjaxEvent(tester.getAssertionsForSubComp("buttonWeb").isNotNull().getTarget(), "click");

        SIList list = (SIList) tester.getAssertionsForSubComp("panelWeb")
                .getSubCompomentWithTypeNameSimple("webhealth").assertSInstance().isComposite().field("urls").getTarget();

        return (SIComposite) list.addNew();
    }

    @WithUserDetails("vinicius.nunes")
    @Transactional
    @Test
    public void httpCheckerTest() throws Exception {
        SIComposite url = reachWebPanel();
        url.getField(0).setValue("http://www.opensingular.org/");

        tester.executeAjaxEvent(tester.getAssertionsForSubComp("checkButtonWeb").getTarget(), "click");
    }

    @WithUserDetails("vinicius.nunes")
    @Transactional
    @Test
    public void ipCheckerExceptionTest() throws Exception {
        SIComposite url = reachWebPanel();
        url.getField(0).setValue("ip://www.opensingular.org/");

        tester.executeAjaxEvent(tester.getAssertionsForSubComp("checkButtonWeb").getTarget(), "click");
        Assert.assertEquals(1, url.getField(0).getValidationErrors().size());
    }

    @WithUserDetails("vinicius.nunes")
    @Transactional
    @Test
    public void ldapCheckerExceptionTest() throws Exception {
        SIComposite url = reachWebPanel();
        url.getField(0).setValue("ldap://10.0.0.3:363");

        tester.executeAjaxEvent(tester.getAssertionsForSubComp("checkButtonWeb").getTarget(), "click");
    }

    @WithUserDetails("vinicius.nunes")
    @Transactional
    @Test
    public void tcpCheckerExceptionTest() throws Exception {
        SIComposite url = reachWebPanel();
        url.getField(0).setValue("tcp://10.0.0.3:80");

        tester.executeAjaxEvent(tester.getAssertionsForSubComp("checkButtonWeb").getTarget(), "click");
    }

    @WithUserDetails("vinicius.nunes")
    @Transactional
    @Test
    public void emptyValueTest() throws Exception {
        SIComposite url = reachWebPanel();
        url.getField(0).setValue("");

        tester.executeAjaxEvent(tester.getAssertionsForSubComp("checkButtonWeb").getTarget(), "click");
        Assert.assertEquals(1, url.getField(0).getValidationErrors().size());
    }
}
