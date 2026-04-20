package jung.global.aspect;

import jakarta.servlet.http.HttpServletRequest;
import jung.global.annotation.DuplicateAnnotation;
import jung.global.error.CommonErrorCode;
import jung.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;


@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class DuplicateCheckAspect {

    private static final String KEY = "DuplicateCheck::";

    private final HttpServletRequest request;
    private final StringRedisTemplate redisTemplate;

    @Around("@annotation(duplicateAnnotation)")
    public Object validateDuplicate(ProceedingJoinPoint joinPoint, DuplicateAnnotation duplicateAnnotation) throws Throwable {

      log.debug("[DuplicateAnnotation START]");

      String headerName = duplicateAnnotation.header();
      long ttl = duplicateAnnotation.ttl();

      String header = request.getHeader(headerName);
      String key = KEY+header;
      Boolean success = redisTemplate.opsForValue().setIfAbsent(key,"USED", Duration.ofMinutes(ttl));

        if (Boolean.FALSE.equals(success)) {
            log.info("Header Idempotency-Key Duplicate Request : {}", header);
            throw new BusinessException(CommonErrorCode.DUPLICATE_NOT);
        }

        return joinPoint.proceed();
    }
}
