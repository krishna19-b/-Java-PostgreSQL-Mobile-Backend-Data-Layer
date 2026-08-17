package dao;

import exception.DataAccessException;
import model.User;
import util.DBConnection;

import java.sql.*;

public class UserDao {

    public int save(User user) {

        String sql =
                "INSERT INTO users(name, email, password) " +
                        "VALUES (?, ?, ?)";

        try (

                Connection connection =
                        DBConnection.getConnection();

                PreparedStatement ps =
                        connection.prepareStatement(
                                sql,
                                Statement.RETURN_GENERATED_KEYS
                        )
        ) {
            Class.forName("org.postgresql.Driver");
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());

            ps.executeUpdate();

            try (ResultSet rs =
                         ps.getGeneratedKeys()) {

                if (rs.next()) {

                    return rs.getInt(1);
                }
            }

        } catch (Exception e) {

            throw new DataAccessException(
                    "Failed to register user",
                    e
            );
        }

        return 0;
    }

    public User findByEmail(String email) {

        String sql =
                "SELECT id, name, email, password " +
                        "FROM users " +
                        "WHERE email = ?";

        try (
                Connection connection =
                        DBConnection.getConnection();

                PreparedStatement ps =
                        connection.prepareStatement(sql)
        ) {

            ps.setString(1, email);

            try (ResultSet rs =
                         ps.executeQuery()) {

                if (rs.next()) {

                    return mapUser(rs);
                }
            }

        } catch (SQLException e) {

            throw new DataAccessException(
                    "Failed to find user",
                    e
            );
        }

        return null;
    }

    private User mapUser(ResultSet rs)
            throws SQLException {

        User user = new User();

        user.setId(rs.getInt("id"));
        user.setName(rs.getString("name"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));

        return user;
    }
}