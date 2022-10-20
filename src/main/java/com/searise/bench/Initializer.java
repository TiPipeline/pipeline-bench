package com.searise.bench;

import java.util.Properties;
import java.util.concurrent.*;
import java.sql.*;

public class Initializer {
    static String[] intRange = {"NULL", "0", "1", "-1", "1.1", "-1.1", "2147483647", "2147483646", "-2147483648", "-2147483647", "1234567890", "-1234567890", "123456.7890", "-123456.7890", "1.0000000000000002"};
    static String[] doubeRange = {"NULL", "0.0", "1.1", "-1.1", "2147483647", "2147483646", "-2147483648", "-2147483647", "2.2250738585072014e-308", "-2.2250738585072014e-308", "123456.7890", "-123456.7890", "1.0000000000000002", "4.9406564584124654e-324", "2.2250738585072009e-308", "-2.2250738585072009e-308"};
    static String[] stringRange = {"NULL", "'aaaaab'", "'basdbga'", "' qwe sdfg sd '", "'@!@%sdaf$@$'", "'%**^@!#^!@#'"};
    static String[] dateTimeRange = {"NULL", "'1000-01-01 00:00:00'", "'1000-01-02 23:59:59'", "'9999-12-3 00:00:00'", "'9999-12-2 23:59:59'", "'1234-01-01 00:00:00'"};

    public static void run(Properties props, DBHolder dbHolder) throws Exception {
        try (Connection initConn = dbHolder.createConnection();
            Statement initStmt = initConn.createStatement()) {
            initStmt.execute("drop table if exists big_one");
            initStmt.execute(
                """
                create table big_one (
                    id bigint primary key auto_random, 
                    agg_key smallint, 
                    i int, 
                    r real, 
                    s char(32) DEFAULT 'abc', 
                    dt datetime DEFAULT '1000-01-01 00:00:00')
                """);
            initStmt.execute("drop table if exists small_one");
            initStmt.execute("create table small_one (agg_key smallint)");
            if (props.getProperty("cluster_type", "mysql").equals("tiflash")) {
                initStmt.execute(String.format(
                    "ALTER TABLE big_one SET TIFLASH REPLICA %s", 
                    props.getProperty("tiflash_num", "0")));
                initStmt.execute(String.format(
                    "ALTER TABLE small_one SET TIFLASH REPLICA %s", 
                    props.getProperty("tiflash_num", "0")));
            }
        }

        int aggKeyTypes = Integer.parseInt(props.getProperty("initialize.agg_key_types", "0"));
        int rowsPerKey = Integer.parseInt(props.getProperty("initialize.rows_per_key", "0"));
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < aggKeyTypes; i++) {
                int agg_key = i;
                System.out.println("insert for key: " + agg_key);
                executor.submit(() -> {
                    try (Connection insertConn = dbHolder.createConnection();
                        Statement insertStmt = insertConn.createStatement()) {
                        insertStmt.execute(String.format("insert into small_one (agg_key) VALUES (%s)", agg_key));
                        int j = 0;
                        while (j < rowsPerKey) {
                            StringBuilder sqlBuilder = new StringBuilder("insert into big_one (agg_key, i, r, s, dt) VALUES ");
                            dataLoop:
                            for (String intValue : intRange) {
                                for (String doubeValue : doubeRange) {
                                    for (String stringValue : stringRange) {
                                        for (String dateTimeValue : dateTimeRange) {
                                            sqlBuilder.append(String.format(
                                                "(%s, %s, %s, %s, %s),", 
                                                agg_key, intValue, doubeValue, stringValue, dateTimeValue));
                                            
                                            if (++j >= rowsPerKey) {
                                                break dataLoop;
                                            }
                                        }
                                    }
                                }
                            }

                            // remove last `,`.
                            sqlBuilder.setLength(sqlBuilder.length() - 1);
                            insertStmt.execute(sqlBuilder.toString());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.exit(-1);
                    }
                });
            }
        }
        System.out.println(String.format("insert %s rows to big_one success", aggKeyTypes * rowsPerKey));
    }
}
