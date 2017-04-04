package org.opensingular.server.commons.test;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.opensingular.lib.support.spring.util.ApplicationContextProvider;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import javax.servlet.ServletException;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = TestConfiguration.class, loader = SingularServerContextLoader.class)
public abstract class SingularServerTestBase {


}
