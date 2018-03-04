package com.my.baffinrpc.core.filter;

import com.my.baffinrpc.core.common.exception.RPCBizException;
import com.my.baffinrpc.core.common.exception.RPCFrameworkException;
import com.my.baffinrpc.core.common.model.Invocation;
import com.my.baffinrpc.core.common.model.Result;
import com.my.baffinrpc.core.common.model.ResultFactory;
import com.my.baffinrpc.core.protocol.invoker.Invoker;

public class ExceptionFilter implements Filter {
    @Override
    public Result invoke(Invoker invoker, Invocation invocation) {
        try
        {
            return invoker.invoke(invocation);
        }catch (RPCBizException e)
        {
            return ResultFactory.newExceptionResult(e);
        }
        catch (Exception e)
        {
            //非RPCBizException 异常包装成RPCFrameworkException
            return ResultFactory.newExceptionResult(new RPCFrameworkException(e));
        }

    }
}
