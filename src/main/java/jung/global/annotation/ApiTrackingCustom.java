package jung.global.annotation;

import jung.global.constants.ApiType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD) //메서드에 붙일 수 있다는 의미
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiTrackingCustom {
    ApiType type() default ApiType.UNKOWN;
}
