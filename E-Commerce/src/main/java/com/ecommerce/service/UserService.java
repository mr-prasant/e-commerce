package com.ecommerce.service;

import com.ecommerce.entity.Product;
import com.ecommerce.entity.User;

interface UserService {
    User registerUser(User user);
    User updateUser(String  userid, User user);
    User findUserByEmail(String  userid);
    void removeUser(User user);
    boolean existUser(User user);
}
