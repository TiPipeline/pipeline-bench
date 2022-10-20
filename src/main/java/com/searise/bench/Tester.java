package com.searise.bench;

import java.util.*;
import java.util.concurrent.*;

import org.apache.commons.lang3.time.StopWatch;
import com.google.common.collect.*;

import java.sql.*;

public class Tester {
    // index, round, sql
    static String[] simple_agg_sqls = {
        "select %s, %s, count(1) from big_one group by agg_key;", 
        "select %s, %s, sum(i) from big_one group by agg_key;", 
        "select %s, %s, min(r) from big_one group by agg_key;", 
        "select %s, %s, count(s) from big_one group by agg_key;", 
        "select %s, %s, avg(dt) from big_one group by agg_key;",
    };
    static String[] simple_where_sqls = {
        "select %s, %s, agg_key from big_one where i = 2;", 
        "select %s, %s, agg_key from big_one where r = 1.2;", 
        "select %s, %s, agg_key from big_one where s = 'test';", 
        "select %s, %s, agg_key from big_one where dt = '1233-01-01 00:00:00';",
    };
    static String[] simple_join_sqls = {
        "select %s, %s, t1.agg_key from big_one t1, small_one t2 where t1.agg_key = t2.agg_key limit 10;",
    };
    static String[] all_sqls = ObjectArrays.concat(ObjectArrays.concat(simple_agg_sqls, simple_where_sqls, String.class), simple_join_sqls, String.class);

    static Map<String, String[]> sqlMap = new HashMap<String, String[]>();
    static {
        sqlMap.put("simple_agg", simple_agg_sqls);
        sqlMap.put("simple_where", simple_where_sqls);
        sqlMap.put("simple_join", simple_join_sqls);
        sqlMap.put("all", all_sqls);
    }

    // 展示指标
    // - 全部跑完的总耗时
    // - 每个 client 的耗时
    public static void run(Properties props, DBHolder dbHolder, String sqlMode) throws Exception {
        if (!sqlMap.containsKey(sqlMode)) {
            sqlMode = "all";
        }
        String[] sqls = sqlMap.getOrDefault(sqlMode, all_sqls);
        System.out.println("Tester run in sql_mode: " + sqlMode);

        int concurrency = Integer.parseInt(props.getProperty("test.concurrency", "0"));
        int round = Integer.parseInt(props.getProperty("test.round", "0"));
        boolean forceForMPP =
            props.getProperty("cluster_type", "mysql").equals("tiflash")
            && Integer.parseInt(props.getProperty("tiflash_num", "0")) > 0;

        long[] elapsedTimes = new long[concurrency];
        try (ExecutorService executor = Executors.newFixedThreadPool(concurrency)) {
            for (int i = 0; i < concurrency; i++) {
                int index = i;
                System.out.println("execute for index: " + index);
                elapsedTimes[index] = 0;
                executor.submit(() -> {
                    try (Connection conn = dbHolder.createConnection();
                        Statement stmt = conn.createStatement()) {
                        if (forceForMPP) {
                            stmt.execute("set tidb_allow_mpp=1");
                            stmt.execute("set tidb_enforce_mpp=1");
                            stmt.execute("set tidb_isolation_read_engines='tiflash'");
                        }
                        long cur_milliseconds = 0;
                        int j = 0;
                        testLoop:
                        while (j < round || round < 0) {
                            for (String sql : sqls) {
                                StopWatch curWatch = new StopWatch();
                                curWatch.start();
                                stmt.execute(String.format(sql, index, j));
                                curWatch.stop();
                                cur_milliseconds += curWatch.getTime();
                                if (++j >= round && round >= 0) {
                                    break testLoop;
                                }
                            }
                        }
                        elapsedTimes[index] = cur_milliseconds; 
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.exit(-1);
                    }
                });
            }
        }
        System.out.println("=======================");
        for (int i = 0; i < concurrency; i++) {
            System.out.println(String.format("time elapsed for %s is %s milliseconds", i, elapsedTimes[i]));
        }
        System.out.println(String.format("sum = %s milliseconds", Arrays.stream(elapsedTimes).sum()));
        System.out.println(String.format("max = %s milliseconds", Arrays.stream(elapsedTimes).max().orElse(0)));
        System.out.println(String.format("min = %s milliseconds", Arrays.stream(elapsedTimes).min().orElse(0)));
        double avg = Arrays.stream(elapsedTimes).average().orElse(0);
        System.out.println(String.format("avg = %s milliseconds", avg));
        double variance = 0;
        for (var elapsedTime : elapsedTimes) {
            variance += (Math.pow((elapsedTime - avg), 2));
        }
        variance /= elapsedTimes.length;
        System.out.println(String.format("variance = %s", variance));
    }
}
