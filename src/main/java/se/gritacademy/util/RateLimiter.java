package se.gritacademy.util;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class RateLimiter {

    private static final ConcurrentHashMap<String, Long> requestTimestamps = new ConcurrentHashMap<>();
    private static final long REQUEST_LIMIT_TIME = TimeUnit.SECONDS.toMillis(5); // 5 seconds per request

    /** Check if an IP is allowed to make a request */
    public static boolean isAllowed(String ip) {
        long currentTime = System.currentTimeMillis();
        Long lastRequestTime = requestTimestamps.get(ip);

        if (lastRequestTime == null || (currentTime - lastRequestTime) > REQUEST_LIMIT_TIME) {
            requestTimestamps.put(ip, currentTime);
            return true;
        }
        return false;
    }
}
