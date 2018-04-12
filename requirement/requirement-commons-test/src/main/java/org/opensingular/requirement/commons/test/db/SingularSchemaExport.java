package org.opensingular.requirement.commons.test.db;

import org.apache.logging.log4j.util.Strings;
import org.hibernate.cfg.Configuration;
import org.opensingular.lib.commons.scan.SingularClassPathScanner;
import org.opensingular.lib.commons.util.Loggable;

import javax.persistence.Entity;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public class SingularSchemaExport implements Loggable {

    public static final String SCRIPT_FILE = "exportScript.sql";

    public static final String H2         = "org.hibernate.dialect.H2Dialect";
    public static final String POSTGRE    = "org.hibernate.dialect.PostgreSQLDialect";
    public static final String ORACLE     = "org.hibernate.dialect.OracleDialect";
    public static final String ORACLE_8I  = "org.hibernate.dialect.Oracle8iDialect";
    public static final String ORACLE_9I  = "org.hibernate.dialect.Oracle9iDialect";
    public static final String ORACLE_10G = "org.hibernate.dialect.Oracle10gDialect";
    public static final String SQLSERVER  = "org.hibernate.dialect.SQLServerDialect";


    public void generateScript() {
        generateScript(null, null, null);
    }

    public void generateScript(String packageStr, String dialect, String filename) {

        Set<Class<?>> typesAnnotatedWith = SingularClassPathScanner.get().findClassesAnnotatedWith(Entity.class);
        List<Class<?>> list = typesAnnotatedWith.stream().filter(c ->
                Strings.isNotEmpty(packageStr) ? c.getPackage().getName().startsWith(packageStr) : true
        ).collect(Collectors.toList());


        //create a minimal configuration
        Configuration cfg = new Configuration();
        cfg.setProperty("hibernate.dialect", Strings.isNotEmpty(dialect) ? dialect : H2);
        cfg.setProperty("hibernate.hbm2ddl.auto", "create");

        for (Class<?> c : list) {
            cfg.addAnnotatedClass(c);
        }

        //build all the mappings, before calling the AuditConfiguration
        cfg.buildMappings();

        //execute the export
        org.hibernate.tool.hbm2ddl.SchemaExport export = new org.hibernate.tool.hbm2ddl.SchemaExport(cfg);

        export.setOutputFile(Strings.isNotEmpty(filename) ? filename : SCRIPT_FILE);
        export.setDelimiter(";");
        export.setFormat(true);

        export.execute(true, false, false, true);
    }

}
