package org.opensingular.server.commons.db;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.Entity;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.util.Strings;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.opensingular.lib.commons.scan.SingularClassPathScanner;
import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.server.commons.RESTPaths;
import org.opensingular.server.commons.exception.ExportScriptGenerationException;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;


public class SingularSchemaExport implements Loggable {


    public static final String SCRIPT_FILE = "/src/main/resources/db/ddl/exportScript.sql";

    public static final String H2 = "org.hibernate.dialect.H2Dialect";
    public static final String POSTGRE = "org.hibernate.dialect.PostgreSQLDialect";
    public static final String ORACLE = "org.hibernate.dialect.OracleDialect";
    public static final String ORACLE_8I = "org.hibernate.dialect.Oracle8iDialect";
    public static final String ORACLE_9I = "org.hibernate.dialect.Oracle9iDialect";
    public static final String ORACLE_10G = "org.hibernate.dialect.Oracle10gDialect";
    public static final String SQLSERVER = "org.hibernate.dialect.SQLServerDialect";
    public static final String SQLSERVER_2012 =  "org.hibernate.dialect.SQLServer2012Dialect";

    public static Resource generateScript(String packageStr, String dialect, String directoryFileName) {
        try {
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

            RESTPaths.class.getClassLoader().getResource("db/ddl/drops.sql");


            //execute the export
            org.hibernate.tool.hbm2ddl.SchemaExport export = new org.hibernate.tool.hbm2ddl.SchemaExport(cfg);


            if (Strings.isEmpty(directoryFileName)) {
                File tempFile = File.createTempFile("schema-singular", ".sql");
                exportParameters(export,tempFile.getAbsolutePath());
                StringBuilder sqls = new StringBuilder("");
                for (String sql : FileUtils.readLines(tempFile, StandardCharsets.UTF_8.name())) {
                    sqls.append(sql);
                }
                return new ByteArrayResource(sqls.toString().getBytes());
            } else {
                exportParameters(export, directoryFileName);
                return null;
            }


        } catch (Exception e) {
            throw new ExportScriptGenerationException(e.getMessage(), e);
        }
    }

    private static void exportParameters(SchemaExport export, String outputFile) {
        export.setOutputFile(outputFile)
                .setDelimiter(";")
                .setFormat(true)
                .execute(true, false, false, true);
    }

}
