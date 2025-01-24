package jung.global.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
/** AOP */
@Aspect
@Component
public class ApiTrackingAspect {
//    @Around("annotation(*jung.global.annotation)") 애노테이션 적용
//    @Around("execution(* jung.api..*Controller.*(..))") 컨트롤러에 적용

    @Pointcut("@annotation(*jung.global.annotation.ApiTrackingCustom)")
    private void trackingController() { /* Point CUT이므로 로직 없음 */ }

    @Around("trackingController()")
    public Object saveApiTrackingAdvice(ProceedingJoinPoint joinPoint) throws Throwable{
        boolean isSuccess = true;
        Object result = null;
        try{
            result = joinPoint.proceed();
        }catch(Throwable e){
            isSuccess = false;
        }
        // 메인 타겟 실행 완료 후에, 사용자 로그 저장
        saveLog(joinPoint,result,isSuccess);
        return result;
    }

    private void saveLog(ProceedingJoinPoint joinPoint,Object result,boolean isSuccess){

    }
}
