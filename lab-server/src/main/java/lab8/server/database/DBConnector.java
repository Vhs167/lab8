package lab8.server.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;




import java.sql.Connection;

import java.sql.SQLException;

public class DBConnector {
    private static final String URL = "jdbc:postgresql://127.0.0.1:5432/studs?sslmode=disable";
    private static final String USER = "lab";
    private static final String PASS = "lab";

    private static final HikariDataSource dataSource;

    static {
        HikariConfig config = new HikariConfig();

        config.setJdbcUrl(URL);
        config.setUsername(USER);
        config.setPassword(PASS);

        config.addDataSourceProperty("ssl", "false");

        config.setMaximumPoolSize(30);
        config.setMinimumIdle(2);

        config.setIdleTimeout(30000);
        config.setConnectionTimeout(10000);

        dataSource = new HikariDataSource(config);
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
