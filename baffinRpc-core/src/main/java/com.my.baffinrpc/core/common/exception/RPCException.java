package com.my.baffinrpc.core.common.exception;

import java.io.Serializable;

public abstract class RPCException extends RuntimeException implements Serializable {
    protected RPCException(String errorMsg)
    {
        super(errorMsg);
    }

    protected RPCException(Throwable cause)
    {
        super(cause);
    }
}
