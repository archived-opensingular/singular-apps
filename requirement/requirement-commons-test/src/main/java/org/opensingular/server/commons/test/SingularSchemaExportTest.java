package org.opensingular.server.commons.test;

import org.junit.Test;
import org.opensingular.app.commons.spring.persistence.database.ConfigurationProcessor;
import org.opensingular.requirement.commons.test.db.SingularSchemaExport;

public abstract class SingularSchemaExportTest {

    static final String SCRIPT_FILE = "/src/main/resources/exportScript.sql";

    @Test
    public abstract void generateScriptByDialect();

    protected void generateScript() {
        generateScript(System.getProperty("user.dir") + SCRIPT_FILE);
    }

    protected void generateScript(String scriptFilePath) {
        ConfigurationProcessor configurationProcessor = getPersistenceConfiguration();
        SingularSchemaExport.generateScript(
                configurationProcessor.getPackagesToScan(),
                configurationProcessor.getDialect(),
                scriptFilePath,
                configurationProcessor.getSQLScritps()
        );
    }

    protected ConfigurationProcessor getPersistenceConfiguration() {
        return new ConfigurationProcessor();
    }

}
