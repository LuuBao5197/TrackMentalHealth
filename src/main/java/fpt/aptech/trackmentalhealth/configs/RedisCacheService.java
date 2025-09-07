package fpt.aptech.trackmentalhealth.configs;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.function.Supplier;

//@Service
//public class RedisCacheService {
//
//    private final RedisTemplate<String, Object> redisTemplate;
//    private final RedisCircuitBreaker breaker = new RedisCircuitBreaker();
//
//    public RedisCacheService(RedisTemplate<String, Object> redisTemplate) {
//        this.redisTemplate = redisTemplate;
//    }
//
//    /** Tương đương @Cacheable */
//    public <T> T get(String cacheName,String key, Supplier<Object> dbFallback) {
//        String redisKey = cacheName + "::" + key;
//        if (!breaker.allowRequest()) {
//            return (T) dbFallback.get();
//        }
//        try {
//            Object value = redisTemplate.opsForValue().get(redisKey);
//            if (value != null) {
//                breaker.recordSuccess();
//                return (T) value;
//            }
//            // Cache miss → query DB
//            Object dbValue = dbFallback.get();
//            if (dbValue != null) {
//                put(redisKey, dbValue); // cache lại
//
//            }
//            breaker.recordSuccess();
//            return (T) dbValue;
//        } catch (Exception e) {
//            breaker.recordFailure();
//            return (T) dbFallback.get();
//        }
//    }
//
//    /** Tương đương @CachePut */
//    public void put(String key, Object value) {
//        if (!breaker.allowRequest()) return;
//        try {
//            redisTemplate.opsForValue().set(key, value);
//            breaker.recordSuccess();
//        } catch (Exception e) {
//            breaker.recordFailure();
//        }
//    }
//
//    /** Tương đương @CacheEvict */
//    public void evict(String key) {
//        if (!breaker.allowRequest()) return;
//        try {
//            redisTemplate.delete(key);
//            breaker.recordSuccess();
//        } catch (Exception e) {
//            breaker.recordFailure();
//        }
//    }
//
//    /** Xóa toàn bộ cache */
//    public void clear() {
//        if (!breaker.allowRequest()) return;
//        try {
//            redisTemplate.getConnectionFactory().getConnection().flushAll();
//            breaker.recordSuccess();
//        } catch (Exception e) {
//            breaker.recordFailure();
//        }
//    }
//}

@Service
public class RedisCacheService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisCircuitBreaker breaker = new RedisCircuitBreaker();
    private boolean redisAvailable = true; // trạng thái Redis đã từng connect thành công

    public RedisCacheService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;

        // Thử kết nối Redis khi khởi tạo
        try {
            redisTemplate.hasKey("test-connection"); // dummy call
            redisAvailable = true;
        } catch (Exception e) {
            System.err.println("⚠️ Redis chưa sẵn sàng: fallback DB");
            redisAvailable = false;
        }
    }

    /** Tương đương @Cacheable */
    public <T> T get(String cacheName, String key, Supplier<Object> dbFallback) {
        String redisKey = cacheName + "::" + key;

        if (!redisAvailable || !breaker.allowRequest()) {
            // Redis offline → fallback DB
            return (T) dbFallback.get();
        }

        try {
            Object value = null;
            try {
                value = redisTemplate.opsForValue().get(redisKey);
            } catch (Exception e) {
                redisAvailable = false; // đánh dấu Redis offline
                System.err.println("⚠️ Redis GET error, fallback DB: " + e.getMessage());
                return (T) dbFallback.get();
            }

            if (value != null) {
                breaker.recordSuccess();
                return (T) value;
            }

            // Cache miss → query DB
            Object dbValue = dbFallback.get();
            if (dbValue != null) {
                put(redisKey, dbValue);
            }
            breaker.recordSuccess();
            return (T) dbValue;
        } catch (Exception e) {
            breaker.recordFailure();
            return (T) dbFallback.get();
        }
    }

    public void put(String key, Object value) {
        if (!redisAvailable || !breaker.allowRequest()) return;
        try {
            redisTemplate.opsForValue().set(key, value);
            breaker.recordSuccess();
        } catch (Exception e) {
            redisAvailable = false;
            breaker.recordFailure();
        }
    }

    public void evict(String key) {
        if (!redisAvailable || !breaker.allowRequest()) return;
        try {
            redisTemplate.delete(key);
            breaker.recordSuccess();
        } catch (Exception e) {
            redisAvailable = false;
            breaker.recordFailure();
        }
    }

    public void clear() {
        if (!redisAvailable || !breaker.allowRequest()) return;
        try {
            redisTemplate.getConnectionFactory().getConnection().flushAll();
            breaker.recordSuccess();
        } catch (Exception e) {
            redisAvailable = false;
            breaker.recordFailure();
        }
    }
}

