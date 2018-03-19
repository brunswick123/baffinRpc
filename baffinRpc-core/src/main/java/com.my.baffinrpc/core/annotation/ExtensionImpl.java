package com.my.baffinrpc.core.annotation;

import java.lang.annotation.*;


/***
 *  扩展点实现
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface ExtensionImpl {
    String name();
    Class<?> extension(); //实现的扩展点
}
