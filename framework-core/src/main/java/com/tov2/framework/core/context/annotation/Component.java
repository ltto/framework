package com.tov2.framework.core.context.annotation;

import com.tov2.framework.BaseInfo;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Component {
    String value() default "";

    Class loadAfter() default BaseInfo.class;
}
