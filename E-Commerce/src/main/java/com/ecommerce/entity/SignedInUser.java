package com.ecommerce.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "signed_in_users")
public class SignedInUser {
    @Id
    private String userid;

    private String token;
    private String role;
    private Long time;

    @OneToOne
    @JoinColumn(name = "userid", referencedColumnName = "userid")
    private User user;
}
