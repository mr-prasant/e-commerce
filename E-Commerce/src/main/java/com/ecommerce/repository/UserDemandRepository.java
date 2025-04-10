package com.ecommerce.repository;

import com.ecommerce.entity.UserDemand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserDemandRepository extends JpaRepository<UserDemand, Long> {
    List<UserDemand> findByProductPidOrderById(String pid);
}
