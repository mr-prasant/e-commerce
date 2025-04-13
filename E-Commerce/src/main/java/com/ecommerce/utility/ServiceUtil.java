package com.ecommerce.utility;

import com.ecommerce.exception.InvalidInputResourceException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class ServiceUtil {

    private final PasswordEncoder encoder = new BCryptPasswordEncoder(12);

    // generating id
    public String generateId(String prefix) {
        long timestamp = System.currentTimeMillis();
        int randomNumber = ThreadLocalRandom.current().nextInt(100000, 10000001);

        return prefix + timestamp + randomNumber;
    }

    // format date from millisecond
    public String formatDateFromMillis(long millis) {
        Date date = new Date(millis);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        return sdf.format(date);
    }

    public void verifyUserRole(String role) {
        if (!("admin, user, admin").contains(role)) {
            throw new InvalidInputResourceException("Invalid role: " + role);
        }
    }

    public PasswordEncoder getEncoder() {
        return encoder;
    }
}
