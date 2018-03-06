package com.my.baffinrpc.core.communication;

public interface Server{
    /***
     * 绑定端口,开启监听
     * @return 绑定是否成功 true成功 false失败
     *
     */
    boolean bind();
}
