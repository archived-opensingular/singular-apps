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

package org.opensingular.requirement.module.admin.healthsystem.validation.database;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Named;
import javax.transaction.Transactional;

import org.opensingular.lib.support.persistence.SimpleDAO;
import org.opensingular.requirement.module.persistence.dto.healthsystem.ColumnInfoDTO;
import org.opensingular.requirement.module.persistence.dto.healthsystem.SequenceInfoDTO;
import org.opensingular.requirement.module.persistence.dto.healthsystem.TableInfoDTO;

@Named
public abstract class AbstractValidator extends SimpleDAO implements IValidatorDatabase {


	@Transactional
	@Override
	public List<TableInfoDTO> getAllInfoTable(List<String> table) {
		List<TableInfoDTO> privileges = new ArrayList<>();

		table.forEach(tableName -> {
			TableInfoDTO tableInfo = new TableInfoDTO();
			tableInfo.setTableName(tableName);

			tableInfo.setUserPrivs(getPermissionSpecificTable(tableName));
			privileges.add(tableInfo);

			if (!tableInfo.getUserPrivs().isEmpty()) {
				tableInfo.setColumnsInfo(getColumnsInfoFromTable(tableName));
				if (tableInfo.getColumnsInfo() != null && !tableInfo.getColumnsInfo().isEmpty()) tableInfo.setSchema(
						tableInfo.getColumnsInfo().get(0).getSchema());
			}
		});

		return privileges;
	}

	@Transactional
	@Override
	public void checkColumnPermissions(TableInfoDTO tableInfoDTO) {

		List<ColumnInfoDTO> columns = getColumnsInfoFromTable(tableInfoDTO.getTableName());

		// Verifica se as columnsInfo encontradas no banco foi encontrada no hibernate
		// caso nao tenha sido, indica isso
		columns.forEach(column -> {
			boolean databaseColumnFoundInHibernate = false;
			for (ColumnInfoDTO col: tableInfoDTO.getColumnsInfo()) {
				if (col.getColumnName().equals(column.getColumnName())) {
					databaseColumnFoundInHibernate = true;
					column.setFoundHibernate(true);
					break;
				}
			}

			if(!databaseColumnFoundInHibernate){
				column.setFoundHibernate(false); // garantir que o valor é de que não foi encontrado
			}
		});

		// Verifica se as columnsInfo encontradas no hibernate foram encontradas no banco
		// caso nao tenha sido, indica isso
		tableInfoDTO.getColumnsInfo().forEach(tableCol->{
			boolean hibernateColumnFoundInDatabase = false;
			for (ColumnInfoDTO col : columns) {
				if(col.getColumnName().equals(tableCol.getColumnName())) {
					hibernateColumnFoundInDatabase = true;
					break;
				}
			}

			if(!hibernateColumnFoundInDatabase){
				tableCol.setFoundHibernate(true);
				tableCol.setFoundDataBase(false);
				columns.add(tableCol);
			}
		});

		tableInfoDTO.setColumnsInfo(columns);
	}

	/**
	 * Recupera a lista de columnsInfo para a respectiva tabela.
	 * @param table
	 * @return
	 */
	protected abstract List<ColumnInfoDTO> getColumnsInfoFromTable(String table);

	/**
	 * Recupera a lista de permissões que o usuário atual
	 * tem na tabela passada como parâmetro
	 * @param table
	 * @return
	 */
	protected abstract List<String> getPermissionSpecificTable(String table);

	@Override
	@Transactional
	public List<TableInfoDTO> getTablesPermission(List<TableInfoDTO> tables) {
		tables.forEach(table -> setFoundAndUserPermissionFromTable(table));
		return tables;
	}

	private void setFoundAndUserPermissionFromTable(TableInfoDTO table) {
		List<String> permissions = getPermissionSpecificTable(table.getTableName());
		table.setUserPrivs(permissions);
		if(permissions != null && !permissions.isEmpty()){
			table.setFound(true);
		}else{
			table.setFound(false);
		}
	}

	@Override
	@Transactional
	public List<SequenceInfoDTO> checkSequences(List<String> sequencesName) {

		List<SequenceInfoDTO> sequences = new ArrayList<>();

		sequencesName.forEach(sequenceName->{
			SequenceInfoDTO info = getSequenceInfoDTO(sequenceName);
			if(info == null){
				info = new SequenceInfoDTO();
				info.setSequenceName(sequenceName);
				info.setFound(false);
			}
			sequences.add(info);
		});
		return sequences;
	}

	/**
	 * Recupera as informações de sequence no banco de dados
	 * @param sequenceName
	 * @return
	 */
	protected abstract SequenceInfoDTO getSequenceInfoDTO(String sequenceName);

	@Override
	@Transactional
	public void checkAllInfoTable(List<TableInfoDTO> tables) {

		tables.forEach(table->{
			setFoundAndUserPermissionFromTable(table);
			checkColumnPermissions(table);

			if(table.getSchema() == null
					&& table.getColumnsInfo() != null && !table.getColumnsInfo().isEmpty()){
				table.setSchema(table.getColumnsInfo().get(0).getSchema());
			}
		});
	}
}
