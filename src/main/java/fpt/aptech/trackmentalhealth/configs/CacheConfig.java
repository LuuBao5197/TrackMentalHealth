package fpt.aptech.trackmentalhealth.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.SimpleCacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;

@Configuration
@EnableCaching
public class CacheConfig {

    @Value("${spring.data.redis.host:localhost}")
    private String redisHost;

    @Value("${spring.data.redis.port:6379}")
    private int redisPort;

    /**
     * Kết nối Redis với timeout ngắn (fail-fast nếu Redis chết).
     */
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
                .commandTimeout(Duration.ofMillis(500)) // timeout Redis
                .shutdownTimeout(Duration.ofMillis(100))
                .build();

        RedisStandaloneConfiguration serverConfig = new RedisStandaloneConfiguration(redisHost, redisPort);
        LettuceConnectionFactory factory = new LettuceConnectionFactory(serverConfig, clientConfig);

        try {
            factory.afterPropertiesSet(); // thử connect Redis
            System.out.println("✅ Redis connected successfully");
        } catch (Exception e) {
            System.err.println("⚠️ Redis chưa sẵn sàng, API sẽ fallback DB: " + e.getMessage());
        }

        return factory;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        try {
            template.afterPropertiesSet();
        } catch (Exception e) {
            System.err.println("⚠️ RedisTemplate chưa sẵn sàng: " + e.getMessage());
        }
        return template;
    }

    /**
     * Cấu hình CacheManager dùng Redis (TTL = 10 phút).
     * Kèm theo CacheErrorHandler để fallback khi Redis lỗi.
     */
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10))   // TTL cho cache
                .disableCachingNullValues();

        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(config)
                .build();
    }

    @Bean
    public CacheErrorHandler cacheErrorHandler() {
        return new SimpleCacheErrorHandler() {
            @Override
            public void handleCacheGetError(RuntimeException exception, org.springframework.cache.Cache cache, Object key) {
                System.err.println("⚠️ Redis GET error, fallback DB: " + exception.getMessage());
            }

            @Override
            public void handleCachePutError(RuntimeException exception, org.springframework.cache.Cache cache, Object key, Object value) {
                System.err.println("⚠️ Redis PUT error, fallback DB: " + exception.getMessage());
            }

            @Override
            public void handleCacheEvictError(RuntimeException exception, org.springframework.cache.Cache cache, Object key) {
                System.err.println("⚠️ Redis EVICT error, fallback DB: " + exception.getMessage());
            }

            @Override
            public void handleCacheClearError(RuntimeException exception, org.springframework.cache.Cache cache) {
                System.err.println("⚠️ Redis CLEAR error, fallback DB: " + exception.getMessage());
            }
        };
    }



}
