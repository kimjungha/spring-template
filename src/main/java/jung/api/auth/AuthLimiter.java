package jung.api.auth;

import jung.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

import static jung.global.error.BusinessErrorCode.LOGIN_RATE_LIMIT_EXCEEDED;

@Component
@RequiredArgsConstructor
public class AuthLimiter {

    private static final String KEY_PREFIX = "Login::";
    private static final Duration BLOCK_DURATION = Duration.ofMinutes(10);
    private static final int MAX_COUNT = 5;

    private final StringRedisTemplate redisTemplate;

    public void recordFail(String email) {

        String key = KEY_PREFIX+email;
        Long count = redisTemplate.opsForValue().increment(key);

        if(count != null && count == 1){
            redisTemplate.expire(key,BLOCK_DURATION);
        }
    }

    public void checkFailCount(String email) {

        String count = redisTemplate.opsForValue().get(KEY_PREFIX+email);

        if(count != null && Integer.parseInt(count) > MAX_COUNT) {
            throw new BusinessException(LOGIN_RATE_LIMIT_EXCEEDED);
        }
    }
}
