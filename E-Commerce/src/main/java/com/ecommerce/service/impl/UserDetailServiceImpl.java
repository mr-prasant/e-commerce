package com.ecommerce.service.impl;

import com.ecommerce.entity.User;
import com.ecommerce.entity.UserDetail;
import com.ecommerce.exception.InvalidInputResourceException;
import com.ecommerce.exception.ResourceAlreadyExistException;
import com.ecommerce.repository.UserDetailRepository;
import com.ecommerce.service.UserDetailService;
import com.ecommerce.utility.ServiceUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
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
        String userid = clean(userDetail.getUser().getUserid());

        serviceUtil.verifyUserRole(userDetail.getRoles());

        User dbUser = userService.getUser(userid);
        if (dbUser == null) {
            throw new InvalidInputResourceException("Userid with: " + userid + " not matched");
        }

        if (!dbUser.getRoles().contains(userDetail.getRoles())) {
            throw new InvalidInputResourceException("UserDetail with role: " + userDetail.getRoles() + " not exist.");
        }

        UserDetail dbUserDetail = userDetailRepository.findByUserUserid(dbUser.getUserid());

        if (dbUserDetail != null) {
            throw new ResourceAlreadyExistException("Userid with: " + userid + " already exist");
        }

        userDetail.setUser(dbUser);
        userDetail.setRoles(dbUser.getRoles());

        return userDetailRepository.save(userDetail);
    }

    @Override
    public UserDetail updateUserDetail(String userid, UserDetail userDetail) {
        userid = clean(userid);
        userDetail.setRoles(clean(userDetail.getRoles()));

        UserDetail dbUserDetail = userDetailRepository.findByUserUserid(userid);

        if (userid.isEmpty() || dbUserDetail == null) {
            throw new InvalidInputResourceException("UserDetail with userid: " + userid + " not found.");
        }

        if (!userDetail.getRoles().isEmpty()) {
            serviceUtil.verifyUserRole(userDetail.getRoles());
            dbUserDetail.setRoles(userDetail.getRoles());
        }

        // cleaning other details
        clean(userDetail);

        if (!userDetail.getName().isEmpty()) {
            dbUserDetail.setName(userDetail.getName());
        }

        if (!userDetail.getPhone().isEmpty()) {
            dbUserDetail.setPhone(userDetail.getPhone());
        }

        if (!userDetail.getAddress().isEmpty()) {
            dbUserDetail.setAddress(userDetail.getAddress());
        }

        return userDetailRepository.save(dbUserDetail);
    }

    @Override
    public void removeUserDetail(String userid) {
        userid = clean(userid);

        UserDetail userDetail = userDetailRepository.findByUserUserid(userid);

        if (userDetail == null) {
            throw new InvalidInputResourceException("UserDetail with userid: " + userid + " not matched");
        }

        userDetailRepository.delete(userDetail);
    }

    @Override
    public UserDetail getUserDetailByUserid(String userid) {
        userid = clean(userid);

        return userDetailRepository.findByUserUserid(userid);
    }

    private void verifyUserDetail(UserDetail userDetail) {
        clean(userDetail); // clean the user detail data
        serviceUtil.verifyUserRole(userDetail.getRoles());

        User user = userDetail.getUser();

        if (user.getUserid().isEmpty() || userDetail.getName().isEmpty()) {
            throw new InvalidInputResourceException("Insufficient data provided.");
        }
    }

    private String clean(String s) {
        return s == null? "" : s.trim().toLowerCase();
    }

    private void clean(UserDetail userDetail) {
        System.out.println(userDetail);

        User user = userDetail.getUser();
        if (user == null) {
            user = new User();
        }

        userDetail.getUser().setUserid(clean(user.getUserid()));
        userDetail.setRoles(clean(userDetail.getRoles()));
        userDetail.setPhone(clean(userDetail.getPhone()));
        userDetail.setName(userDetail.getName().trim());
        userDetail.setAddress(userDetail.getAddress().trim());

        user.setUserid(clean(user.getUserid()));
        user.setRoles(clean(user.getRoles()));
        userDetail.setUser(user);
    }
}
