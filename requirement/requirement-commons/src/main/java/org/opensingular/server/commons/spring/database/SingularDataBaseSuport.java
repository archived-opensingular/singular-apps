package org.opensingular.server.commons.spring.database;

import org.hibernate.dialect.Dialect;

public interface SingularDataBaseSuport {

    AbstractResourceDatabasePopulator getPopulatorBeanInstance();

    boolean isDialectSupported(Class<? extends Dialect> dialect);
}
