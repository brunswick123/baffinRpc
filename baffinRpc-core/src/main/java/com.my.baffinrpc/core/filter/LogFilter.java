package com.my.baffinrpc.core.filter;

import com.my.baffinrpc.core.common.model.Invocation;
import com.my.baffinrpc.core.common.model.Result;
import com.my.baffinrpc.core.protocol.invoker.Invoker;
import org.apache.log4j.Logger;

public class LogFilter implements Filter {

    private static final Logger logger = Logger.getLogger(LogFilter.class);

    @Override
    public Result invoke(Invoker invoker, Invocation invocation) throws Exception {
        logger.info("log filter before invoker");
        Result result = invoker.invoke(invocation);
        logger.info("log filter after invoker");
        return result;
    }
}
