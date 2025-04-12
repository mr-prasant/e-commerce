package com.ecommerce.service;

import com.ecommerce.entity.UserDetail;

public interface UserDetailService {
    UserDetail registerUserDetail(UserDetail userDetail);
    UserDetail updateUserDetail(String userid, UserDetail userDetail);
    void removeUserDetail(String userid);
    UserDetail getUserDetailByUserid(String userid);
}
