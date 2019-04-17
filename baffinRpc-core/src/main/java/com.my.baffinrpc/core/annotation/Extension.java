package com.my.baffinrpc.core.annotation;

import java.lang.annotation.*;


/***
 * 标注扩展点接口
 * 只能用于标注接口
 * 框架内可以供用户扩展替换的部分采用此注解标记
 * 进行扩展需要实现扩展点接口,再使用ExtensionImpl注解标记
 * @see ExtensionImpl
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface Extension {

}
