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

package org.opensingular.requirement.module.persistence.dao.flow;

import org.hibernate.criterion.Restrictions;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.opensingular.flow.core.SUser;
import org.opensingular.flow.persistence.entity.Actor;
import org.opensingular.lib.support.persistence.BaseDAO;
import org.opensingular.lib.support.persistence.util.Constants;
import org.opensingular.lib.support.persistence.util.SqlUtil;
import org.opensingular.requirement.module.exception.SingularServerException;
import org.opensingular.requirement.module.persistence.transformer.FindActorByUserCodResultTransformer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


public class ActorDAO extends BaseDAO<Actor, Integer> {

    public ActorDAO() {
        super(Actor.class);
    }


    public Actor retrieveByUserCod(String userName) {

        if (userName == null) {
            return null;
        }

        Query query = getSession().createNativeQuery(
                "select a.CO_ATOR as \"cod\", a.CO_USUARIO as \"codUsuario\", a.NO_ATOR as \"nome\", a.DS_EMAIL as \"email\" " +
                        " FROM " + Constants.SCHEMA + ".VW_ATOR a " +
                        " WHERE UPPER(rtrim(ltrim(a.CO_USUARIO))) = :codUsuario");

        query.setParameter("codUsuario", userName.toUpperCase());
        query.setResultTransformer(new FindActorByUserCodResultTransformer());

        return (Actor) query.uniqueResult();

    }

    public SUser saveOrUpdateUserIfNeeded(SUser sUser) {
        if (sUser == null) {
            return null;
        }

        Integer cod        = sUser.getCod();
        String  codUsuario = sUser.getCodUsuario();

        return saveOrUpdateUserIfNeeded(cod, codUsuario, sUser.getSimpleName(), sUser.getEmail()).orElse(null);
    }

    public Optional<SUser> saveOrUpdateUserIfNeeded(@Nonnull String codUsuario) {
        return saveOrUpdateUserIfNeeded(null, Objects.requireNonNull(codUsuario));
    }

    private Optional<SUser> saveOrUpdateUserIfNeeded(Integer cod, String codUsuario) {
        return saveOrUpdateUserIfNeeded(cod, codUsuario, codUsuario, null);
    }

    private Optional<SUser> saveOrUpdateUserIfNeeded(Integer cod, String codUsuario, @Nullable String name, @Nullable String email) {
        SUser result = null;
        if (cod != null) {
            result = (SUser) getSession().createCriteria(Actor.class).add(Restrictions.eq("cod", cod)).uniqueResult();
        }

        if (result == null && codUsuario != null) {
            result = (SUser) getSession().createCriteria(Actor.class).add(Restrictions.ilike("codUsuario", codUsuario)).uniqueResult();
        }

        if (result == null && cod == null) {
            Dialect dialect = ((SessionFactoryImplementor) getSession().getSessionFactory()).getDialect();
            if (dialect.getNativeIdentifierGeneratorStrategy().equals("sequence") && dialect.supportsSequences()) {
                dialect.getSequenceNextValString("nada");
                getSession().doWork(connection -> {
                    String sql = SqlUtil.replaceSingularSchemaName("insert into "
                            + Constants.SCHEMA + ".TB_ATOR (CO_ATOR, CO_USUARIO, NO_ATOR, DS_EMAIL) VALUES (("
                            + dialect.getSequenceNextValString(Constants.SCHEMA + ".SQ_CO_ATOR") + ")" + ", ?,?,? )");
                    PreparedStatement ps = connection.prepareStatement(sql);
                    ps.setString(1, codUsuario);
                    ps.setString(2, name);
                    ps.setString(3, email);
                    ps.executeUpdate();
                });
            } else {
                getSession().doWork(connection -> {
                    String            sql = SqlUtil.replaceSingularSchemaName("insert into " + Constants.SCHEMA + ".TB_ATOR (CO_USUARIO, NO_ATOR, DS_EMAIL) VALUES (?)");
                    PreparedStatement ps  = connection.prepareStatement(sql);
                    ps.setString(1, codUsuario);
                    ps.setString(2, name);
                    ps.setString(3, email);
                    ps.executeUpdate();
                });
            }
            getSession().flush();
            result = (SUser) getSession().createCriteria(Actor.class).add(Restrictions.eq("codUsuario", codUsuario)).uniqueResult();

            if (result == null) {
                throw SingularServerException.rethrow("Usuário que deveria ter sido criado não pode ser recuperado.");
            }
        }

        if (result != null
                && (!Objects.equals(result.getSimpleName(), name)
                    || !Objects.equals(result.getEmail(), email))) {
            String finalName;
            if (Objects.equals(name, codUsuario)) {
                finalName = result.getSimpleName();
            } else {
                finalName = name;
            }
            getSession().doWork(connection -> {
                String            sql = SqlUtil.replaceSingularSchemaName("UPDATE " + Constants.SCHEMA + ".TB_ATOR SET NO_ATOR = ?, DS_EMAIL = ? WHERE CO_USUARIO = ?");
                PreparedStatement ps  = connection.prepareStatement(sql);
                ps.setString(1, finalName);
                ps.setString(2, email);
                ps.setString(3, codUsuario);
                ps.executeUpdate();
            });
            getSession().flush();
            getSession().refresh(result);
        }

        return Optional.ofNullable(result);
    }

    @SuppressWarnings("unchecked")
    public List<Actor> listAllowedUsers(Integer taskInstanceId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT DISTINCT a.CO_ATOR AS \"cod\",");
        sql.append("   a.CO_USUARIO            AS \"codUsuario\",");
        sql.append("   UPPER(a.NO_ATOR)        AS \"nome\",");
        sql.append("   a.DS_EMAIL              AS \"email\"");
        sql.append(" FROM " + Constants.SCHEMA + " .VW_ATOR a");
        sql.append(" ORDER BY UPPER(a.NO_ATOR)");
        NativeQuery<Actor> query = getSession().createNativeQuery(sql.toString());
        query.addScalar("cod", StandardBasicTypes.INTEGER);
        query.addScalar("codUsuario", StandardBasicTypes.STRING);
        query.addScalar("nome", StandardBasicTypes.STRING);
        query.addScalar("email", StandardBasicTypes.STRING);
        query.setResultTransformer(Transformers.aliasToBean(Actor.class));
        return query.list();
    }
}
