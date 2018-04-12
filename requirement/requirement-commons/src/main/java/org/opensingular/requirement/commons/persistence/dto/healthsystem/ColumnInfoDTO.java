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

package org.opensingular.requirement.commons.persistence.dto.healthsystem;

import java.io.Serializable;
import java.math.BigDecimal;

@SuppressWarnings("serial")
public class ColumnInfoDTO implements Serializable {
	
	private String schema;
	private String tableName;
	private String columnName;
	private String dataType;
	
	/**
	 * Mostra a quantidade de char para os tipos: CHAR, VARCHAR2, NCHAR, NVARCHAR
	 * 
	 * Caso o dataType da coluna seja diferente dos mencionados, então o valor será zero.
	 */
	private BigDecimal charLength;
	
	/**
	 * Precisao DECIMAL de valores do tipo NUMBER
	 * Se for FLOAT entao a precisao é em BINARIO
	 * 
	 * NULL para outros tipos de coluna
	 */
	private BigDecimal dataPrecision;
	
	/**
	 * Length of the column (in bytes)
	 */
	private BigDecimal dataLength;
	
	private boolean nullable;
	
	private boolean foundHibernate = false;
	private boolean foundDataBase = false;
	
	public ColumnInfoDTO() {
	}

	public ColumnInfoDTO(String columnName, boolean foundHibernate) {
		this.columnName = columnName;
		this.foundHibernate = foundHibernate;
	}

	public String getSchema() {
		return schema;
	}
	public void setSchema(String schema) {
		this.schema = schema;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public BigDecimal getCharLength() {
		return charLength;
	}
	public void setCharLength(BigDecimal charLength) {
		this.charLength = charLength;
	}
	public BigDecimal getDataPrecision() {
		return dataPrecision;
	}
	public void setDataPrecision(BigDecimal dataPrecision) {
		this.dataPrecision = dataPrecision;
	}
	public BigDecimal getDataLength() {
		return dataLength;
	}
	public void setDataLength(BigDecimal dataLength) {
		this.dataLength = dataLength;
	}

	public boolean isNullable() {
		return nullable;
	}

	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}

	public boolean isFoundHibernate() {
		return foundHibernate;
	}

	public void setFoundHibernate(boolean foundHibernate) {
		this.foundHibernate = foundHibernate;
	}

	public boolean isFoundDataBase() {
		return foundDataBase;
	}

	public void setFoundDataBase(boolean foundDataBase) {
		this.foundDataBase = foundDataBase;
	}

}
