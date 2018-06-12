package org.opensingular.app.commons.spring.persistence.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;
import javax.annotation.Nonnull;

import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.lib.support.persistence.DatabaseObjectNameReplacement;
import org.opensingular.lib.support.persistence.util.SqlUtil;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ScriptException;

public class SingularDataBasePopulator implements DatabasePopulator, Loggable {

    private StringBuilder scripts;

    public SingularDataBasePopulator(@Nonnull PersistenceConfigurationProvider persistenceConfigurationProvider) {
        this.scripts = formattedScriptsToExecute(persistenceConfigurationProvider);
    }

    private StringBuilder formattedScriptsToExecute(PersistenceConfigurationProvider persistenceConfigurationProvider) {
        StringBuilder scripts = SingularSchemaExport.generateScript(
                persistenceConfigurationProvider.getPackagesToScan(false),
                persistenceConfigurationProvider.getDialect(),
                null,
                persistenceConfigurationProvider.getSQLScritps());
        for (DatabaseObjectNameReplacement schemaReplacement : persistenceConfigurationProvider.getSchemaReplacements()) {
            scripts = new StringBuilder(SqlUtil.replaceSchemaName(scripts.toString(),
                    schemaReplacement.getOriginalObjectName(),
                    schemaReplacement.getObjectNameReplacement()));
        }
        return scripts;
    }


    @Override
    public void populate(Connection connection) throws SQLException, ScriptException {
        try (PreparedStatement ps = connection.prepareStatement(scripts.toString())) {
            ps.execute();
            Logger.getLogger(SingularSchemaExport.class.getName()).info(scripts.toString());
        }

    }


}
