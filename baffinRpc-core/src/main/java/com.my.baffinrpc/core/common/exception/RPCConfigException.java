package com.my.baffinrpc.core.common.exception;

public class RPCConfigException extends RPCFrameworkException {
    public RPCConfigException(String errorMsg) {
        super(errorMsg);
    }

    public RPCConfigException(Throwable throwable)
    {
        super(throwable);
    }
}
