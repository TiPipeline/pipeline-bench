package com.searise.bench;

import java.util.Properties;
import java.sql.*;

public class DBHolder {
    Properties props;

    DBHolder(Properties props) throws Exception {
        this.props = props;
        Class.forName(props.getProperty("driverClassName", "com.mysql.cj.jdbc.Driver"));
    }

    public Connection createConnection() throws Exception {
        String jdbcUrl = props.getProperty("jdbcUrl", "");
        String user = props.getProperty("username", "");
        String password = props.getProperty("password", "");
        return DriverManager.getConnection(jdbcUrl, user, password);
    }
}
