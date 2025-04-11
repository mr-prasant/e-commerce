package com.ecommerce.utility;

import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

@Component
public class ServiceUtil {

    public String generateId(String prefix) {
        long timestamp = System.currentTimeMillis();
        int randomNumber = ThreadLocalRandom.current().nextInt(100000, 10000001);

        return prefix + timestamp + randomNumber;
    }
}
