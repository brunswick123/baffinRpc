package com.my.baffinrpc.core.filter;

import com.my.baffinrpc.core.common.model.Invocation;
import com.my.baffinrpc.core.common.model.Result;
import com.my.baffinrpc.core.protocol.invoker.Invoker;

public interface Filter {
    Result invoke(Invoker invoker, Invocation invocation) throws Exception;
}
