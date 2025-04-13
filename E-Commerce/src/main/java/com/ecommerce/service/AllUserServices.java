package com.ecommerce.service;

import com.ecommerce.entity.User;

public interface AllUserServices {
    boolean registerUser(User user);
    void removeUser(User user);
}
