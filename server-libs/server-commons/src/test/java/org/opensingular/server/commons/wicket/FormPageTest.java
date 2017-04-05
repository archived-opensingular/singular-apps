package org.opensingular.server.commons.wicket;

import org.junit.Test;
import org.opensingular.form.wicket.helpers.SingularWicketTester;
import org.opensingular.server.commons.STypeFOO;
import org.opensingular.server.commons.spring.security.DefaultUserDetailService;
import org.opensingular.server.commons.test.SingularApplicationMock;
import org.opensingular.server.commons.test.SingularServerBaseTest;
import org.opensingular.server.commons.wicket.view.form.FormPage;
import org.opensingular.server.commons.wicket.view.util.ActionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.web.servlet.HandlerAdapter;

import javax.inject.Inject;
import javax.transaction.Transactional;

public class FormPageTest extends SingularServerBaseTest {

    @Inject
    SingularApplicationMock singularApplication;
    @Inject
    DefaultUserDetailService defaultUserDetailService;
    @Inject
    private ApplicationContext applicationContext;
    private SingularWicketTester tester;

    public void setUp() {
        tester = new SingularWicketTester(singularApplication);
        MockHttpServletRequest  request;
        MockHttpServletResponse response;
        HandlerAdapter          handlerAdapter;
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        handlerAdapter = applicationContext.getBean(HandlerAdapter.class);
    }


    @WithUserDetails("vinicius.nunes")
    @Transactional
    @Test
    public void testFormPage() {
        setUp();
        ActionContext context = new ActionContext();
        context.setFormName(STypeFOO.class.getName());
        FormPage p = new FormPage(context);
        tester.startPage(p);
        tester.assertRenderedPage(FormPage.class);
    }

}
