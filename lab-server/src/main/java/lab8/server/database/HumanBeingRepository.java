package lab8.server.database;


import lab8.common.models.Car;
import lab8.common.models.Coordinates;
import lab8.common.models.HumanBeing;
import lab8.common.models.Mood;
import lab8.server.utils.ServerLogger;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

public class HumanBeingRepository {

    public HumanBeingRepository() {
    }

    public long insert(HumanBeing humanBeing, long userId) {
        String sql = """
                INSERT INTO human_being(
                name,
                coordinate_x,
                coordinate_y,
                real_hero,
                has_toothpick,
                impact_speed,
                soundtrack_name,
                minutes_of_waiting,
                mood,
                car_cool,
                user_id)
                VALUES(?,?,?,?,?,?,?,?,?,?,?)
                RETURNING id;
                """;
        try (PreparedStatement stmt = DBConnector.getConnection().prepareStatement(sql)) {
            stmt.setString(1, humanBeing.getName());
            stmt.setInt(2, humanBeing.getCoordinates().getX());
            stmt.setFloat(3, humanBeing.getCoordinates().getY());
            stmt.setBoolean(4, humanBeing.getRealHero());
            stmt.setBoolean(5, humanBeing.getHasToothpick());
            stmt.setDouble(6, humanBeing.getImpactSpeed());
            stmt.setString(7, humanBeing.getSoundtrackName());
            stmt.setDouble(8, humanBeing.getMinutesOfWaiting());
            stmt.setString(9, humanBeing.getMood() != null ? humanBeing.getMood().name() : null);
            stmt.setObject(10, humanBeing.getCar().getCool());
            stmt.setLong(11, userId);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }

            return -1;
        } catch (SQLException e) {
            ServerLogger.logger.log(Level.SEVERE, "Ошибка вставки");
            return -1;
        }
    }

    public boolean update(long id, HumanBeing human, long userId) {
        String sql = """
                UPDATE human_being
                SET
                    name = ?,
                    coordinate_x = ?,
                    coordinate_y = ?,
                    real_hero = ?,
                    has_toothpick = ?,
                    impact_speed = ?,
                    soundtrack_name = ?,
                    minutes_of_waiting = ?,
                    mood = ?,
                    car_cool = ?
                WHERE id = ?
                AND user_id = ?
                """;

        try (PreparedStatement stmt = DBConnector.getConnection().prepareStatement(sql)) {
            stmt.setString(1, human.getName());
            stmt.setInt(2, human.getCoordinates().getX());
            stmt.setFloat(3, human.getCoordinates().getY());
            stmt.setBoolean(4, human.getRealHero());
            stmt.setBoolean(5, human.getHasToothpick());
            stmt.setDouble(6, human.getImpactSpeed());
            stmt.setString(7, human.getSoundtrackName());
            stmt.setDouble(8, human.getMinutesOfWaiting());
            stmt.setString(9, human.getMood() != null ? human.getMood().name() : null);
            stmt.setObject(10, human.getCar().getCool());
            stmt.setLong(11, id);
            stmt.setLong(12, userId);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            ServerLogger.logger.log(Level.SEVERE, "Ошибка обновления базы данных");
            return false;
        }
    }

    public long deleteById(long id, long userId) {
        String sql = """
                DELETE FROM human_being
                WHERE id = ?
                AND user_id = ?""";
        try (PreparedStatement stmt = DBConnector.getConnection().prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.setLong(2, userId);

            int affected = stmt.executeUpdate();
            return affected > 0 ? id : -1;

        } catch (SQLException e) {
            ServerLogger.logger.log(Level.SEVERE, "Ошибка удаления информации из бд");
            return -1;
        }
    }

    public List<HumanBeing> selectAll() {

        String sql = "SELECT * FROM human_being";

        try (PreparedStatement stmt = DBConnector.getConnection().prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            List<HumanBeing> list = new ArrayList<>();

            while (rs.next()) {
                Coordinates coordinates = new Coordinates(
                        rs.getInt("coordinate_x"),
                        rs.getFloat("coordinate_y")
                );
                String mood = rs.getString("mood");
                Car car = new Car(rs.getBoolean("car_cool"));
                HumanBeing human = new HumanBeing(
                        rs.getLong("id"),
                        rs.getString("name"),
                        coordinates,
                        rs.getTimestamp("creation_date").toLocalDateTime(),
                        rs.getBoolean("real_hero"),
                        rs.getBoolean("has_toothpick"),
                        rs.getDouble("impact_speed"),
                        rs.getString("soundtrack_name"),
                        rs.getDouble("minutes_of_waiting"),
                        mood != null ? Mood.valueOf(mood) : null,
                        car,
                        rs.getLong("user_id"));
                list.add(human);
            }

            return list;
        } catch (SQLException e) {
            ServerLogger.logger.log(Level.WARNING, "Ошибка получения коллекции");
            return Collections.emptyList();
        }
    }

    public boolean deleteAll(long userId) {
        String sql = """
                DELETE FROM human_being
                WHERE user_id = ?""";
        try (PreparedStatement stmt = DBConnector.getConnection().prepareStatement(sql)) {
            stmt.setLong(1, userId);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            ServerLogger.logger.log(Level.WARNING, "Не удалось очистить элементы пользователя с id = " + userId);
            return false;
        }
    }
}
