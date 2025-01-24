package com.template.sprintTemplate;

import jung.global.annotation.HelloWorldAnnotation;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;

public class HelloWorldAnnotationTest {
    @HelloWorldAnnotation(world = "콩콩")
    static
    class HelloWorld{}

    @HelloWorldAnnotation()
    static
    class HelloWorldDefault{}

    @Test
    public void HelloWorldTest() throws Exception{
        Annotation[] annotations = HelloWorld.class.getAnnotations();
        for (Annotation annotation : annotations) {
            HelloWorldAnnotation helloWorldAnnotation = (HelloWorldAnnotation) annotation;
            System.out.println("helloWorldAnnotation 결과 = " + helloWorldAnnotation.world());
        }
    }
}
