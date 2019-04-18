package com.my.baffinrpc.core.message;

import com.my.baffinrpc.core.common.model.Result;

/**
 * Created by Administrator on 2019/4/18.
 */
public class DefaultResponse<MessageId> extends AbstractMessage<MessageId> implements Response<MessageId> {
    private final Result result;

    public DefaultResponse(Result result, MessageId messageId, byte serializeType) {
        super(messageId, serializeType, false);
        this.result = result;
    }

    @Override
    public Result getResult() {
        return result;
    }
}
