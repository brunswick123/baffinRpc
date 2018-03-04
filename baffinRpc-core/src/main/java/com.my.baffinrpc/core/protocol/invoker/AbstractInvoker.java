package com.my.baffinrpc.core.protocol.invoker;

import com.my.baffinrpc.core.common.model.URL;

public abstract class AbstractInvoker implements Invoker {

    protected final URL url;
    protected final Class<?> interfaceClz;

    protected AbstractInvoker(URL url, Class<?> interfaceClz) {
        this.url = url;
        this.interfaceClz = interfaceClz;
    }

    @Override
    public URL getUrl() {
        return url;
    }

    @Override
    public Class<?> getInterface() {
        return interfaceClz;
    }


}
