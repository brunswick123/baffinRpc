package com.my.baffinrpc.core.message.base;

import com.my.baffinrpc.core.common.model.Result;
import com.my.baffinrpc.core.message.AbstractMessage;
import com.my.baffinrpc.core.message.Response;

import java.util.UUID;

public class BaseResponse extends AbstractMessage implements Response {

    private final Result result;


    public BaseResponse(Result result, UUID messageId, byte serializeTyoe ) {
        super(messageId, serializeTyoe, false);
        this.result = result;
    }


    @Override
    public Result getResult() {
        return result;
    }

}
