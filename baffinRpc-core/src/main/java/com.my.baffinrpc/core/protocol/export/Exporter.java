package com.my.baffinrpc.core.protocol.export;

import com.my.baffinrpc.core.protocol.invoker.Invoker;

public interface Exporter {
    Invoker getInvoker();
    void unExport();
}
