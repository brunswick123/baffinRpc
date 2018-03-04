package com.my.baffinrpc.core.common.model;

import com.my.baffinrpc.core.common.exception.RPCBizException;
import com.my.baffinrpc.core.common.exception.RPCFrameworkException;

import java.io.Serializable;

public class Result implements Serializable {
    private final Object invokeResult;
    private final Throwable throwable;

    public Result(Object invokeResult, Throwable throwable) {
        this.invokeResult = invokeResult;
        this.throwable = throwable;
    }

    public Result() {
        this.invokeResult = null;
        this.throwable = null;
    }

    public Object recreateInvokeResult() throws Throwable {
        if (throwable != null) {
            if (throwable instanceof RPCBizException && throwable.getCause() != null) {
                //如果是RPC业务异常获取真实的业务异常再抛出
                throw throwable.getCause();
            } else
                throw throwable;
        }
        else
            return invokeResult;
    }

    public Object getInvokeResult() {
        return invokeResult;
    }
}
