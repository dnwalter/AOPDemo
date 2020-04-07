package com.ousy.aopmodule.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by ousiyuan on 2018/12/29 0029.
 * description:
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SingleClick {
    int value() default 500;

    int[] except() default {};

    String[] exceptIdName() default {};
}
