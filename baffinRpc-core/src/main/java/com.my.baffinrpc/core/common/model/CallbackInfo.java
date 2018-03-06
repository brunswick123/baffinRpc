package com.my.baffinrpc.core.common.model;

import java.io.Serializable;

public class CallbackInfo implements Serializable {
    private final Class<?> callbackInterface;
    private final int methodArgsIndex;
    private final URL callbackURL;

    public CallbackInfo(Class<?> callbackInterface, int methodArgsIndex, URL callbackURL) {
        this.callbackInterface = callbackInterface;
        this.methodArgsIndex = methodArgsIndex;
        this.callbackURL = callbackURL;
    }

    public Class<?> getCallbackInterface() {
        return callbackInterface;
    }

    public int getMethodArgsIndex() {
        return methodArgsIndex;
    }

    public URL getCallbackURL() {
        return callbackURL;
    }
}
