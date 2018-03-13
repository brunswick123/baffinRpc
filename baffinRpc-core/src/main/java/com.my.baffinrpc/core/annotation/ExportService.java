package com.my.baffinrpc.core.annotation;


import org.springframework.stereotype.Component;

import java.lang.annotation.*;

import static com.my.baffinrpc.core.common.constant.DefaultConfig.SERVICE_DEFAULT_PORT;

/***
 * 标注服务的实现类
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Component
public @interface ExportService
{
    Class exportInterface() default void.class;
    int port() default SERVICE_DEFAULT_PORT; //服务端口
    String host() default ""; //服务地址
}
