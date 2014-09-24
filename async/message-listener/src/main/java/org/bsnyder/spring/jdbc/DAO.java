/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bsnyder.spring.jdbc;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @author jmittler
 */
public class DAO {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.setAppInfo("setDataSource","dao");
    }

    public void setAppInfo(String module, String action) {

        final String sql = "{ call DBMS_APPLICATION_INFO.SET_MODULE(?, ?)}";
        this.jdbcTemplate.update(sql, new Object[]{module, action});

    }

    public void insert(String last, String first, String email) {
        this.setAppInfo("insert","dao");
        this.jdbcTemplate.update(
                "insert into AQ_SUBTABLE (LAST_NAME, FIRST_NAME, EMAIL) values ( ?, ?,  ?)",
                last, first, email);
    }
}
