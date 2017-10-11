/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dl.migration.common.database;

import com.dl.migration.common.bean.JDBCBean;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Administrator
 */
public class ConnectUtil {

    public static Connection getConnect(JDBCBean bean) throws Exception {
        Class.forName(bean.getDriver());
        return DriverManager.getConnection(bean.getMysqlURL(), bean.getUser(), bean.getPassword());
    }

    public static void close(Connection conn, Statement st, ResultSet res) {
        if (res != null) {
            try {
                res.close();
            } catch (SQLException ex) {
                Logger.getLogger(ConnectUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (st != null) {
            try {
                st.close();
            } catch (SQLException ex) {
                Logger.getLogger(ConnectUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(ConnectUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
