import model.Order;
import model.OrderItem;
import model.Product;
import model.User;
import service.OrderService;
import service.ProductService;
import service.UserService;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        try {

            UserService userService =
                    new UserService();

            ProductService productService =
                    new ProductService();

            OrderService orderService =
                    new OrderService();

            // 1. USER REGISTRATION

            User user =
                    new User(
                            "Krishna",
                            "krishna@gmail.com",
                            "12345"
                    );

            int userId =
                    userService.register(user);

            System.out.println(
                    "User created. ID = "
                            + userId
            );

            // 2. LOGIN

            User loggedInUser =
                    userService.login(
                            "krishna@gmail.com",
                            "12345"
                    );

            if (loggedInUser != null) {

                System.out.println(
                        "Login successful"
                );

                System.out.println(
                        loggedInUser
                );

            } else {

                System.out.println(
                        "Login failed"
                );
            }


            // 3. CREATE PRODUCTS


            Product laptop =
                    new Product(
                            "Laptop",
                            60000,
                            10
                    );

            int laptopId =
                    productService.addProduct(
                            laptop
                    );

            System.out.println(
                    "Laptop ID = "
                            + laptopId
            );


            Product mouse =
                    new Product(
                            "Mouse",
                            1000,
                            20
                    );

            int mouseId =
                    productService.addProduct(
                            mouse
                    );

            System.out.println(
                    "Mouse ID = "
                            + mouseId
            );

            // 4. GET ALL PRODUCTS


            System.out.println(
                    "\nAll Products:"
            );

            List<Product> products =
                    productService
                            .getAllProducts();

            products.forEach(
                    System.out::println
            );

            // 5. UPDATE PRODUCT


            Product updatedProduct =
                    new Product(
                            laptopId,
                            "Gaming Laptop",
                            65000,
                            10
                    );

            boolean updated =
                    productService.updateProduct(
                            updatedProduct
                    );

            System.out.println(
                    "\nProduct updated = "
                            + updated
            );

            // 6. CREATE ORDER


            OrderItem laptopItem =
                    new OrderItem(
                            laptopId,
                            2,
                            0
                    );

            OrderItem mouseItem =
                    new OrderItem(
                            mouseId,
                            3,
                            0
                    );

            List<OrderItem> items =
                    List.of(
                            laptopItem,
                            mouseItem
                    );

            int orderId =
                    orderService.createOrder(
                            userId,
                            items
                    );

            System.out.println(
                    "\nOrder created. ID = "
                            + orderId
            );

            // 7. RETRIEVE ORDER


            Order order =
                    orderService.getOrder(
                            orderId
                    );

            System.out.println(
                    "\nOrder Details:"
            );

            System.out.println(order);

            // 8. USER ORDERS


            System.out.println(
                    "\nOrders of user:"
            );

            orderService
                    .getOrdersByUser(userId)
                    .forEach(
                            System.out::println
                    );
            // 9. CANCEL ORDER

            orderService.cancelOrder(
                    orderId
            );

            System.out.println(
                    "\nOrder cancelled"
            );

            // 10. VERIFY ORDER


            Order cancelledOrder =
                    orderService.getOrder(
                            orderId
                    );

            System.out.println(
                    "\nAfter cancellation:"
            );

            System.out.println(
                    cancelledOrder
            );


        } catch (Exception e) {

            System.out.println(
                    "Application Error: "
                            + e.getMessage()
            );

            e.printStackTrace();
        }
    }
}