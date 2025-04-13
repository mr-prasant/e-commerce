package com.ecommerce.service;

import com.ecommerce.entity.User;

public interface UserService {
    User registerUser(User user);
    User updateUser(String  userid, User user);
    User removeUser(User user);
    User getUser(String  userid);
    boolean isAuthenticated(User user);
    boolean existUser(User user);
    boolean existUserid(String userid);
}
