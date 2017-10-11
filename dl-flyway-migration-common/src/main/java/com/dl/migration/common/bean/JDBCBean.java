package com.dl.migration.common.bean;

public class JDBCBean {

    public JDBCBean(String url, String user, String password, String driver, String databaseName, String mysqlURL) {
        this.setUrl(url);
        this.setUser(user);
        this.setPassword(password);
        this.setDriver(driver);
        this.setDatabaseName(databaseName);
        this.setMysqlURL(mysqlURL);
    }

    private String url;
    private String user;
    private String password;
    private String driver;
    private String databaseName;
    private String mysqlURL;


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }


    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }


    public String getMysqlURL() {
        return mysqlURL;
    }

    public void setMysqlURL(String mysqlURL) {
        this.mysqlURL = mysqlURL;
    }
}