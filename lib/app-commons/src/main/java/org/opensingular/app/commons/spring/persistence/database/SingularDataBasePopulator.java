package org.opensingular.app.commons.spring.persistence.database;

import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.lib.support.persistence.DatabaseObjectNameReplacement;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.jdbc.datasource.init.ScriptException;

import javax.annotation.Nonnull;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SingularDataBasePopulator extends ResourceDatabasePopulator implements Loggable {

    private String                           sqlScriptEncoding;
    private PersistenceConfigurationProvider persistenceConfigurationProvider;

    public SingularDataBasePopulator(@Nonnull PersistenceConfigurationProvider persistenceConfigurationProvider) {
        this.persistenceConfigurationProvider = persistenceConfigurationProvider;
        this.setSqlScriptEncoding(StandardCharsets.UTF_8.name());
        this.setSeparator(";");
    }

    @Override
    public void populate(Connection connection) throws ScriptException {

        try {
            connection.setAutoCommit(false);
            Savepoint savepoint = connection.setSavepoint();
            try {
                super.addScript(new ByteArrayResource(formattedScriptsToExecute(persistenceConfigurationProvider).toString().getBytes(Charset.forName(this.sqlScriptEncoding)), "Singular Schema Export Hibernate DDL + SQL Files"));
                super.populate(connection);
                connection.commit();
                connection.setAutoCommit(true);
            } catch (Exception e) {
                connection.rollback(savepoint);
                getLogger().error("Error running the Database populator >>> {} ", e);
            }
        } catch (SQLException e) {
            getLogger().error("Error trying to set autocommit false >>> {}", e);
        }
    }

    /**
     * Method to create a StringBuilder with all scripts to execute.
     * The script contains the packages that have to create the entities, and set the replace schema or Catalog.
     *
     * @param persistenceConfigurationProvider The implementation of the interface that contains the packages to Scan, and some replaces that is necessary.
     * @return StringBuilder containg all DML and DDL Scripts.
     */
    private StringBuilder formattedScriptsToExecute(PersistenceConfigurationProvider persistenceConfigurationProvider) {
        StringBuilder scripts = SingularSchemaExport.generateScript(
                persistenceConfigurationProvider.getPackagesToScan(false),
                persistenceConfigurationProvider.getDialect(),
                persistenceConfigurationProvider.getSQLScritps()
        );
        for (DatabaseObjectNameReplacement schemaReplacement : persistenceConfigurationProvider.getSchemaReplacements()) {
            Pattern p     = Pattern.compile(Pattern.quote(schemaReplacement.getOriginalObjectName()));
            Matcher m     = p.matcher(scripts);
            int     start = 0;
            while (m.find(start)) {
                scripts.replace(m.start(), m.end(), schemaReplacement.getObjectNameReplacement());
                start = m.start() + schemaReplacement.getObjectNameReplacement().length();
            }
        }
        return scripts;
    }

    @Override
    public void setSqlScriptEncoding(String sqlScriptEncoding) {
        this.sqlScriptEncoding = sqlScriptEncoding;
        super.setSqlScriptEncoding(sqlScriptEncoding);
    }

}
