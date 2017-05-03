package org.opensingular.server.commons.wicket;

import org.apache.wicket.behavior.AbstractAjaxBehavior;
import org.apache.wicket.behavior.Behavior;
import org.junit.Test;
import org.opensingular.form.wicket.helpers.AssertionsWComponent;
import org.opensingular.form.wicket.helpers.SingularWicketTester;
import org.opensingular.lib.wicket.util.menu.MetronicMenu;
import org.opensingular.server.commons.test.CommonsApplicationMock;
import org.opensingular.server.commons.test.SingularCommonsBaseTest;
import org.opensingular.server.commons.test.SingularServletContextTestExecutionListener;
import org.opensingular.server.commons.wicket.view.template.Menu;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestExecutionListeners;

import javax.inject.Inject;

@TestExecutionListeners(listeners = {SingularServletContextTestExecutionListener.class}, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
public class TemplatePageTest extends SingularCommonsBaseTest {

    @Inject
    private CommonsApplicationMock singularApplication;

    private SingularWicketTester tester;

    @WithUserDetails("vinicius.nunes")
    @Test
    public void testFormPageRendering() {
        tester = new SingularWicketTester(singularApplication);
        FooTemplatePage page = tester.startPage(FooTemplatePage.class);
        MetronicMenu            m    = (MetronicMenu) new AssertionsWComponent(page).getSubComponents(MetronicMenu.class).first().getTarget();
        for (Behavior b : m.getBehaviors()) {
            if (b instanceof AbstractAjaxBehavior) {
                tester.executeBehavior((AbstractAjaxBehavior) b);
            }
        }
        tester.assertRenderedPage(FooTemplatePage.class);
    }


}
