package com.ecommerce.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    private String tid;

    @OneToOne
    @JoinColumn(name = "oid", referencedColumnName = "oid")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "pid", referencedColumnName = "pid")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "userid", referencedColumnName = "userid")
    private User user;
}
