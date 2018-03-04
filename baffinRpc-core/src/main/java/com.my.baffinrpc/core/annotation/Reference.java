package com.my.baffinrpc.core.annotation;

import com.my.baffinrpc.core.common.serialization.SerializeType;

import java.lang.annotation.*;

/***
 * 服务消费者在本地标注远程服务
 * 仅可用于成员属性
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface Reference {
    SerializeType serializeType() default SerializeType.Jdk; //序列化类型
    String[] asyncMethods() default {}; //异步方法名数组
    boolean keepConnected() default true; //是否使用长连接调用
}
