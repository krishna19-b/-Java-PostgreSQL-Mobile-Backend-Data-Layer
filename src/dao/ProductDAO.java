package dao;

import exception.DataAccessException;
import model.Product;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    // CREATE
    public int save(Product product) {

        String sql =
                "INSERT INTO products(name, price, quantity) " +
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

            ps.setString(1, product.getName());
            ps.setDouble(2, product.getPrice());
            ps.setInt(3, product.getQuantity());

            ps.executeUpdate();

            try (ResultSet rs =
                         ps.getGeneratedKeys()) {

                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

        } catch (SQLException e) {

            throw new DataAccessException(
                    "Failed to create product",
                    e
            );
        }

        return 0;
    }

    // READ ONE
    public Product findById(int id) {

        String sql =
                "SELECT id, name, price, quantity " +
                        "FROM products " +
                        "WHERE id = ?";

        try (
                Connection connection =
                        DBConnection.getConnection();

                PreparedStatement ps =
                        connection.prepareStatement(sql)
        ) {

            ps.setInt(1, id);

            try (ResultSet rs =
                         ps.executeQuery()) {

                if (rs.next()) {
                    return mapProduct(rs);
                }
            }

        } catch (SQLException e) {

            throw new DataAccessException(
                    "Failed to find product",
                    e
            );
        }

        return null;
    }

    // READ ALL
    public List<Product> findAll() {

        String sql =
                "SELECT id, name, price, quantity " +
                        "FROM products " +
                        "ORDER BY id";

        List<Product> products =
                new ArrayList<>();

        try (
                Connection connection =
                        DBConnection.getConnection();

                PreparedStatement ps =
                        connection.prepareStatement(sql);

                ResultSet rs =
                        ps.executeQuery()
        ) {

            while (rs.next()) {

                products.add(
                        mapProduct(rs)
                );
            }

        } catch (SQLException e) {

            throw new DataAccessException(
                    "Failed to retrieve products",
                    e
            );
        }

        return products;
    }

    // UPDATE
    public boolean update(Product product) {

        String sql =
                "UPDATE products " +
                        "SET name = ?, price = ?, quantity = ? " +
                        "WHERE id = ?";

        try (
                Connection connection =
                        DBConnection.getConnection();

                PreparedStatement ps =
                        connection.prepareStatement(sql)
        ) {

            ps.setString(1, product.getName());
            ps.setDouble(2, product.getPrice());
            ps.setInt(3, product.getQuantity());
            ps.setInt(4, product.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            throw new DataAccessException(
                    "Failed to update product",
                    e
            );
        }
    }

    // DELETE
    public boolean delete(int id) {

        String sql =
                "DELETE FROM products " +
                        "WHERE id = ?";

        try (
                Connection connection =
                        DBConnection.getConnection();

                PreparedStatement ps =
                        connection.prepareStatement(sql)
        ) {

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            throw new DataAccessException(
                    "Failed to delete product",
                    e
            );
        }
    }

   //FindById

    public Product findById(
            Connection connection,
            int id) throws SQLException {

        String sql =
                "SELECT id, name, price, quantity " +
                        "FROM products " +
                        "WHERE id = ? " +
                        "FOR UPDATE";

        try (
                PreparedStatement ps =
                        connection.prepareStatement(sql)
        ) {

            ps.setInt(1, id);

            try (ResultSet rs =
                         ps.executeQuery()) {

                if (rs.next()) {

                    return mapProduct(rs);
                }
            }
        }

        return null;
    }

    // Update By Quantity

    public void updateQuantity(
            Connection connection,
            int productId,
            int quantity) throws SQLException {

        String sql =
                "UPDATE products " +
                        "SET quantity = ? " +
                        "WHERE id = ?";

        try (
                PreparedStatement ps =
                        connection.prepareStatement(sql)
        ) {

            ps.setInt(1, quantity);
            ps.setInt(2, productId);

            ps.executeUpdate();
        }
    }

    private Product mapProduct(ResultSet rs)
            throws SQLException {

        return new Product(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getDouble("price"),
                rs.getInt("quantity")
        );
    }
}