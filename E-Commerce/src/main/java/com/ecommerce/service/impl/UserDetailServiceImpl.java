package com.ecommerce.service.impl;

import com.ecommerce.entity.UserDetail;
import com.ecommerce.service.UserDetailService;
import com.ecommerce.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserDetailServiceImpl implements UserDetailService {

    private UserServiceImpl userService;

    @Override
    public UserDetail registerUserDetail(UserDetail userDetail) {
        return null;
    }

    @Override
    public UserDetail updateUserDetail(String userid, UserDetail userDetail) {
        return null;
    }

    @Override
    public void removeUserDetail(String userid) {

    }

    @Override
    public UserDetail getUserDetailById(String userid) {
        return null;
    }
}
