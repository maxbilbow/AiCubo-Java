package com.maxbilbow.aicubo.engine.collision.annotations;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by Max on 11/04/2016.
 */
@Retention(RUNTIME)
public @interface OnCollisionStart
{
  int order() default 0;
}
