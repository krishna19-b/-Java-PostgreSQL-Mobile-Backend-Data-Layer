package service;

import dao.ProductDAO;
import model.Product;

import java.util.List;

public class ProductService {

    private final ProductDAO productDAO;

    public ProductService() {

        this.productDAO =
                new ProductDAO();
    }

    public int addProduct(Product product) {

        if (product.getPrice() < 0) {

            throw new IllegalArgumentException(
                    "Price cannot be negative"
            );
        }

        if (product.getQuantity() < 0) {

            throw new IllegalArgumentException(
                    "Quantity cannot be negative"
            );
        }

        return productDAO.save(product);
    }

    public Product getProduct(int id) {

        return productDAO.findById(id);
    }

    public List<Product> getAllProducts() {

        return productDAO.findAll();
    }

    public boolean updateProduct(
            Product product) {

        return productDAO.update(product);
    }

    public boolean deleteProduct(int id) {

        return productDAO.delete(id);
    }
}