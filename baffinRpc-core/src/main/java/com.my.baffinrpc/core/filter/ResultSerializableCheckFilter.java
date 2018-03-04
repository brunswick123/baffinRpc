package com.my.baffinrpc.core.filter;

import com.my.baffinrpc.core.common.exception.RPCFrameworkException;
import com.my.baffinrpc.core.common.model.Invocation;
import com.my.baffinrpc.core.common.model.Result;
import com.my.baffinrpc.core.protocol.invoker.Invoker;

import java.io.Serializable;

public class ResultSerializableCheckFilter implements Filter {
    @Override
    public Result invoke(Invoker invoker, Invocation invocation) throws Exception {
        Result result = invoker.invoke(invocation);
        if (result != null)
        {
            Object invokeResult = result.getInvokeResult();
            if (invokeResult != null && !(invokeResult instanceof Serializable))
                throw new RPCFrameworkException("method return result " + invokeResult + " does not implements Serializable interface" );
        }
        return result;
    }
}
