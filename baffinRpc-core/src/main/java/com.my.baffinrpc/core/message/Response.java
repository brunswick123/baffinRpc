package com.my.baffinrpc.core.message;

import com.my.baffinrpc.core.common.model.Result;

public interface Response extends Message {
    Result getResult();
}
