package com.ecommerce.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "signed_in_users")
public class SignedInUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;
    private String role;
    private Long time;

    @OneToOne
    @JoinColumn(name = "userid", referencedColumnName = "userid")
    @JsonIgnore
    private User user;

    public SignedInUser(String userid, String role) {
        user = new User();

        this.user.setUserid(userid);
        this.role = role;
    }
}
