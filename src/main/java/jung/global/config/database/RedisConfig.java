package jung.global.config.database;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.codec.JsonJacksonCodec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
@EnableCaching
public class RedisConfig {
    private static final String REDISSON_HOST_PREFIX = "redis://";

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    /** Lettuce : 캐싱용 */
    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofMinutes(10)) // TTL 10분 설정
            .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
            .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));

        return RedisCacheManager.builder(redisConnectionFactory)
            .cacheDefaults(redisCacheConfiguration)
            .build();
    }

    /** Redisson  : 분산락용
     * RedissonClient 빈을 생성하는 역할 수행
     */
    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();  // Redisson 설정할 수 있는 객체 생성
        config.useSingleServer()  // 단일 redis 서버 사용하겠다
            .setAddress(REDISSON_HOST_PREFIX + redisHost + ":" + redisPort); //레디스 주소 설정
        config.setCodec(new JsonJacksonCodec()); //데이터를 JSON 변환하여 저장하겠다

        return Redisson.create(config);
    }
}
