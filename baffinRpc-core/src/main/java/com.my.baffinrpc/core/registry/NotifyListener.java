package com.my.baffinrpc.core.registry;

import com.my.baffinrpc.core.common.model.URL;

import java.util.List;

/***
 * 服务URL变化监听器
 */
public interface NotifyListener {

    /***
     * 服务URL发生变化
     * @param urls 新的服务URL
     */
    void notify(List<URL> urls);
}
