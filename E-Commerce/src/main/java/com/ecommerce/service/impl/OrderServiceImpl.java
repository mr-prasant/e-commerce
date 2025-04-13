package com.ecommerce.service.impl;

import com.ecommerce.entity.Order;
import com.ecommerce.entity.Product;
import com.ecommerce.entity.User;
import com.ecommerce.entity.UserDemand;
import com.ecommerce.exception.InvalidInputResourceException;
import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.repository.OrderRepository;
import com.ecommerce.service.OrderService;
import com.ecommerce.service.ProductService;
import com.ecommerce.service.UserDemandService;
import com.ecommerce.service.UserService;
import com.ecommerce.utility.ServiceUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private OrderRepository orderRepository;
    private UserService userService;
    private ProductService productService;
    private UserDemandService userDemandService;
    private ServiceUtil util;

    @Override
    public Order addOrder(Order order) {

        if (order.getUser() == null || order.getProduct() == null) {
            throw new InvalidInputResourceException("Empty fields are not accepted.");
        }

        // generating order id
        order.setOid(util.generateId("O"));
        order.setTime(System.currentTimeMillis());
        order.setStatus("NOT SHIPPED");

        // check for valid user and product
        User user = userService.getUser(order.getUser().getUserid());
        Product product = productService.getProductById(order.getProduct().getPid());
        if (user == null) {
            throw new InvalidInputResourceException("Invalid user.");
        }
        else if (product == null) {
            throw new InvalidInputResourceException("Invalid product.");
        }
        else if (product.getIsAvailable() == 0) {
            throw new InvalidInputResourceException("Product with " + product.getPid() +
                    "is unavailable.");
        }

        int orderQty = order.getQuantity();
        int productQty = product.getQuantity();
        int qty = Math.min(orderQty, productQty);
        order.setQuantity(qty);

        productQty -= qty;
        orderQty -= qty;

        // check and set product availability
        if (productQty <= 0) {
            productService.setProductAvailable(product.getPid(), 0);
        }

        // add remaining order into user demand
        if (orderQty > 0) {
            UserDemand userDemand = new UserDemand();
            userDemand.setProduct(product);
            userDemand.setUser(user);
            userDemand.setQuantity(orderQty);
            userDemandService.addUserDemand(userDemand);
        }
        order.setProduct(product);
        return orderRepository.save(order);
    }

    @Override
    public Order updateOrder(String oid, Order order) {
        Order existingOrder = orderRepository.findById(oid).orElseThrow(() -> new ResourceNotFoundException("No order with: " + oid + "found."));
        if (order.getUser() == null || order.getProduct() == null) {
            throw new InvalidInputResourceException("Empty fields are not accepted.");
        }

        // check for valid user and product
        User user = userService.getUser(order.getUser().getUserid());
        Product product = productService.getProductById(order.getProduct().getPid());
        if (user == null) {
            throw new InvalidInputResourceException("Invalid user.");
        }
        else if (product == null) {
            throw new InvalidInputResourceException("Invalid product.");
        }
        else if (product.getIsAvailable() == 0) {
            throw new InvalidInputResourceException("Product with " + product.getPid() +
                    "is unavailable.");
        }

        // updating qty in product and remove order
        productService.setProductQuantity(existingOrder.getProduct().getPid(), existingOrder.getProduct().getQuantity() + existingOrder.getQuantity());
        removeOrder(oid);

        return addOrder(order);
    }

    @Override
    public void removeOrder(String oid) {
        Order order = orderRepository.findById(oid).orElseThrow(() -> new ResourceNotFoundException("No order with: " + oid + "found."));
        order.setStatus("CANCELLED");
        orderRepository.save(order);
    }

    @Override
    public List<Order> getAllAdminOrdersByPid(String pid) {
        return List.of();
    }

    @Override
    public List<Order> getAllAdminOrders(String userid) {
        return List.of();
    }

    @Override
    public List<Order> getAllUserOrders(String userid) {
        return List.of();
    }

    @Override
    public boolean isDelivered(String oid) {
        return false;
    }

    @Override
    public String getOrderStatus(String oid) {
        Order order = orderRepository.findById(oid).orElseThrow(() -> new ResourceNotFoundException("No order with: " + oid + " found."));
        return order.getStatus();
    }

    @Override
    public boolean setOrderStatus(String oid, String status) {
        Order order = orderRepository.findById(oid).orElseThrow(() -> new ResourceNotFoundException("No order with: " + oid + " found."));
        order.setStatus(status);
        orderRepository.save(order);
        return true;
    }

    @Override
    public Order getOrderById(String oid) {
        return orderRepository.findById(oid).orElseThrow(() -> new ResourceNotFoundException("No order with: " + oid + " found."));
    }
}
