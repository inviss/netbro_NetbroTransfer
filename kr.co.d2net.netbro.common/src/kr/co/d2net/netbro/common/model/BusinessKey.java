package kr.co.d2net.netbro.common.model;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.FIELD, ElementType.METHOD })
public @interface BusinessKey {
    Method[] include() default Method.ALL;
    Method[] exclude() default Method.NONE;
}

