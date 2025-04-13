package com.ecommerce.repository;

import com.ecommerce.entity.UserDetail;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UserDetailRepository extends JpaRepository<UserDetail, Long> {
    UserDetail findByUserUserid(String userid);
    boolean existsByUserUserid(String userid);

    @Modifying
    @Transactional
    @Query("UPDATE UserDetail ud SET ud.roles = :roles WHERE ud.id = :id")
    int updateUserDetailRoles(String roles, Long id);

}
