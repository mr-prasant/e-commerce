package com.ecommerce.service.impl;

import com.ecommerce.entity.User;
import com.ecommerce.exception.InvalidInputResourceException;
import com.ecommerce.exception.NullResourceException;
import com.ecommerce.exception.ResourceAlreadyExistException;
import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.repository.UserRepository;
import com.ecommerce.service.UserService;
import com.ecommerce.utility.ServiceUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ServiceUtil serviceUtil;

    public UserServiceImpl(UserRepository userRepository, ServiceUtil serviceUtil) {
        this.userRepository = userRepository;
        this.serviceUtil = serviceUtil;
    }

    @Override
    public User registerUser(User user) {
        clean(user); // clean the user data
        if (user.getUserid().isEmpty() || user.getPassword().isEmpty()) {
            throw new InvalidInputResourceException("UserServiceImpl.registerUser(): Empty fields are not accepted.");
        }

        // encryption added to the user's password
        user.setPassword(encodePassword(user.getPassword()));

        String role = clean(user.getRoles());
        user.setRoles(role);

        // verifying user role
        serviceUtil.verifyUserRole(role);

        // getting the database user
        User dbUser = getUser(user.getUserid());

        // checking for existing user
        if (dbUser == null) {
            user.setUserDetail(null);
            return userRepository.save(user);
        }

        // getting existing role
        String dbRole = dbUser.getRoles();

        if (dbRole.contains(role)) {
            throw new ResourceAlreadyExistException("UserServiceImpl.registerUser(): Already user exist with role: " + user.getRoles());
        }

        if (dbRole.contains("DELETE")) { // for deleted users
            user.setRoles(role);

        } else { // merge old roles to new one
            user.setRoles(role + ", " + dbRole);
        }

        user.setUserDetail(null);
        return userRepository.save(user);
    }

    @Override
    public User updateUser(String userid, User user) {
        clean(user); // cleaning the user data
        // getting the existing user
        User dbuser = getUser(clean(userid));

        // checking for existing user
        if (dbuser == null || !dbuser.getRoles().contains(user.getRoles())) {
            throw new NullResourceException("UserServiceImpl.updateUser(): No user with userid: " + userid + " found.");
        }

        // encrypt and store the password in database
        dbuser.setPassword(encodePassword(user.getPassword()));

        dbuser.setUserDetail(null);
        return userRepository.save(dbuser);
    }

    @Override
    public User removeUser(User user) {
        clean(user); // cleaning the user data
        System.out.println("user: " + user);

        // checking for authentication of user
        if (!isAuthenticated(user)) {
            throw new ResourceNotFoundException("UserServiceImpl.removeUser(): Invalid userid/password.");
        }

        // getting the existing user
        User dbUser = getUser(user.getUserid());

        // verifying the correct role
        String role = user.getRoles();
        serviceUtil.verifyUserRole(role);

        String[] dbRoles = dbUser.getRoles().split(",");
        String updatedRole = "DELETED";

        // soft delete of user; assigning role -> DELETED
        switch (dbRoles.length) {
            case 0 -> {
                throw new NullResourceException("UserServiceImpl.removeUser(): Trying to delete the invalid user: " + user.getUserid() + ".");
            }

            case 1 -> {
                if (!dbRoles[0].trim().equalsIgnoreCase(role)) {
                    throw new NullResourceException("UserServiceImpl.removeUser()-1: Trying to delete the invalid role: " + role + "; not assigned.");
                }
            }

            case 2 -> {
                if (dbRoles[0].trim().equalsIgnoreCase(role)) {
                    updatedRole = clean(dbRoles[1]);
                } else if (dbRoles[1].trim().equalsIgnoreCase(role)) {
                    updatedRole = clean(dbRoles[0]);
                } else {
                    throw new NullResourceException("UserServiceImpl.removeUser()-2: Trying to delete the invalid role: " + role + "; not assigned.");
                }
            }
        }

        user.setRoles(updatedRole);
        user.setPassword(dbUser.getPassword());

        user.setUserDetail(null);
        return userRepository.save(user);
    }

    @Override
    public User getUser(String userid) {
        return userRepository.findByUserid(userid);
    }

    @Override
    public boolean isAuthenticated(User user) {
        clean(user); // cleaning the user data
        serviceUtil.verifyUserRole(user.getRoles());

        User dbUser = getUser(user.getUserid());

        if (
                dbUser != null
                && matchPassword(user.getPassword(), dbUser.getPassword())
                && dbUser.getRoles().contains(user.getRoles())
        ) {
            dbUser.setUserDetail(null);
            System.out.println("auth-passed");
            return true;
        }

        System.out.println("auth-failed");
        return false;
    }

    @Override
    public boolean existUser(User user) {
        User dbuser;
        if ((dbuser = userRepository.findById(user.getUserid()).orElse(null)) == null) {
            return false;
        }

        return matchPassword(user.getPassword(), dbuser.getPassword());
    }

    @Override
    public boolean existUserid(String userid) {
        return userRepository.findById(userid).isPresent();
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    private boolean matchPassword(String password, String encPassword) {
        return serviceUtil.getEncoder().matches(password, encPassword);
    }

    private String encodePassword(String password) {
        return serviceUtil.getEncoder().encode(password);
    }

    private String clean(String s) {
        return s == null? "" : s.trim().toLowerCase();
    }

    private void clean(User user) {
        if (user == null) {
            user = new User();
        }

        user.setUserid(clean(user.getUserid()));
        user.setRoles(clean(user.getRoles()));
        user.setPassword(user.getPassword().trim());
    }
}
