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

package org.opensingular.app.commons.spring.persistence.database;

import com.zaxxer.hikari.HikariDataSource;
import org.junit.Assert;
import org.junit.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class H2DatasourceTest {

    @Test
    public void testInitScript() throws SQLException {
        DefaultH2DataSource defaultH2DataSource = new DefaultH2DataSource();
        defaultH2DataSource.addToInit("select * from dual");
        defaultH2DataSource.addToInit("select 1 from dual;");
        defaultH2DataSource.addToInit("select 2 from dual");
        defaultH2DataSource.init();

        defaultH2DataSource.getConnection();
    }

    private void createTableAndInsertTwoRows(Connection c) throws SQLException {
        c.prepareStatement("create table dbsingular.whatever(nada int not null)").executeUpdate();
        c.prepareStatement("insert into  dbsingular.whatever values(1)").executeUpdate();
        c.prepareStatement("insert into  dbsingular.whatever values(2)").executeUpdate();
        c.commit();
        c.close();
    }

    private HikariDataSource createNewH2DataSource(){
        DefaultH2DataSource defaultH2DataSource = new DefaultH2DataSource("jdbc:h2:file:./test;TRACE_LEVEL_SYSTEM_OUT=3;");
        defaultH2DataSource.setCreateDrop(true);
        defaultH2DataSource.init();
        return ((HikariDataSource)defaultH2DataSource.getTargetDataSource());
    }

    private void lookForInitOnceAndTwoRowsInWhatever(Connection c) throws SQLException {
        ResultSet rs = c.prepareStatement("select count(*) from dbsingular.whatever ").executeQuery();
        rs.next();
        Assert.assertEquals(2, rs.getInt(1));
        ResultSet rsOnce    = c.createStatement().executeQuery(" SELECT COUNT(*) FROM INITONCE");
        rsOnce.next();
        Assert.assertEquals(1, rsOnce.getInt(1));
        c.close();
    }

    @Test
    public void testDoNotRecreateDatabase() throws SQLException {
        HikariDataSource someDataSource = createNewH2DataSource();
        createTableAndInsertTwoRows(someDataSource.getConnection());

        lookForInitOnceAndTwoRowsInWhatever(someDataSource.getConnection());

        HikariDataSource otherDataSource = createNewH2DataSource();
        lookForInitOnceAndTwoRowsInWhatever(otherDataSource.getConnection());

        someDataSource.close();
        otherDataSource.close();

        while(!someDataSource.isClosed() || !otherDataSource.isClosed()){
            Thread.yield();
        }
    }


    @Test
    public void testRecreateDatabase() throws SQLException {
        HikariDataSource someDataSource = createNewH2DataSource();
        createTableAndInsertTwoRows(someDataSource.getConnection());

        lookForInitOnceAndTwoRowsInWhatever(someDataSource.getConnection());
        someDataSource.close();

        HikariDataSource otherDataSource = createNewH2DataSource();
        createTableAndInsertTwoRows(otherDataSource.getConnection());

        lookForInitOnceAndTwoRowsInWhatever(otherDataSource.getConnection());
        someDataSource.close();

    }
}
