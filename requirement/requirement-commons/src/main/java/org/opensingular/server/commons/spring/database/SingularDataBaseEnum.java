package org.opensingular.server.commons.spring.database;

import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.H2Dialect;
import org.hibernate.dialect.Oracle8iDialect;
import org.hibernate.dialect.SQLServerDialect;

public enum SingularDataBaseEnum implements SingularDataBaseSuport {

    ORACLE(new OracleResourceDatabasePopulator(), Oracle8iDialect.class),
    MSSQL(new MSSQLResourceDatabasePopulator(), SQLServerDialect.class),
    H2(new H2ResourceDatabasePopulator(), H2Dialect.class);

    private AbstractResourceDatabasePopulator resourceDatabasePopulator;
    private Class<? extends Dialect> dialect;

    SingularDataBaseEnum(AbstractResourceDatabasePopulator resourceDatabasePopulator, Class<? extends Dialect> dialect) {
        this.dialect = dialect;
        this.resourceDatabasePopulator = resourceDatabasePopulator;
    }

    @Override
    public AbstractResourceDatabasePopulator getPopulatorBeanInstance() {
        return resourceDatabasePopulator;
    }

    @Override
    public boolean isDialectSupported(Class<? extends Dialect> dialect) {
        return  this.dialect.isAssignableFrom(dialect);
    }


}
