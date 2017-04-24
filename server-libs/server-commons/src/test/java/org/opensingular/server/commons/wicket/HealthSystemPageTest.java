package org.opensingular.server.commons.wicket;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.markup.html.panel.Panel;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.opensingular.form.wicket.helpers.AssertionsWComponent;
import org.opensingular.form.wicket.helpers.SingularWicketTester;
import org.opensingular.server.commons.test.CommonsApplicationMock;
import org.opensingular.server.commons.test.SingularCommonsBaseTest;
import org.opensingular.server.commons.test.SingularServletContextTestExecutionListener;
import org.opensingular.server.p.commons.admin.healthsystem.HealthSystemPage;
import org.opensingular.server.p.commons.admin.healthsystem.panel.CachePanel;
import org.opensingular.server.p.commons.admin.healthsystem.panel.DbPanel;
import org.opensingular.server.p.commons.admin.healthsystem.panel.JobPanel;
import org.opensingular.server.p.commons.admin.healthsystem.panel.PermissionPanel;
import org.opensingular.server.p.commons.admin.healthsystem.panel.WebPanel;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestExecutionListeners;

import javax.inject.Inject;
import javax.transaction.Transactional;

@TestExecutionListeners(listeners = {SingularServletContextTestExecutionListener.class}, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
public class HealthSystemPageTest extends SingularCommonsBaseTest {

    @Inject
    CommonsApplicationMock singularApplication;

    private SingularWicketTester tester;

    @WithUserDetails("vinicius.nunes")
    @Transactional
    @Test
    public void testHealthSystemPageRendering() {
        tester = new SingularWicketTester(singularApplication);
        Page p = new HealthSystemPage();
        tester.startPage(p);
        tester.assertRenderedPage(HealthSystemPage.class);
    }

    @WithUserDetails("vinicius.nunes")
    @Transactional
    @Test
    @Ignore("DELFINO")
    public void testClickDbButton() {
        clickButtonAndCheckPanel("buttonDb", DbPanel.class);
    }

    @WithUserDetails("vinicius.nunes")
    @Transactional
    @Test
    public void testClickCacheButton() {
        clickButtonAndCheckPanel("buttonCache", CachePanel.class);
    }

    @WithUserDetails("vinicius.nunes")
    @Transactional
    @Test
    public void testClickJobButton() {
        clickButtonAndCheckPanel("buttonJobs", JobPanel.class);
    }

    @WithUserDetails("vinicius.nunes")
    @Transactional
    @Test
    public void testClickPermissionButton() {
        clickButtonAndCheckPanel("buttonPermissions", PermissionPanel.class);
    }

    @WithUserDetails("vinicius.nunes")
    @Transactional
    @Test
    public void testClickWebButton() {
        clickButtonAndCheckPanel("buttonWeb", WebPanel.class);
    }

    private void clickButtonAndCheckPanel(String buttonId, Class<? extends Panel> clazz) {
        tester = new SingularWicketTester(singularApplication);
        Page p = new HealthSystemPage();
        tester.startPage(p);
        Component buttonDb = new AssertionsWComponent(p).getSubCompomentWithId(buttonId).getTarget();
        tester.executeAjaxEvent(buttonDb, "click");
        Component container = new AssertionsWComponent(p).getSubCompomentWithId("containerAllContent").getTarget();
        if (tester.isRenderedPage(HealthSystemPage.class).wasFailed()
                || !clazz.isInstance(container)) {
            Assert.fail(String.format("O botão de id '%s' está lançando erro ao ser clicado.", buttonId));
        }
    }
}
