package com.ecommerce.service.impl;

import com.ecommerce.entity.SignedInUser;
import com.ecommerce.entity.User;
import com.ecommerce.exception.NullResourceException;
import com.ecommerce.repository.SignedInUserRepository;
import com.ecommerce.service.SignedInUserService;
import com.ecommerce.utility.ServiceUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SignedInUserServiceImpl implements SignedInUserService {

    private final SignedInUserRepository signedInUserRepository;
    private final ServiceUtil serviceUtil;

    @Override
    public SignedInUser addSignedInUser(User user) {
        String role;

        // Validate the input user and ensure the user has a userid and role
        if (
                (user == null) ||
                (user.getUserid() == null) ||
                ((role = user.getRoles()) == null)
        ){
            // Throw an exception if any required data is missing
            throw new NullResourceException("SignedInUserServiceImpl.addSignedInUser(): Sufficient data are not provided.");
        }

        // Verify the user's role through the service utility
        serviceUtil.verifyUserRole(role);

        // Set the user's information and other session-related data
        SignedInUser signedInUser = new SignedInUser();

        signedInUser.setUser(user);
        signedInUser.setRole(role);
        signedInUser.setTime(System.currentTimeMillis());
        signedInUser.setToken(serviceUtil.generateId("TOKEN-"));

        // Save the SignedInUser entity to the database
        return signedInUserRepository.save(signedInUser);
    }

    @Override
    public void removeSignedInUser(String userid) {
        // Fetch the signed-in user using the provided userid
        SignedInUser inUser = getSignedInUser(userid);

        // If the user is found, delete their session from the database
        if (inUser != null) {
            signedInUserRepository.delete(inUser);
        }
    }

    @Override
    public SignedInUser getSignedInUser(String userid) {

        if (userid == null) {
            // Throw an exception if userid is missing
            throw new NullResourceException("SignedInUserServiceImpl.removeSignedInUser(): Userid is not provided.");
        }

        // Return the signed-in user from the repository based on the userid
        return signedInUserRepository.findByUserUserid(userid);
    }

    @Override
    public boolean isExpired(String userid) {
        // Fetch the signed-in user using the provided userid
        SignedInUser signedInUser = getSignedInUser(userid);

        // If the userid is null, throw a custom exception
        if (signedInUser == null) {
            return false;
        }

        // Calculate the time difference between the current time and the session start time
        long currentTime = System.currentTimeMillis();
        long sessionTime = signedInUser.getTime();

        // 7 days in milliseconds
        long sessionValidityPeriod = 7L * 24 * 60 * 60 * 1000;

        // If the session time exceeds 7 days, return true indicating that the session is expired
        boolean isExpired = (currentTime - sessionTime) > sessionValidityPeriod;

        if (isExpired) {
            removeSignedInUser(userid);
        }

        return isExpired;
    }
}
