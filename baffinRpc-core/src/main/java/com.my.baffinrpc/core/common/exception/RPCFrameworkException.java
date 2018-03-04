package com.my.baffinrpc.core.common.exception;

/****
 * 表示来自rpc框架自身的异常,比如由于框架配置、网络、编码等引发的rpc调用失败,非来自实际方法的异常
 * @see RPCBizException 来自实际方法的异常
 */
public class RPCFrameworkException extends RPCException {
    public RPCFrameworkException(String errorMsg) {
        super(errorMsg);
    }

    public RPCFrameworkException(Throwable cause) {
        super(cause);
    }
}
