package com.ecommerce.entity;

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
    @Email(message="Invalid mail format")
    private String userid;

    @NotBlank(message="Password is required")
    private String password;

    @NotBlank(message="Role is required")
    private String role;

    // Mapping to SignedInUsers
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private SignedInUser signedInUser;

    // Mapping to UserDetails
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private UserDetail userDetail;

    // One-to-Many with Product
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Product> products;

    // One-to-Many with Order
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Order> orders;

    // One-to-Many with Cart
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Cart> carts;

    // One-to-Many with Transaction
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Transaction> transactions;
}
