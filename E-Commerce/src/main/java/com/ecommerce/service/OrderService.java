package com.ecommerce.service;

import com.ecommerce.entity.Order;

import java.util.List;

public interface OrderService {
    Order addOrder(Order order);
    Order updateOrder(String oid, Order order);
    void removeOrder(String oid);
    List<Order> getAllAdminOrdersByPid(String pid);
    List<Order> getAllAdminOrders(String userid);
    List<Order> getAllUserOrders(String userid);
    boolean isDelivered(String oid);
    String getOrderStatus(String oid);
    boolean setOrderStatus(String oid, String status);
    Order getOrderById(String oid);
}
