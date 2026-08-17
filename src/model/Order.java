package model;

import java.util.ArrayList;
import java.util.List;

public class Order {

    private int id;
    private int userId;
    private double totalAmount;
    private String status;

    private List<OrderItem> items =
            new ArrayList<>();

    public Order() {
    }

    public Order(int userId,
                 double totalAmount,
                 String status) {
        this.id=id;
        this.userId = userId;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    public Order(int userId,
                 List<OrderItem> items) {

        this.userId = userId;
        this.items = items;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    @Override
    public String toString() {

        return "Order{" +
                "id=" + id +
                ", userId=" + userId +
                ", totalAmount=" + totalAmount +
                ", status='" + status + '\'' +
                ", items=" + items +
                '}';
    }
}