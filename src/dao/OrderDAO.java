package dao;

import exception.DataAccessException;
import model.Order;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    public int save(
            Connection connection,
            Order order) throws SQLException {

        String sql =
                "INSERT INTO orders " +
                        "(user_id, total_amount, status) " +
                        "VALUES (?, ?, ?)";

        try (
                PreparedStatement ps =
                        connection.prepareStatement(
                                sql,
                                Statement.RETURN_GENERATED_KEYS
                        )
        ) {

            ps.setInt(1, order.getUserId());
            ps.setDouble(2, order.getTotalAmount());
            ps.setString(3, order.getStatus());

            ps.executeUpdate();

            try (ResultSet rs =
                         ps.getGeneratedKeys()) {

                if (rs.next()) {

                    int orderId = rs.getInt(1);

                    order.setId(orderId);

                    return orderId;
                }
            }
        }

        throw new SQLException(
                "Order ID was not generated"
        );
    }

    public Order findById(
            Connection connection,
            int orderId) throws SQLException {

        String sql =
                "SELECT id, user_id, total_amount, status " +
                        "FROM orders " +
                        "WHERE id = ?";

        try (
                PreparedStatement ps =
                        connection.prepareStatement(sql)
        ) {

            ps.setInt(1, orderId);

            try (ResultSet rs =
                         ps.executeQuery()) {

                if (rs.next()) {

                    return new Order(rs.getInt("user_id"), rs.getDouble("total_amount"), rs.getString("status"));
                }
            }
        }

        return null;
    }

    public List<Order> findByUserId(
            int userId) {

        String sql =
                "SELECT id, user_id, total_amount, status " +
                        "FROM orders " +
                        "WHERE user_id = ? " +
                        "ORDER BY id DESC";

        List<Order> orders =
                new ArrayList<>();

        try (
                Connection connection =
                        util.DBConnection.getConnection();

                PreparedStatement ps =
                        connection.prepareStatement(sql)
        ) {

            ps.setInt(1, userId);

            try (ResultSet rs =
                         ps.executeQuery()) {

                while (rs.next()) {

                    orders.add(
                            new Order(

                                    rs.getInt("user_id"),
                                    rs.getDouble(
                                            "total_amount"
                                    ),
                                    rs.getString("status")
                            )
                    );
                }
            }

        } catch (SQLException e) {

            throw new DataAccessException(
                    "Failed to retrieve orders",
                    e
            );
        }

        return orders;
    }

    public boolean updateStatus(
            Connection connection,
            int orderId,
            String status) throws SQLException {

        String sql =
                "UPDATE orders " +
                        "SET status = ? " +
                        "WHERE id = ?";

        try (
                PreparedStatement ps =
                        connection.prepareStatement(sql)
        ) {

            ps.setString(1, status);
            ps.setInt(2, orderId);

            return ps.executeUpdate() > 0;
        }
    }
}