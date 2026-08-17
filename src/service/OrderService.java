package service;

import dao.OrderDAO;
import dao.OrderItemDAO;
import dao.ProductDAO;
import exception.DataAccessException;
import model.Order;
import model.OrderItem;
import model.Product;
import util.DBConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class OrderService {

    private final OrderDAO orderDAO;
    private final OrderItemDAO orderItemDAO;
    private final ProductDAO productDAO;

    public OrderService() {

        this.orderDAO = new OrderDAO();
        this.orderItemDAO = new OrderItemDAO();
        this.productDAO = new ProductDAO();
    }
    // CREATE ORDER

    public int createOrder(
            int userId,
            List<OrderItem> items) {

        if (items == null ||
                items.isEmpty()) {

            throw new IllegalArgumentException(
                    "Order must contain products"
            );
        }

        Connection connection = null;

        try {
            Class.forName("org.postgresql.Driver");
            connection =
                    DBConnection.getConnection();


            connection.setAutoCommit(false);

            double totalAmount = 0;


            for (OrderItem item : items) {

                Product product =
                        productDAO.findById(
                                connection,
                                item.getProductId()
                        );

                if (product == null) {

                    throw new IllegalArgumentException(
                            "Product not found: "
                                    + item.getProductId()
                    );
                }

                if (item.getQuantity() <= 0) {

                    throw new IllegalArgumentException(
                            "Quantity must be greater than zero"
                    );
                }

                if (product.getQuantity()
                        < item.getQuantity()) {

                    throw new IllegalArgumentException(
                            "Insufficient stock for "
                                    + product.getName()
                    );
                }

                item.setPrice(
                        product.getPrice()
                );

                totalAmount +=
                        product.getPrice()
                                * item.getQuantity();
            }

            Order order =
                    new Order(userId, totalAmount, "CONFIRMED");

            int orderId =
                    orderDAO.save(
                            connection,
                            order
                    );


            for (OrderItem item : items) {

                item.setOrderId(orderId);

                orderItemDAO.save(
                        connection,
                        item
                );

                Product product =
                        productDAO.findById(
                                connection,
                                item.getProductId()
                        );

                int newQuantity =
                        product.getQuantity()
                                - item.getQuantity();

                productDAO.updateQuantity(
                        connection,
                        item.getProductId(),
                        newQuantity
                );
            }


            connection.commit();

            return orderId;

        } catch (Exception e) {

            if (connection != null) {

                try {

                    connection.rollback();

                } catch (SQLException rollbackException) {

                    e.addSuppressed(
                            rollbackException
                    );
                }
            }

            if (e instanceof DataAccessException) {

                throw (DataAccessException) e;
            }

            throw new DataAccessException(
                    "Order creation failed",
                    e
            );

        } finally {

            if (connection != null) {

                try {

                    connection.setAutoCommit(true);

                    connection.close();

                } catch (SQLException e) {

                    e.printStackTrace();
                }
            }
        }
    }


    // GET ORDER


    public Order getOrder(int orderId) {

        try (

                Connection connection =
                        DBConnection.getConnection()
        ) {
            Class.forName("org.postgresql.Driver");
            Order order =
                    orderDAO.findById(
                            connection,
                            orderId
                    );

            if (order == null) {

                return null;
            }

            List<OrderItem> items =
                    orderItemDAO.findByOrderId(
                            connection,
                            orderId
                    );

            order.setItems(items);

            return order;

        } catch (Exception e) {

            throw new DataAccessException(
                    "Failed to retrieve order",
                    e
            );
        }
    }

    // GET USER ORDERS


    public List<Order> getOrdersByUser(
            int userId) {

        return orderDAO.findByUserId(
                userId
        );
    }


    // CANCEL ORDER


    public void cancelOrder(int orderId) {

        Connection connection = null;

        try {
            Class.forName("org.postgresql.Driver");

            connection =
                    DBConnection.getConnection();

            connection.setAutoCommit(false);


            Order order =
                    orderDAO.findById(
                            connection,
                            orderId
                    );

            if (order == null) {

                throw new IllegalArgumentException(
                        "Order not found"
                );
            }

            if ("CANCELLED"
                    .equals(order.getStatus())) {

                throw new IllegalArgumentException(
                        "Order is already cancelled"
                );
            }

            List<OrderItem> items =
                    orderItemDAO.findByOrderId(
                            connection,
                            orderId
                    );

            for (OrderItem item : items) {

                Product product =
                        productDAO.findById(
                                connection,
                                item.getProductId()
                        );

                if (product == null) {

                    throw new IllegalArgumentException(
                            "Product not found"
                    );
                }

                int newQuantity =
                        product.getQuantity()
                                + item.getQuantity();

                productDAO.updateQuantity(
                        connection,
                        item.getProductId(),
                        newQuantity
                );
            }

            orderDAO.updateStatus(
                    connection,
                    orderId,
                    "CANCELLED"
            );

            connection.commit();

        } catch (Exception e) {

            if (connection != null) {

                try {

                    connection.rollback();

                } catch (SQLException rollbackException) {

                    e.addSuppressed(
                            rollbackException
                    );
                }
            }

            throw new DataAccessException(
                    "Order cancellation failed",
                    e
            );

        } finally {

            if (connection != null) {

                try {

                    connection.setAutoCommit(true);

                    connection.close();

                } catch (SQLException e) {

                    e.printStackTrace();
                }
            }
        }
    }
}