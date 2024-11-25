package io.edurt.datacap.service.annotation;

import io.edurt.datacap.common.view.EntityView;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DynamicJsonView
{
    Class<?> admin() default EntityView.AdminView.class;

    Class<?> user() default EntityView.UserView.class;
}
