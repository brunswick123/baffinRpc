package com.my.baffinrpc.core.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface ExtensionImpl {
    String name();
    Class<?> extension();
}
