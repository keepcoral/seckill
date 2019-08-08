package com.bujidao.seckill.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;


@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        //这个类才是注解的真正的实现类(处理类)
        validatedBy = {IsMobileValidator.class}
)
public @interface isMobile {
    //true:参数必须有,进行格式校验
    //false:参数可以为空,不为空时也要进行进行格式校验
    boolean require() default true;//这是自定义的

    //提示信息
    String message() default "手机格式错误";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
