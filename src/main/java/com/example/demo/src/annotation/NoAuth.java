package com.example.demo.src.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME) //어노테이션 레벨을 결정짓는다.
@Target({ElementType.TYPE,ElementType.METHOD})//선언된 어노테이션이 적용될수 있는 위치를 결정. TYPE-class,interface,enum에 적용.
public @interface NoAuth {
}
