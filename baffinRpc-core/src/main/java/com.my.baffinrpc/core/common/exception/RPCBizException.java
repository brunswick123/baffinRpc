package com.my.baffinrpc.core.common.exception;

import java.io.Serializable;

/***
 * 代表调用远程服务 服务方法本身抛出的异常 是方法本身的业务异常 和rpc框架无关
 * @see RPCFrameworkException rpc框架的异常
 *
 */
public class RPCBizException extends RPCException implements Serializable {

    public RPCBizException(String errorMsg) {
        super(errorMsg);
    }

    public RPCBizException(Throwable cause) {
        super(cause);
    }
}
