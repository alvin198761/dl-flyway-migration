package com.dl.migration.admin;

import com.dl.migration.common.bean.JDBCBean;
import com.dl.migration.common.database.MysqlDataBaseExecutor;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootApplication
@EnableAutoConfiguration
@Configuration
public class Application implements InitializingBean {

    @Value("${migration.source.urls}")
    private String urls;
    @Value("${migration.source.user}")
    private String user;
    @Value("${migration.source.password}")
    private String password;
    @Value("${migration.source.driver}")
    private String driver;
    @Value("${migration.source.url.format}")
    private String urlFormat;
    @Value("${migration.source.mode}")
    private String mode;

    @Autowired
    private MysqlDataBaseExecutor mysqlDataBaseExecutor;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("------------------------------------------------");
        System.out.println("began migration ...");
        List<JDBCBean> list = new ArrayList<>();
        String regex = "([^,]+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(this.urls);
        while (matcher.find()) {
            String[] urlParams = matcher.group(1).trim().split(":");
            String url = String.format(this.urlFormat, urlParams);
            String mysqlUrl = String.format(this.urlFormat, urlParams[0], urlParams[1], "database");
            list.add(new JDBCBean(url, this.user, this.password, this.driver, urlParams[2], mysqlUrl));
        }
        list.forEach(this::createTable);
    }

    private void createTable(JDBCBean jdbcBean) {
        //删除再创建
        if ("delete".equalsIgnoreCase(this.mode)) {
            System.out.println("drop database " + jdbcBean.getDatabaseName());
            mysqlDataBaseExecutor.dropDatabase(jdbcBean);
        }
        if (!mysqlDataBaseExecutor.checkDatabaseExist(jdbcBean)) {
            System.out.println("create database " + jdbcBean.getDatabaseName());
            mysqlDataBaseExecutor.createDatabase(jdbcBean);
        }
        try {
            System.out.println("do migrate " + jdbcBean.getUrl());
            //封装 data source
            DataSource dataSource = DataSourceBuilder.create(Thread.currentThread().getContextClassLoader())
                    .driverClassName(jdbcBean.getDriver())
                    .url(jdbcBean.getUrl())
                    .username(jdbcBean.getUser())
                    .password(jdbcBean.getPassword())
                    .build();
            //迁移数据
            Flyway flyway = new Flyway();
            flyway.setDataSource(dataSource);
            flyway.migrate();
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
        System.out.println("end of migration");
        System.out.println("-------------------------------------------------!");
    }

    @Bean
    public MysqlDataBaseExecutor mysqlDataBaseExecutor(){
        return new MysqlDataBaseExecutor();
    }

}
