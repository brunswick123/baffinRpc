package com.my.baffinrpc.core.filter;

import com.my.baffinrpc.core.common.exception.RPCFrameworkException;
import com.my.baffinrpc.core.common.model.Invocation;
import com.my.baffinrpc.core.common.model.Result;
import com.my.baffinrpc.core.protocol.invoker.Invoker;

import java.io.Serializable;

public class ArgsSerializableCheckFilter implements Filter {
    @Override
    public Result invoke(Invoker invoker, Invocation invocation) throws Exception {
        Object[] args = invocation.getArgs();
        if (args.length > 0)
        {
            for (Object o : args)
            {
                if (!(o instanceof Serializable))
                    throw new RPCFrameworkException(o.getClass().getName() +  " does not implements Serializable interface");
            }
        }
        return invoker.invoke(invocation);
    }
}
