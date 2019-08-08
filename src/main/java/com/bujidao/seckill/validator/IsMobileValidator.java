package com.bujidao.seckill.validator;

import com.bujidao.seckill.util.ValidatorUtil;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 注意的是:ConstraintValidator<IsMobile, String>
 * 第一个参数就是自定义注解的名字 第二个就是注解修饰的字段类型
 */
public class IsMobileValidator implements ConstraintValidator<isMobile, String> {

    //申明一个required 默认的值为false
    public boolean required = false;

    @Override
    public void initialize(isMobile constraintAnnotation) {
        //constraintAnnotation就是注解本身
        //初始化参数
        required = constraintAnnotation.require();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        //判断value是否必须 required为true则为必须 反之不是必须的
        //这里处理if-else是为了健壮性
        if (required) {
            return ValidatorUtil.isMobile(value);
        } else {
            //如果value不是必须的，那么如果value为空直接返回true
            if (StringUtils.isEmpty(value)) {
                return true;
            } else {
                return ValidatorUtil.isMobile(value);
            }
        }
    }
}
