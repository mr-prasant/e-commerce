package com.ecommerce.repository;

import com.ecommerce.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, String> {

    @Query("SELECT DISTINCT p.category FROM Product p")
    List<String> findAllCategories();

    List<Product> findAllByCategory(String category);

    List<Product> findByNameContainingIgnoreCase(String search);

    @Query("SELECT p.quantity FROM Product p WHERE p.pid = ?1")
    int getQuantityByPid(String pid);

    byte[] findImageByPid(String pid);

    int findIsAvailableByPid(String pid);
}

