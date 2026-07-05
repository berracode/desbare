package com.ritallus.desvare.core.infrastructure.sqlite.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.sql.DataSource;

import org.sqlite.SQLiteDataSource;

public class SqliteDataSourceFactory {

    public DataSource create() {

        try {
            Path directory = Paths.get(System.getProperty("user.home"), ".desvarapp");

            Files.createDirectories(directory);

            String dbFile = directory.resolve("desvareDB.db").toString();

            SQLiteDataSource ds = new SQLiteDataSource();

            ds.setUrl("jdbc:sqlite:" + dbFile);
            return ds;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

