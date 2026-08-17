package dao;

import model.OrderItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderItemDAO {

    public int save(
            Connection connection,
            OrderItem item) throws SQLException {

        String sql =
                "INSERT INTO order_items " +
                        "(order_id, product_id, quantity, price) " +
                        "VALUES (?, ?, ?, ?)";

        try (
                PreparedStatement ps =
                        connection.prepareStatement(
                                sql,
                                Statement.RETURN_GENERATED_KEYS
                        )
        ) {

            ps.setInt(1, item.getOrderId());
            ps.setInt(2, item.getProductId());
            ps.setInt(3, item.getQuantity());
            ps.setDouble(4, item.getPrice());

            ps.executeUpdate();

            try (ResultSet rs =
                         ps.getGeneratedKeys()) {

                if (rs.next()) {

                    int id = rs.getInt(1);

                    item.setId(id);

                    return id;
                }
            }
        }

        throw new SQLException(
                "Order item ID was not generated"
        );
    }

    public List<OrderItem> findByOrderId(
            Connection connection,
            int orderId) throws SQLException {

        String sql =
                "SELECT id, order_id, product_id, " +
                        "quantity, price " +
                        "FROM order_items " +
                        "WHERE order_id = ?";

        List<OrderItem> items =
                new ArrayList<>();

        try (
                PreparedStatement ps =
                        connection.prepareStatement(sql)
        ) {

            ps.setInt(1, orderId);

            try (ResultSet rs =
                         ps.executeQuery()) {

                while (rs.next()) {

                    items.add(
                            new OrderItem(
                                    rs.getInt("id"),
                                    rs.getInt("order_id"),
                                    rs.getInt("product_id"),
                                    rs.getInt("quantity"),
                                    rs.getDouble("price")
                            )
                    );
                }
            }
        }

        return items;
    }
}