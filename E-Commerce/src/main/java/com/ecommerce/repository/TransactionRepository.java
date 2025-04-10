package com.ecommerce.repository;

import com.ecommerce.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, String> {
    Transaction findByOrderOidAndUserUserid(String oid, String userid);
    List<Transaction> findByUserUserid(String userid);
}
