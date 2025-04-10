package com.ecommerce.service;

import com.ecommerce.entity.UserDemand;
import com.ecommerce.entity.UserDetail;

import java.util.List;

public interface UserDemandService {
    UserDemand addUserDemand(UserDemand userDemand);
    UserDetail updateUserDemand(String pid, int quantity);
    List<UserDemand> getAllUserDemand(String pid);
}
