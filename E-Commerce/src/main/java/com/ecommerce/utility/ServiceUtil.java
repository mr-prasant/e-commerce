package com.ecommerce.utility;

import com.ecommerce.exception.InvalidInputResourceException;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

@Component
public class ServiceUtil {

    public String generateId(String prefix) {
        long timestamp = System.currentTimeMillis();
        int randomNumber = ThreadLocalRandom.current().nextInt(100000, 10000001);

        return prefix + timestamp + randomNumber;
    }

    public void verifyUserRole(String role) {
        if (!role.equals("admin") && !role.equals("user")) {
            throw new InvalidInputResourceException("Invalid role: " + role);
        }
    }
}
