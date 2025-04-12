package com.ecommerce.service.impl;

import com.ecommerce.entity.User;
import com.ecommerce.entity.UserDetail;
import com.ecommerce.exception.InvalidInputResourceException;
import com.ecommerce.repository.UserDetailRepository;
import com.ecommerce.service.UserDetailService;
import com.ecommerce.service.UserService;
import com.ecommerce.utility.ServiceUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserDetailServiceImpl implements UserDetailService {

    private UserServiceImpl userService;
    private UserDetailRepository userDetailRepository;
    private ServiceUtil serviceUtil;

    @Override
    public UserDetail registerUserDetail(UserDetail userDetail) {
        verifyUserDetail(userDetail);

        if (!userService.existUserid(userDetail.getUserid())) {
            throw new InvalidInputResourceException("Userid not matched");
        }

        return userDetailRepository.save(userDetail);
    }

    @Override
    public UserDetail updateUserDetail(String userid, UserDetail userDetail) {
        userid = clean(userid);
        verifyUserDetail(userDetail);

        if (userid.isEmpty() || !userDetailRepository.existsById(userid)) {
            throw new InvalidInputResourceException("Userid not matched");
        }

        return userDetailRepository.save(userDetail);
    }

    @Override
    public void removeUserDetail(String userid) {
        UserDetail userDetail = getUserDetailById(userid);

        if (userDetail == null) {
            throw new InvalidInputResourceException("Userid not matched");
        }

        userDetailRepository.delete(userDetail);
    }

    @Override
    public UserDetail getUserDetailById(String userid) {
        userid = clean(userid);

        return userDetailRepository.findById(userid).orElse(null);
    }

    private void verifyUserDetail(UserDetail userDetail) {
        clean(userDetail); // clean the user detail data
        serviceUtil.verifyUserRole(userDetail.getRole());

        if (userDetail.getUserid().isEmpty() || userDetail.getName().isEmpty()) {
            throw new InvalidInputResourceException("Insufficient data provided.");
        }
    }

    private String clean(String s) {
        return s == null? "" : s.trim().toLowerCase();
    }

    private void clean(UserDetail userDetail) {
        userDetail.setUserid(clean(userDetail.getUserid()));
        userDetail.setRole(clean(userDetail.getRole()));
        userDetail.setName(clean(userDetail.getName()));
        userDetail.setPhone(clean(userDetail.getPhone()));
        userDetail.setAddress(clean(userDetail.getAddress()));
    }
}
