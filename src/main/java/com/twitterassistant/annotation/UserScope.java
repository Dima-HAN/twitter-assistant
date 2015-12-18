package com.twitterassistant.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.springframework.stereotype.Component;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

@Component
@Target(value = { ElementType.METHOD, ElementType.TYPE })
@Retention(value = RetentionPolicy.RUNTIME)
public @interface UserScope {
}
