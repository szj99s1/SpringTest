package spring.test.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Andre.Z 2014-10-23 下午3:18:04<br>
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface FieldCheck {

    String name() default ""; // 参数名称

    String desc() default ""; // 字段描述

    int length() default 0; // 字段长度，汉字算2

}
