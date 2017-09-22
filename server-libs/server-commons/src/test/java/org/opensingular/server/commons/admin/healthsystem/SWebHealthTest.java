package org.opensingular.server.commons.admin.healthsystem;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.opensingular.form.SIComposite;
import org.opensingular.form.SIList;
import org.opensingular.form.validation.ValidationError;
import org.opensingular.form.wicket.helpers.SingularWicketTester;
import org.opensingular.server.commons.test.CommonsApplicationMock;
import org.opensingular.server.commons.test.SingularCommonsBaseTest;
import org.opensingular.server.commons.test.SingularServletContextTestExecutionListener;
import org.opensingular.server.p.commons.admin.healthsystem.HealthSystemPage;
import org.opensingular.server.p.commons.admin.healthsystem.extension.WebAdminEntry;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestExecutionListeners;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Collection;

import static org.opensingular.server.p.commons.admin.healthsystem.HealthSystemPage.ENTRY_PATH_PARAM;

@TestExecutionListeners(listeners = {SingularServletContextTestExecutionListener.class}, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
public class SWebHealthTest extends SingularCommonsBaseTest {

    @Inject
    CommonsApplicationMock singularApplication;

    private SingularWicketTester tester;

    private SIComposite reachWebPanelAndGetNewCompositeInstance() {
        tester = new SingularWicketTester(singularApplication);
        HealthSystemPage healthSystemPage = new HealthSystemPage(new PageParameters().add(ENTRY_PATH_PARAM, new WebAdminEntry().getKey()));
        tester.startPage(healthSystemPage);
        SIList list = (SIList) tester.getAssertionsForSubComp("panelWeb")
                .getSubCompomentWithTypeNameSimple("webhealth").assertSInstance().isComposite().field("urls").getTarget();

        return (SIComposite) list.addNew();
    }

    @WithUserDetails("vinicius.nunes")
    @Transactional
    @Test
    public void httpCheckerTest() throws Exception {
        SIComposite url = reachWebPanelAndGetNewCompositeInstance();
        url.getField(0).setValue("http://www.naoexisteabc.com.br");

        tester.executeAjaxEvent(tester.getAssertionsForSubComp("checkButtonWeb").getTarget(), "click");
        Assert.assertEquals(1, url.getField(0).getValidationErrors().size());
    }

    @WithUserDetails("vinicius.nunes")
    @Transactional
    @Test
    public void httpsCheckerTest() throws Exception {
        SIComposite url = reachWebPanelAndGetNewCompositeInstance();
        url.getField(0).setValue("https://wwwasdsadasdasdadsad");

        tester.executeAjaxEvent(tester.getAssertionsForSubComp("checkButtonWeb").getTarget(), "click");
        Assert.assertEquals(1, url.getField(0).getValidationErrors().size());
    }

    @Ignore
    @WithUserDetails("vinicius.nunes")
    @Transactional
    @Test
    public void ipCheckerExceptionTest() throws Exception {
        SIComposite url = reachWebPanelAndGetNewCompositeInstance();
        url.getField(0).setValue("ip://naoexisteabc.com.br:80");

        tester.executeAjaxEvent(tester.getAssertionsForSubComp("checkButtonWeb").getTarget(), "click");
        Assert.assertEquals(1, url.getField(0).getValidationErrors().size());
    }

    @WithUserDetails("vinicius.nunes")
    @Transactional
    @Test
    public void ipCheckerTest() throws Exception {
        SIComposite url = reachWebPanelAndGetNewCompositeInstance();
        url.getField(0).setValue("ip://localhost:80");

        tester.executeAjaxEvent(tester.getAssertionsForSubComp("checkButtonWeb").getTarget(), "click");
        Collection<ValidationError> validationErrors = url.getField(0).getValidationErrors();
        String                      message          = validationErrors.stream().map(ve -> ve.getMessage() + "\n").reduce("", String::concat);
        Assert.assertEquals(message, 0, url.getField(0).getValidationErrors().size());
    }

    @Ignore("Muito lento e não funciona bem")
    @WithUserDetails("vinicius.nunes")
    @Transactional
    @Test
    public void ldapCheckerExceptionTest() throws Exception {
        SIComposite url = reachWebPanelAndGetNewCompositeInstance();
        url.getField(0).setValue("ldap://10.0.0.3:363");

        tester.executeAjaxEvent(tester.getAssertionsForSubComp("checkButtonWeb").getTarget(), "click");
    }

    @Ignore("Muito lento e não funciona bem")
    @WithUserDetails("vinicius.nunes")
    @Transactional
    @Test
    public void ldapsCheckerExceptionTest() throws Exception {
        SIComposite url = reachWebPanelAndGetNewCompositeInstance();
        url.getField(0).setValue("ldaps://10.0.0.3:363");

        tester.executeAjaxEvent(tester.getAssertionsForSubComp("checkButtonWeb").getTarget(), "click");
    }

    @Ignore("Muito lento e não funciona bem")
    @WithUserDetails("vinicius.nunes")
    @Transactional
    @Test
    public void tcpCheckerExceptionTest() throws Exception {
        SIComposite url = reachWebPanelAndGetNewCompositeInstance();
        url.getField(0).setValue("tcp://10.0.0.3:8080");

        tester.executeAjaxEvent(tester.getAssertionsForSubComp("checkButtonWeb").getTarget(), "click");
        Assert.assertEquals(1, url.getField(0).getValidationErrors().size());
    }

    @WithUserDetails("vinicius.nunes")
    @Transactional
    @Test
    public void invalidValueTest() throws Exception {
        SIComposite url = reachWebPanelAndGetNewCompositeInstance();
        url.getField(0).setValue("www");

        tester.executeAjaxEvent(tester.getAssertionsForSubComp("checkButtonWeb").getTarget(), "click");
        Assert.assertEquals(1, url.getField(0).getValidationErrors().size());
    }
}
