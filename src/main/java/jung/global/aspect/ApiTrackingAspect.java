package jung.global.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
/** AOP */
@Aspect
@Component
public class ApiTrackingAspect {
    @Around("annotation(*jung.global.annotation)")
//    @Around("execution(* jung.api..*Controller.*(..))")
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
