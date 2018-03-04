package com.my.baffinrpc.core.common.model;

public class ResultFactory {
    public static Result newNormalResult(Object invokeResult)
    {
        return new Result(invokeResult,null);
    }

    public static Result newVoidResult()
    {
        return new Result();
    }

    public static Result newExceptionResult(Throwable throwable)
    {
        return new Result(null,throwable);
    }
}
