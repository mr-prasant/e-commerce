package com.ecommerce.service;

import com.ecommerce.entity.SignedInUser;

public interface SignedInUserService {
    SignedInUser addSignedInUser(SignedInUser signedInUser);
    void removeSignedInUser(String userid);
    SignedInUser getSignedInUser(String userid);
}
