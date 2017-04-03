import org.junit.Test;
import org.junit.runner.RunWith;
import org.opensingular.server.commons.config.SingularAnnotationConfigWebContextLoader;
import org.opensingular.server.commons.config.TestInitializer;
import org.slf4j.LoggerFactory;
import org.slf4j.impl.SimpleLoggerFactory;
import org.slf4j.impl.StaticLoggerBinder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;

import javax.inject.Inject;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = TestInitializer.class, loader = SingularAnnotationConfigWebContextLoader.class)
public class ServerTestBase {

    private StaticLoggerBinder binder = StaticLoggerBinder.getSingleton();

    @Inject
    protected WebApplicationContext webApplicationContext;


    @Test
    public void donothing() {

    }
}
