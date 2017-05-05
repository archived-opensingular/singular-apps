package org.opensingular.server.commons.persistence.dao;

import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.metadata.ClassMetadata;
import org.opensingular.lib.support.persistence.SimpleDAO;

import javax.inject.Named;
import javax.transaction.Transactional;
import java.util.Map;

@Named
public class HealthSystemDAO extends SimpleDAO {

	@Transactional
	public Map<String, ClassMetadata> getAllDbMetaData(){		
		return sessionFactory.getAllClassMetadata();
	}
	
	@Transactional
	public String getHibernateDialect(){
		return ((SessionFactoryImpl)sessionFactory).getDialect().toString();
	}

}
