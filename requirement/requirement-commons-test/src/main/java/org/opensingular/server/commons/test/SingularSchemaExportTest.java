package org.opensingular.server.commons.test;

import java.util.List;

import org.hibernate.dialect.Dialect;
import org.junit.Test;
import org.opensingular.requirement.commons.test.db.SingularSchemaExport;

public abstract class SingularSchemaExportTest {

    static final String SCRIPT_FILE = "/src/main/resources/exportScript.sql";

    @Test
    public abstract void generateScriptByDialect();

    protected void generateScript(String scriptFilePath, String packageToScan, Class<? extends Dialect> dialect, List<String> scriptsPath) {

        String directoryName = scriptFilePath == null ? System.getProperty("user.dir") + SCRIPT_FILE : scriptFilePath;
        SingularSchemaExport.generateScript(packageToScan, dialect,
                directoryName, scriptsPath);
    }

}
