package com.treefinance.crawler.framework.config.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since Feb 7, 2014 10:17:09 AM
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Path {

    String value() default "";
}