package com.ecommerce.service.impl;

import com.ecommerce.entity.User;
import com.ecommerce.entity.UserDetail;
import com.ecommerce.exception.InvalidInputResourceException;
import com.ecommerce.exception.NullResourceException;
import com.ecommerce.repository.UserDetailRepository;
import com.ecommerce.service.UserDetailService;
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
        verifyUserDetail(userDetail); // verifying for emptiness
        String userid = userDetail.getUser().getUserid();

        // verifying user role
        serviceUtil.verifyUserRole(userDetail.getRoles());

        // getting the existing user
        User dbUser = userService.getUser(userid);

        // checking for nullability of user
        if (dbUser == null) {
            throw new InvalidInputResourceException("UserDetail.registerUserDetail(): Userid with: " + userid + " not matched");
        }

        // checking if the new role matches to the existing roles
        if (!dbUser.getRoles().contains(userDetail.getRoles())) {
            throw new InvalidInputResourceException("UserDetail.registerUserDetail(): UserDetail with role: " + userDetail.getRoles() + " not exist.");
        }

        // getting the existing user details of the user
        UserDetail dbUserDetail = userDetailRepository.findByUserUserid(dbUser.getUserid());

        // setting the existing user in user details
        userDetail.setUser(dbUser);

        if (dbUserDetail != null) { // if already exist
            // add the new role of existing user detail of user
            int updates = userDetailRepository.updateUserDetailRoles(userDetail.getUser().getRoles(), dbUserDetail.getId());

            if (updates > 0) { // if updated successfully
                return userDetailRepository.findByUserUserid(userDetail.getUser().getUserid());
            } else { // if failed
                throw new NullResourceException("UserDetail.registerUserDetail(): UserDetail not updated");
            }

        } else { // unique user detail found of particular user
            // set the user roles to the user details
            userDetail.setRoles(dbUser.getRoles());

            // and save in user details
            return userDetailRepository.save(userDetail);
        }
    }

    @Override
    public UserDetail updateUserDetail(String userid, UserDetail userDetail) {
        clean(userDetail); // cleaning user details
        userDetail.setUser(null); // for preventing cyclic dependency
        userid = clean(userid); // cleaning the userid

        // getting the existing user details of userid
        UserDetail dbUserDetail = userDetailRepository.findByUserUserid(userid);

        System.out.println("UserDetails: " + userid);

        // if user details not found
        if (userid.isEmpty() || dbUserDetail == null) {
            throw new InvalidInputResourceException("UserDetail.updateUserDetail(): UserDetail with userid: " + userid + " not found.");
        }

        // for preventing cyclic dependency
        dbUserDetail.setUser(null);

        // if having roles data for update
        if (!userDetail.getRoles().isEmpty()) {
            serviceUtil.verifyUserRole(userDetail.getRoles());
            dbUserDetail.setRoles(userDetail.getRoles());
        }

        // if having name data for update
        if (!userDetail.getName().isEmpty()) {
            dbUserDetail.setName(userDetail.getName());
        }

        // if having phone data for update
        if (!userDetail.getPhone().isEmpty()) {
            dbUserDetail.setPhone(userDetail.getPhone());
        }

        // if having address data for update
        if (!userDetail.getAddress().isEmpty()) {
            dbUserDetail.setAddress(userDetail.getAddress());
        }

        return userDetailRepository.save(dbUserDetail);
    }

    @Override
    public void removeUserDetail(String userid) {
        userid = clean(userid);
        System.out.println("UserDetail: " + userid);

        UserDetail userDetail = userDetailRepository.findByUserUserid(userid);

        if (userDetail == null) {
            throw new InvalidInputResourceException("UserDetail.removeUserDetail(): UserDetail with userid: " + userid + " not matched");
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

        if (user == null || user.getUserid().isEmpty() || userDetail.getName().isEmpty()) {
            throw new InvalidInputResourceException("UserDetail.verifyUserDetail(): Insufficient data provided.");
        }
    }

    private String clean(String s) {
        return s == null? "" : s.trim().toLowerCase();
    }

    private void clean(UserDetail userDetail) {

        User user = userDetail.getUser();
        System.out.println("working");
        if (user == null) {
            user = new User();
            userDetail.setUser(new User());
        }

        userDetail.getUser().setUserid(clean(user.getUserid()));
        userDetail.setRoles(clean(userDetail.getRoles()));
        userDetail.setPhone(clean(userDetail.getPhone()));
        userDetail.setName(userDetail.getName() == null? "" : userDetail.getName().trim());
        userDetail.setAddress(userDetail.getAddress() == null? "" : userDetail.getAddress().trim());

        user.setUserid(clean(user.getUserid()));
        user.setRoles(clean(user.getRoles()));
        userDetail.setUser(user);
    }
}
