package com.my.baffinrpc.core.annotation;

import java.lang.annotation.*;

/***
 * 注解方法参数中的回调参数
 * 参数必须是接口
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@Documented
public @interface Callback {
}
