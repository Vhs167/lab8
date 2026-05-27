package lab8.server.auth;

import lab8.server.database.UserRepository;
import lab8.server.utils.ServerLogger;

import java.sql.SQLException;
import java.util.logging.Level;

public class AuthService {
    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean register(String userName, String password) {
        try {
            String hash = PasswordHasher.hash(password);

            long id = userRepository.createUser(userName, hash);

            return id != -1;
        } catch (SQLException e) {
            ServerLogger.logger.log(Level.SEVERE, "Ошибка регистрации пользователя!");
            return false;
        }
    }

    public Long login(String userName, String password) {
        try {
            String hash = PasswordHasher.hash(password);

            String dbHash = userRepository.getPasswordHash(userName);

            if (dbHash == null) {
                return null;
            }

            if (dbHash.equals(hash)) {
                return userRepository.getUserId(userName);
            }

            return null;
        } catch (SQLException e) {
            ServerLogger.logger.log(Level.SEVERE, "Неверный пароль или логин!");
            return null;
        }
    }
}
