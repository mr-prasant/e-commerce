package com.ecommerce.service.impl;

import com.ecommerce.entity.User;
import com.ecommerce.exception.InvalidInputResourceException;
import com.ecommerce.exception.NullResourceException;
import com.ecommerce.exception.ResourceAlreadyExistException;
import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.repository.UserRepository;
import com.ecommerce.service.UserDetailService;
import com.ecommerce.service.UserService;
import com.ecommerce.utility.ServiceUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ServiceUtil serviceUtil;

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
    public User removeUser(User user) {
        clean(user); // cleaning the user data
        System.out.println("user: " + user);

        if (!isAuthenticated(user)) {
            throw new ResourceNotFoundException("No user with userid: " + user.getUserid() + " found.");
        }

        User dbUser = getUser(user.getUserid());

        String role = clean(user.getRoles());
        serviceUtil.verifyUserRole(role);

        String[] dbRoles = dbUser.getRoles().split(",");
        String updatedRole = "DELETED";

        // soft delete of user; assigning role -> DELETED
        switch (dbRoles.length) {
            case 0 -> {
                throw new NullResourceException("Trying to delete the invalid user: " + user.getUserid() + ".");
            }

            case 1 -> {
                if (!dbRoles[0].trim().equalsIgnoreCase(role)) {
                    throw new NullResourceException("Trying to delete the invalid role: " + role + "; not assigned.");
                }
            }

            case 2 -> {
                if (dbRoles[0].trim().equalsIgnoreCase(role)) {
                    updatedRole = clean(dbRoles[1]);
                } else if (dbRoles[1].trim().equalsIgnoreCase(role)) {
                    updatedRole = clean(dbRoles[0]);
                } else {
                    throw new NullResourceException("Trying to delete the invalid role: " + role + "; not assigned.");
                }
            }
        }

        // TODO deleting related data

        user.setRoles(updatedRole);
        user.setPassword(dbUser.getPassword());

        return userRepository.save(user);
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
