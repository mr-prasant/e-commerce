package com.ecommerce.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @Email(message = "Invalid email format")
    @Column(nullable = false, unique = true)
    private String userid;

    @NotBlank(message = "Password is required")
    @Column(nullable = false)
    private String password;

    @NotBlank(message = "Role is required")
    @Column(nullable = false)
    private String roles;

    // Mapping to SignedInUser
    @JsonIgnore
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private SignedInUser signedInUser;

    // Mapping to UserDetail
//    @JsonIgnore
//    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private UserDetail userDetail;

    // One-to-Many with Product
    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Product> products;

    // One-to-Many with Order
    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Order> orders;

    // One-to-Many with Cart
    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Cart> carts;

    // One-to-Many with Transaction
    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Transaction> transactions;
}
