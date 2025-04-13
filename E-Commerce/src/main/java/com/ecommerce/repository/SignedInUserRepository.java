package com.ecommerce.repository;

import com.ecommerce.entity.SignedInUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SignedInUserRepository extends JpaRepository<SignedInUser, Long> {
    SignedInUser findByUserUserid(String userid);
}
