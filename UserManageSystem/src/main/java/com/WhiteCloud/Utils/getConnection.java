package com.WhiteCloud.Utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public class getConnection {
    public static Connection myConnection1() {
        String url = "jdbc:mysql://localhost:3306/usermanagesystem?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai";
        String username = "root";
        String password = "123456";
        Connection connection;
        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }
//    public static Connection myConnection2(){
//        HikariDataSource dataSource;
//
//        HikariConfig config = new HikariConfig();
//        config.setJdbcUrl("jdbc:mysql://localhost:3306/usermanagesystem?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai");
//        config.setUsername("root");
//        config.setPassword("123456");
//        //自行加设置
//        dataSource = new HikariDataSource(config);
//
//        try {
//            return dataSource.getConnection();
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }
}
