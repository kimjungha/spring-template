package jung.global.utils;

import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

public class CustomSpringELParser {

    /**
     * Spring > ExpressionParser (SPEL) : Spring 에서 제공하는 강력한 표현식 언어
     *  	XML, 어노테이션, AOP, Spring Security 등에서 표현식을 직접 입력하여 값을 설정 가능
     */

    public static Object getDynamicValue(String[] parameterNames, Object[] args, String key) {
        ExpressionParser parser = new SpelExpressionParser(); //SpEL 표현식 파서 생성
        StandardEvaluationContext context = new StandardEvaluationContext(); //SpEL 컨텍스트 생성

        for (int i = 0; i < parameterNames.length; i++) {
            context.setVariable(parameterNames[i], args[i]); //매개변수를 SpEL 컨텍스트에 등록
        }

        return parser.parseExpression(key).getValue(context, Object.class); // SpEL 표현식 실행 및 결과 반환
    }
}
