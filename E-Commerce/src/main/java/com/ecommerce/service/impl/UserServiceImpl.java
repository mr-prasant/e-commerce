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

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ServiceUtil serviceUtil;

    private final PasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public UserServiceImpl(UserRepository userRepository, ServiceUtil serviceUtil) {
        this.userRepository = userRepository;
        this.serviceUtil = serviceUtil;
    }

    @Override
    public User registerUser(User user) {
        clean(user); // clean the user data
        if (user.getUserid().isEmpty() || user.getPassword().isEmpty()) {
            throw new InvalidInputResourceException("Empty fields are not accepted.");
        }

        // encryption added to the user's password
        user.setPassword(encodePassword(user.getPassword()));

        String role = user.getRoles();
        user.setRoles(role);

        // verifying user role
        serviceUtil.verifyUserRole(role);

        User dbUser = getUser(user.getUserid());

        if (dbUser == null) {
            return userRepository.save(user);
        }

        String dbRole = dbUser.getRoles();

        if (dbRole.contains(role)) {
            throw new ResourceAlreadyExistException("Already user exist with role: " + user.getRoles());
        }

        user.setRoles(role + ", " + dbRole);
        return userRepository.save(user);
    }

    @Override
    public User updateUser(String userid, User user) {
        User dbuser = getUser(clean(userid));

        if (dbuser == null) {
            throw new NullResourceException("No user with userid: " + userid + " found.");
        }

        dbuser.setPassword(encodePassword(user.getPassword()));
        return userRepository.save(dbuser);
    }

    @Override
    public void removeUser(User user) {
        clean(user); // cleaning the user data

        if (!isAuthenticated(user)) {
            throw new ResourceNotFoundException("No user with userid: " + user.getUserid() + " found.");
        }

        User dbUser = getUser(user.getUserid());

        String role = user.getRoles();
        serviceUtil.verifyUserRole(role);

        String[] dbRoles = dbUser.getRoles().split(",");
        String updatedRole = "";

        for (String dbRole: dbRoles) {
            if (!dbRole.trim().equals(role)) {
                updatedRole = dbRole.trim();
            }
        }

        if (updatedRole.equals(role)) {
            throw new NullResourceException("Trying to delete the invalid role: " + role + "; not assigned.");
        }

        // TODO deleting related data

        if (updatedRole.trim().isEmpty()) { // all roles are removed
            userRepository.delete(user); // delete the user from database

        } else { // update the new role to user
            user.setRoles(updatedRole);
            userRepository.save(user);
        }
    }


    @Override
    public User getUser(String userid) {
        return userRepository.findById(userid).orElse(null);
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
            return true;
        }

        if (dbUser == null) {
            throw new ResourceNotFoundException("User with userid: " + user.getUserid() + " not found.");
        }

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
        return encoder.matches(password, encPassword);
    }

    private String encodePassword(String password) {
        return encoder.encode(password);
    }


    private String clean(String s) {
        return s == null? "" : s.trim().toLowerCase();
    }

    private void clean(User user) {
        user.setUserid(clean(user.getUserid()));
        user.setRoles(clean(user.getRoles()));
        user.setPassword(user.getPassword().trim());
    }
}
