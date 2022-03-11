package com.example.demo.src.account.social;

import com.example.demo.config.Constant.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;
import org.springframework.context.annotation.Configuration;

import java.util.Locale;


public class SocialLoginTypeConverter implements Converter<String,SocialLoginType> {

    @Override
    public SocialLoginType convert(String s){
        return SocialLoginType.valueOf(s.toUpperCase());
    }

    @Override
    public JavaType getInputType(TypeFactory typeFactory) {
        return null;
    }

    @Override
    public JavaType getOutputType(TypeFactory typeFactory) {
        return null;
    }
}
