package org.opensingular.server.commons.test;

import org.junit.Test;
import org.opensingular.app.commons.spring.persistence.database.PersistenceConfigurationProvider;
import org.opensingular.app.commons.spring.persistence.database.SingularSchemaExport;

public abstract class SingularSchemaExportTest {

    static final String SCRIPT_FILE = "/src/main/resources/exportScript.sql";

    @Test
    public abstract void exportScriptToFile();

    protected void generateScript() {
        generateScript(System.getProperty("user.dir") + SCRIPT_FILE);
    }

    protected void generateScript(String scriptFilePath) {
        PersistenceConfigurationProvider persistenceConfigurationProvider = getPersistenceConfiguration();
        SingularSchemaExport.generateScript(
                persistenceConfigurationProvider.getPackagesToScan(false),
                persistenceConfigurationProvider.getDialect(),
                scriptFilePath,
                persistenceConfigurationProvider.getSQLScritps()
        );
    }

    protected PersistenceConfigurationProvider getPersistenceConfiguration() {
        return new PersistenceConfigurationProvider();
    }

}
