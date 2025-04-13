package com.ecommerce.service.impl;

import com.ecommerce.entity.User;
import com.ecommerce.entity.UserDetail;
import com.ecommerce.exception.NullResourceException;
import com.ecommerce.service.AllUserServices;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@AllArgsConstructor
public class AllUserServicesImpl implements AllUserServices {
    private UserServiceImpl userService;
    private UserDetailServiceImpl userDetailService;

    @Override
    public boolean registerUser(User user) {
        UserDetail userDetail;

        System.out.println(user);

        if (user == null || (userDetail = user.getUserDetail()) == null) {
            throw new NullResourceException("Invalid data given.");
        }

        userDetail.setUser(userService.registerUser(user));
        userDetailService.registerUserDetail(userDetail);

        return true;
    }

    @Override
    public void removeUser(User user) {
        String userid = user.getUserid();
        user = userService.removeUser(user);

        System.out.println("delete: " + user);

        if (user.getRoles().equals("DELETED")) {
            userDetailService.removeUserDetail(userid);
        } else {
            UserDetail userDetail = new UserDetail();
            userDetail.setRoles(user.getRoles());
            userDetailService.updateUserDetail(userid, userDetail);
        }
    }
}
