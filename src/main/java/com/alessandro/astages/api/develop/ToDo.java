package com.alessandro.astages.api.develop;

public @interface ToDo {
    String value();
    String before() default "";
}