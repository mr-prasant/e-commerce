package com.ecommerce.utility;

import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class ServiceUtil {

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
}
