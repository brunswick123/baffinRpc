package com.my.baffinrpc.core.config;

import com.my.baffinrpc.core.common.model.CallbackInfo;

import java.io.Serializable;

public class MethodConfig implements Serializable {
    private String methodName;
    private boolean async;
    private boolean callback;
    private CallbackInfo callbackInfo;

    public MethodConfig(String methodName, boolean async, boolean callback, CallbackInfo callbackInfo) {
        this.methodName = methodName;
        this.async = async;
        this.callback = callback;
        this.callbackInfo = callbackInfo;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public boolean isAsync() {
        return async;
    }

    public void setAsync(boolean async) {
        this.async = async;
    }

    public boolean isCallback() {
        return callback;
    }

    public void setCallback(boolean callback) {
        this.callback = callback;
    }

    public CallbackInfo getCallbackInfo() {
        return callbackInfo;
    }

    public void setCallbackInfo(CallbackInfo callbackInfo) {
        this.callbackInfo = callbackInfo;
    }

    public static MethodConfig defaultMethodConfig()
    {
        return new MethodConfig(null,false,false,null);
    }





}
