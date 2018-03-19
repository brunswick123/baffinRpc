package com.my.baffinrpc.core.common.model;


import java.io.Serializable;

public class Invocation implements Serializable{
    private final String interfaceName;
    private final String methodName;
    private final Object[] args;
    private boolean async;
    private final boolean isOneWay;
    private byte serializationType;
    private CallbackInfo callbackInfo;
    private URL url;

    public Invocation(String interfaceName, String methodName, Object[] args, boolean async, boolean isOneWay, byte serializationType, CallbackInfo callbackInfo) {
        this.interfaceName = interfaceName;
        this.methodName = methodName;
        this.args = args;
        this.async = async;
        this.isOneWay = isOneWay;
        this.serializationType = serializationType;
        this.callbackInfo = callbackInfo;
    }

    public Invocation(String interfaceName, String methodName, Object[] args, boolean isOneWay) {
        this.interfaceName = interfaceName;
        this.methodName = methodName;
        this.args = args;
        this.isOneWay = isOneWay;
    }


    public void setAsync(boolean async) {
        this.async = async;
    }


    public void setSerializationType(byte serializationType) {
        this.serializationType = serializationType;
    }

    public void setCallbackInfo(CallbackInfo callbackInfo) {
        this.callbackInfo = callbackInfo;
    }

    public void setUrl(URL url) {
        this.url = url;
        this.async = url.isMethodAsync(methodName);
        CallbackInfo callbackInfo = url.getCallbackInfo(methodName);
        if (callbackInfo != null)
            this.callbackInfo = callbackInfo;
    }

    public String getMethodName() {
        return methodName;
    }

    public Object[] getArgs() {
        return args;
    }

    public boolean isAsync() {
        return async;
    }

    public byte getSerializationType() {
        return serializationType;
    }

    public URL getUrl() {
        return url;
    }

    public boolean isOneWay() {
        return isOneWay;
    }

    public CallbackInfo getCallbackInfo() {
        return callbackInfo;
    }

    public String getInterfaceName() {
        return interfaceName;
    }
}
