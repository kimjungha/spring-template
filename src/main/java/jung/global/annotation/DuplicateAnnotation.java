package jung.global.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DuplicateAnnotation {
    String header() default "Idempotency-Key";
    TimeUnit timeUnit() default TimeUnit.MINUTES;
    long ttl() default 15L; // 15 분
}
