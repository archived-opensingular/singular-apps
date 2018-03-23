package org.opensingular.server.commons.test;

import java.util.Arrays;
import java.util.List;

import org.hibernate.dialect.SQLServer2008Dialect;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opensingular.server.commons.exception.ResourceDatabasePopularException;
import org.opensingular.server.commons.spring.SingularDefaultPersistenceConfiguration;
import org.opensingular.server.commons.spring.database.AbstractResourceDatabasePopulator;
import org.opensingular.server.commons.spring.database.SingularDataBaseEnum;
import org.opensingular.server.commons.spring.database.SingularDataBaseSuport;
import org.opensingular.server.commons.spring.database.SingularSchemaExport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader=AnnotationConfigContextLoader.class)
public class SingularSchemaExportTest {

    static final String SCRIPT_FILE = "/src/main/resources/db/ddl/exportScript.sql";


    @Configuration
    static class ContextConfiguration {

        @Bean
        public AbstractResourceDatabasePopulator databasePopulator() {
            return getSupportedDatabases()
                    .stream()
                    .filter(f -> f.isDialectSupported(SQLServer2008Dialect.class))
                    .findFirst()
                    .map(SingularDataBaseSuport::getPopulatorBeanInstance)
                    .orElseThrow(() -> new ResourceDatabasePopularException("Dialect not Supported. Look for supported values in " + SingularDefaultPersistenceConfiguration.class + ".getSupportedDatabases()"));
        }
    }

    @Autowired
    private AbstractResourceDatabasePopulator databasePopulator;

    @Test
    public void generateScript() {
        String directoryName = System.getProperty("user.dir") + SCRIPT_FILE;
        new SingularSchemaExport().generateScript("org.opensingular", SQLServer2008Dialect.class,
                directoryName, databasePopulator.getScriptsText());
    }

    public static List<SingularDataBaseEnum> getSupportedDatabases() {
        return Arrays.asList(SingularDataBaseEnum.values());
    }


}
