package com.ecommerce.service;

import com.ecommerce.entity.SignedInUser;
import com.ecommerce.entity.User;

public interface SignedInUserService {
    SignedInUser addSignedInUser(User user);
    void removeSignedInUser(String userid);
    SignedInUser getSignedInUser(String userid);
    boolean isExpired(String userid);
}
