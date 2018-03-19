package com.my.baffinrpc.core.common.exception;

public class RPCNoServiceProviderException extends RPCFrameworkException {
    public RPCNoServiceProviderException(String errorMsg) {
        super(errorMsg);
    }

    public RPCNoServiceProviderException(Throwable cause) {
        super(cause);
    }
}
