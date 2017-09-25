package com.company;

import com.company.config.Configs;
import com.typesafe.config.Config;

import java.sql.DriverManager;

public class DBITestProvider {
    public static void initDBI() {
        Config db = Configs.getConfig("persist.conf","db");
        initDBI(db.getString("url"), db.getString("user"), db.getString("password"));
    }

    public static void initDBI(String dbUrl, String dbUser, String dbPassword) {
        DBIProvider.init(() -> {
            try {
                Class.forName("org.hsqldb.jdbcDriver");
            } catch (ClassNotFoundException e) {
                throw new IllegalStateException("driver not found", e);
            }
            return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
        });
    }
}
