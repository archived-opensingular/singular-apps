package org.opensingular.server.commons.test;

import javax.inject.Inject;

import org.hibernate.dialect.SQLServer2008Dialect;
import org.junit.Test;
import org.opensingular.server.commons.spring.database.AbstractResourceDatabasePopulator;
import org.opensingular.server.commons.spring.database.SingularSchemaExport;
import org.springframework.test.context.TestExecutionListeners;

@TestExecutionListeners(listeners = {SingularServletContextTestExecutionListener.class},
        mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
public class SingularSchemaExportTest extends SingularCommonsBaseTest {

    static final String SCRIPT_FILE = "/src/main/resources/exportScript.sql";

    @Inject
    private AbstractResourceDatabasePopulator databasePopulator;

    @Test
    public void generateScript() {

        String directoryName = System.getProperty("user.dir") + SCRIPT_FILE;
        SingularSchemaExport.generateScript("org.opensingular", SQLServer2008Dialect.class,
                directoryName, databasePopulator.getScriptsText());
    }

}
