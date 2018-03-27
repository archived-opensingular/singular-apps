package org.opensingular.server.commons.test;

import java.util.List;

import org.hibernate.dialect.Dialect;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opensingular.server.commons.spring.SingularDefaultPersistenceConfiguration;
import org.opensingular.server.commons.test.db.SingularSchemaExport;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SingularDefaultPersistenceConfiguration.class)
public abstract class SingularSchemaExportTest {

    static final String SCRIPT_FILE = "/src/main/resources/exportScript.sql";

    @Test
    public abstract void generateScriptByDialect();

    public void generateScript(String packageToScan, Class<? extends Dialect> dialect, List<String> scriptsPath) {

        String directoryName = System.getProperty("user.dir") + SCRIPT_FILE;
        SingularSchemaExport.generateScript(packageToScan, dialect,
                directoryName, scriptsPath);
    }

}
