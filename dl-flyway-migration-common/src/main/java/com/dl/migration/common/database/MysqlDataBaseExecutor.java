/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dl.migration.common.database;

import com.dl.migration.common.bean.JDBCBean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MysqlDataBaseExecutor {

    public boolean checkDatabaseExist(JDBCBean bean) {
        try {
            String sql = "select count(*) from information_schema.schemata where schema_name=?";
            Connection conn = ConnectUtil.getConnect(bean);
            if (conn == null) {
                return false;
            }
            PreparedStatement ps = null;
            ResultSet res = null;
            try {
                //
                ps = conn.prepareStatement(sql);
                ps.setString(1, bean.getDatabaseName());
                res = ps.executeQuery();
                if (res.next()) {
                    return res.getInt(1) > 0;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                ConnectUtil.close(conn, ps, res);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public boolean dropDatabase(JDBCBean bean) {
        try {
            String sql = "drop database if exists " + bean.getDatabaseName();
            Connection conn = ConnectUtil.getConnect(bean);
            if (conn == null) {
                return false;
            }
            PreparedStatement ps = null;
            try {
                //
                ps = conn.prepareStatement(sql);
                int res = ps.executeUpdate();
                return res >= 0;
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                ConnectUtil.close(conn, ps, null);
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public boolean createDatabase(JDBCBean bean) {
        try {
            String sql = "create database " + bean.getDatabaseName() + " DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci";
            Connection conn = ConnectUtil.getConnect(bean);
            if (conn == null) {
                return false;
            }
            PreparedStatement ps = null;
            try {
                //
                ps = conn.prepareStatement(sql);
                int res = ps.executeUpdate();
                return res != 0;
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                ConnectUtil.close(conn, ps, null);
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

}
