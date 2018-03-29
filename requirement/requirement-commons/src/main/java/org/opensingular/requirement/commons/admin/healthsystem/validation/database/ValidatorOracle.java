/*
 *
 *  * Copyright (C) 2016 Singular Studios (a.k.a Atom Tecnologia) - www.opensingular.com
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  * http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package org.opensingular.requirement.commons.admin.healthsystem.validation.database;

import org.hibernate.SQLQuery;
import org.hibernate.transform.ResultTransformer;
import org.opensingular.requirement.commons.persistence.dto.healthsystem.ColumnInfoDTO;
import org.opensingular.requirement.commons.persistence.dto.healthsystem.SequenceInfoDTO;

import javax.inject.Named;
import java.math.BigDecimal;
import java.util.List;

@Named
public class ValidatorOracle extends AbstractValidator {

	@Override
	protected List<ColumnInfoDTO> getColumnsInfoFromTable(String table) {
		String query = "SELECT OWNER, COLUMN_NAME, DATA_TYPE, CHAR_LENGTH, DATA_PRECISION, TABLE_NAME, DATA_LENGTH, NULLABLE "
				+ " FROM SYS.ALL_TAB_COLS "
				+ " WHERE TABLE_NAME = :nome_tabela";

		SQLQuery querySQL = getSession().createSQLQuery(query);
		querySQL.setParameter("nome_tabela", table);

		querySQL.setResultTransformer(new ResultTransformer() {

			@Override
			public Object transformTuple(Object[] obj, String[] arg1) {
				ColumnInfoDTO column = new ColumnInfoDTO();

				int i=0;
				column.setSchema((String) obj[i]);
				column.setColumnName((String) obj[++i]);
				column.setDataType((String) obj[++i]);
				column.setCharLength((BigDecimal) obj[++i]);
				column.setDataPrecision((BigDecimal) obj[++i]);
				column.setTableName((String) obj[++i]);
				column.setDataLength((BigDecimal) obj[++i]);
				column.setNullable("Y".equals((String) obj[++i]));
				column.setFoundDataBase(true);

				return column;
			}
			@Override
			public List transformList(List list) {
				return list;
			}
		});

		return querySQL.list();
	}

	/**
	 * Segundo Documentação Oracle:
	 *
	 * ALL_TAB_PRIVS_RECD describes the following types of grants:
	 * 		Object grants for which the current user is the grantee
	 * 		Object grants for which an enabled role or PUBLIC is the grantee
	 *
	 * @param table
	 * @return lista de String com o nome dos privilegios obtidos.
	 */
	@Override
	protected List<String> getPermissionSpecificTable(String table) {
		String query = " SELECT PRIVILEGE"
				+ " FROM SYS.ALL_TAB_PRIVS_RECD"
				+ " WHERE TABLE_NAME = :nome_tabela";

		SQLQuery querySQL = getSession().createSQLQuery(query);
		querySQL.setParameter("nome_tabela", table);

		return querySQL.list();
	}

	@Override
	protected SequenceInfoDTO getSequenceInfoDTO(String sequenceName) {
		String query = "SELECT "
				+ " seq.SEQUENCE_NAME, seq.LAST_NUMBER, seq.MIN_VALUE, seq.MAX_VALUE, seq.INCREMENT_BY "
				+ " FROM SYS.ALL_SEQUENCES seq  "
				+ " WHERE seq.SEQUENCE_NAME = :sequence_name";

		SQLQuery querySQL = getSession().createSQLQuery(query);
		querySQL.setParameter("sequence_name", sequenceName);

		querySQL.setResultTransformer(new ResultTransformer() {
			@Override
			public Object transformTuple(Object[] arg0, String[] arg1) {
				SequenceInfoDTO info = new SequenceInfoDTO();

				int i=0;
				info.setSequenceName((String) arg0[i]);
				info.setCurrentValue((BigDecimal) arg0[++i]);
				info.setMinValue((BigDecimal) arg0[++i]);
				info.setMaxValue((BigDecimal) arg0[++i]);
				info.setIncrement((BigDecimal) arg0[++i]);
				info.setFound(true);

				return info;
			}
			@Override
			public List transformList(List list) {
				return list;
			}
		});
		return (SequenceInfoDTO) querySQL.uniqueResult();
	}
}
