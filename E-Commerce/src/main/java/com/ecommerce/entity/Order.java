package com.ecommerce.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "orders")
public class Order {
    @Id
    private String oid;

    @ManyToOne
    @JoinColumn(name = "userid", referencedColumnName = "userid")
    private User user;

    @ManyToOne
    @JoinColumn(name = "pid", referencedColumnName = "pid")
    private Product product;

    private Long time;
    private Double price;
    private int quantity;
    private String address;
    private String status;

}
