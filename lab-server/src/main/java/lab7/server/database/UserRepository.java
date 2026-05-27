package lab7.server.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepository {
    public UserRepository() {
    }

    public long createUser(String userName, String passwordHash) throws SQLException {
        String sql = """
                INSERT INTO users (username, password_hash)
                VALUES(?,?)
                RETURNING id
                """;
        try (PreparedStatement stmt = DBConnector.getConnection().prepareStatement(sql)) {

            stmt.setString(1, userName);
            stmt.setString(2, passwordHash);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }
            return -1;
        }
    }

    public String getPasswordHash(String userName) throws SQLException {
        String sql = """
                SELECT password_hash FROM users
                WHERE username = ?
                """;
        try (PreparedStatement stmt = DBConnector.getConnection().prepareStatement(sql)) {
            stmt.setString(1, userName);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("password_hash");
            }
            return null;
        }
    }

    public Long getUserId(String userName) throws SQLException {
        String sql = """
                SELECT id FROM users
                WHERE username = ?""";

        try (PreparedStatement stmt = DBConnector.getConnection().prepareStatement(sql)) {
            stmt.setString(1, userName);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                long id = rs.getLong(1);
                return id;
            }
            return null;
        }
    }
}
