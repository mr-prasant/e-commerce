package com.ecommerce.controller;

import com.ecommerce.entity.Order;
import com.ecommerce.service.OrderService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/order")
public class OrderControllerTest {

    private OrderService orderService;

    @PostMapping("/add")
    public ResponseEntity<Order> addOrder(@RequestBody @Valid Order order) {
        return new ResponseEntity<>(
                orderService.addOrder(order),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{oid}")
    public ResponseEntity<Order> updateOrder(@PathVariable String oid, @RequestBody @Valid Order order) {
        return new ResponseEntity<>(
            orderService.updateOrder(oid, order),
            HttpStatus.OK
        );
    }

    @DeleteMapping("/{oid}")
    public ResponseEntity<Void> deleteOrder(@PathVariable String oid) {
        return new ResponseEntity<>(
                HttpStatus.OK
        );
    }
}
