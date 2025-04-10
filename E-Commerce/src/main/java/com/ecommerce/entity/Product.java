package com.ecommerce.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "products")
public class Product {
    @Id
    private String pid;

    private String name;
    private Double price;
    private String detail;
    private Double discount;
    private String category;
    private int quantity;
    private int isAvailable;

    @Lob
    private byte[] image;

    @ManyToOne
    @JoinColumn(name = "userid", referencedColumnName = "userid")
    private User user;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Transaction> transactions;
}
