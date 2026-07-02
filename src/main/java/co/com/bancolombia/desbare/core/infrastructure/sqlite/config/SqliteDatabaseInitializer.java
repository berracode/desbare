package co.com.bancolombia.desbare.core.infrastructure.sqlite.config;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SqliteDatabaseInitializer {

    private final DataSource dataSource;

    public void initialize() {

        String sql = """
                CREATE TABLE gpg_keys (
                     id INTEGER PRIMARY KEY AUTOINCREMENT,
                     name TEXT NOT NULL,
                     email TEXT NOT NULL,
                     fingerprint TEXT NOT NULL UNIQUE,
                     public_key TEXT NOT NULL,
                     private_key TEXT NOT NULL,
                     created_at TEXT NOT NULL
                 );
                """;

        try (
                Connection con =
                        dataSource.getConnection();
                Statement stmt =
                        con.createStatement()
        ) {

            stmt.execute(sql);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
