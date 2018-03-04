package com.my.baffinrpc.core.common.model;

import java.io.Serializable;

public class ExceptionInfo implements Serializable {
    private final boolean isBizException;
    private final String exceptionMessage;

    public ExceptionInfo(boolean isBizException, String exceptionMessage) {
        this.isBizException = isBizException;
        this.exceptionMessage = exceptionMessage;
    }

    public boolean isBizException() {
        return isBizException;
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }
}
