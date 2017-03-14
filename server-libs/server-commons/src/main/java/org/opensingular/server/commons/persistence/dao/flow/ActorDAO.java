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

package org.opensingular.server.commons.persistence.dao.flow;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.opensingular.flow.core.MUser;
import org.opensingular.flow.persistence.entity.Actor;
import org.opensingular.lib.commons.base.SingularProperties;
import org.opensingular.lib.support.persistence.BaseDAO;
import org.opensingular.lib.support.persistence.util.Constants;
import org.opensingular.lib.support.persistence.util.SqlUtil;
import org.opensingular.server.commons.exception.SingularServerException;
import org.opensingular.server.commons.persistence.transformer.FindActorByUserCodResultTransformer;

import javax.annotation.Nonnull;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


public class ActorDAO extends BaseDAO<Actor, Integer> {

    public ActorDAO() {
        super(Actor.class);
    }

    public Actor buscarPorCodUsuario(String username) {

        if (username == null) {
            return null;
        }

        Query query = getSession().createSQLQuery(
                "select a.CO_ATOR as \"cod\", a.CO_USUARIO as \"codUsuario\", a.NO_ATOR as \"nome\", a.DS_EMAIL as \"email\" " +
                        " FROM " + Constants.SCHEMA + ".VW_ATOR a " +
                        " WHERE UPPER(trim(a.CO_USUARIO)) = :codUsuario");

        query.setParameter("codUsuario", username.toUpperCase());
        query.setResultTransformer(new FindActorByUserCodResultTransformer());

        return (Actor) query.uniqueResult();

    }

    public MUser saveUserIfNeeded(MUser mUser) {
        if (mUser == null) {
            return null;
        }

        Integer cod        = mUser.getCod();
        String  codUsuario = mUser.getCodUsuario();

        return saveUserIfNeeded(cod, codUsuario).orElse(null);
    }

    public Optional<MUser> saveUserIfNeeded(@Nonnull String codUsuario) {
        return saveUserIfNeeded(null, Objects.requireNonNull(codUsuario));
    }

    private Optional<MUser> saveUserIfNeeded(Integer cod, String codUsuario) {
        MUser result = null;
        if (cod != null) {
            result = (MUser) getSession().createCriteria(Actor.class).add(Restrictions.eq("cod", cod)).uniqueResult();
        }

        if (result == null && codUsuario != null) {
            result = (MUser) getSession().createCriteria(Actor.class).add(Restrictions.ilike("codUsuario", codUsuario)).uniqueResult();
        }

        if (result == null && cod == null) {
            if ("sequence".equals(SingularProperties.get().getProperty(SingularProperties.HIBERNATE_GENERATOR))) {
                getSession().doWork(connection -> {
                    String            sql = SqlUtil.replaceSchemaName("insert into " + Constants.SCHEMA + ".TB_ATOR (CO_ATOR, CO_USUARIO) VALUES (" + Constants.SCHEMA + ".SQ_CO_ATOR.NEXTVAL, ? )");
                    PreparedStatement ps  = connection.prepareStatement(sql);
                    ps.setString(1, codUsuario);
                    ps.execute();
                });
            } else {
                getSession().doWork(connection -> {
                    String            sql = SqlUtil.replaceSchemaName("insert into " + Constants.SCHEMA + ".TB_ATOR (CO_USUARIO) VALUES (?)");
                    PreparedStatement ps = connection.prepareStatement(sql);
                    ps.setString(1, codUsuario);
                    ps.execute();
                });
            }
            getSession().flush();
            result = (MUser) getSession().createCriteria(Actor.class).add(Restrictions.eq("codUsuario", codUsuario)).uniqueResult();

            if (result == null) {
                throw SingularServerException.rethrow("Usuário que deveria ter sido criado não pode ser recuperado.");
            }
        }
        return Optional.ofNullable(result);
    }

    @SuppressWarnings("unchecked")
    public List<Actor> listAllocableUsers(Integer taskInstanceId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT DISTINCT a.CO_ATOR AS \"cod\",");
        sql.append("   a.CO_USUARIO            AS \"codUsuario\",");
        sql.append("   UPPER(a.NO_ATOR)        AS \"nome\",");
        sql.append("   a.DS_EMAIL              AS \"email\"");
        sql.append(" FROM "+ Constants.SCHEMA +" .VW_ATOR a");
        sql.append(" ORDER BY UPPER(a.NO_ATOR)");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.addScalar("cod", StandardBasicTypes.INTEGER);
        query.addScalar("codUsuario", StandardBasicTypes.STRING);
        query.addScalar("nome", StandardBasicTypes.STRING);
        query.addScalar("email", StandardBasicTypes.STRING);
        query.setResultTransformer(Transformers.aliasToBean(Actor.class));
        return query.list();
    }
}
