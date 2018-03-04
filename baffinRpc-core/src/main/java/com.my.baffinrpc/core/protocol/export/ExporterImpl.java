package com.my.baffinrpc.core.protocol.export;

import com.my.baffinrpc.core.protocol.invoker.Invoker;

public class ExporterImpl implements Exporter {

    private final Invoker invoker;

    public ExporterImpl(Invoker invoker) {
        this.invoker = invoker;
    }

    @Override
    public Invoker getInvoker() {
        return invoker;
    }

    @Override
    public void unExport() {
        invoker.destroy();
    }
}
