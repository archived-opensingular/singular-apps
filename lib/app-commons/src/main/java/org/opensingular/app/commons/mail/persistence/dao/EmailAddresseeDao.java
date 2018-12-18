/*
 * Copyright (C) 2016 Singular Studios (a.k.a Atom Tecnologia) - www.opensingular.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.opensingular.app.commons.mail.persistence.dao;

import org.opensingular.app.commons.mail.persistence.entity.email.EmailAddresseeEntity;
import org.opensingular.lib.support.persistence.BaseDAO;

import javax.transaction.Transactional;
import java.util.List;

@SuppressWarnings("unchecked")
@Transactional(Transactional.TxType.MANDATORY)
public class EmailAddresseeDao<T extends EmailAddresseeEntity> extends BaseDAO<T, Long> {

    public EmailAddresseeDao() {
        super((Class<T>) EmailAddresseeEntity.class);
    }

    public EmailAddresseeDao(Class<T> entityClass) {
        super(entityClass);
    }

    public int countPending() {
        return ((Long) getSession()
                .createQuery("select count(e.cod) from " + entityClass.getName() + " e where e.sentDate is null ")
                .uniqueResult()).intValue();
    }

    public List<T> listPending(int firstResult, int maxResults) {
        return (List<T>) getSession()
                .createQuery("select e from " + entityClass.getName() + " e where e.sentDate is null order by e.cod asc")
                .setFirstResult(firstResult)
                .setMaxResults(maxResults)
                .list();
    }
}
