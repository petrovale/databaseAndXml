package com.company;

import com.company.config.Configs;
import com.company.export.SynchronizerByXML;
import com.typesafe.config.Config;
import com.company.export.JobDescriptionExporterToXml;

import java.nio.file.Paths;
import java.sql.DriverManager;


public class Main {

    public static void main(String[] args) throws Exception {
        initDBI();

        if (args.length > 0) {
            switch (args[0]) {
                case "export":
                    final JobDescriptionExporterToXml exporterToXml = new JobDescriptionExporterToXml();
                    exporterToXml.writeToXml(Paths.get(args[1]));
                    break;
                case "sync":
                    final SynchronizerByXML synchronizerDBbyXML = new SynchronizerByXML();
                    synchronizerDBbyXML.synchronize(args[1]);
                    break;
                default:
                    System.out.println("incorrect command");
                    break;
            }
        } else {
            System.out.println("not found command");
        }
    }

    public static void initDBI() {
        Config db = Configs.getConfig("persist.conf","db");
        initDBI(db.getString("url"), db.getString("user"), db.getString("password"));
    }

    public static void initDBI(String dbUrl, String dbUser, String dbPassword) {
        DBIProvider.init(() -> {
            try {
                Class.forName("org.hsqldb.jdbcDriver");
            } catch (ClassNotFoundException e) {
                throw new IllegalStateException("HSQLDB driver not found", e);
            }
            return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
        });
    }
}
