package com.ecommerce.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Entity
@Table(name = "user_details")
public class UserDetail {
    @Id
    @Email(message="Invalid mail format")
    private String userid;

    @NotBlank(message = "Role cannot blank")
    private String role;

    @NotBlank(message = "Name cannot blank")
    private String name;

    private String phone;
    private String address;

    @OneToOne
    @MapsId
    @JoinColumn(name = "userid")
    private User user;

}
