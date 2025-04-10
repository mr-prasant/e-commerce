package com.ecommerce.repository;

import com.ecommerce.entity.UserDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDetailRepository extends JpaRepository<UserDetail, String> {

}
