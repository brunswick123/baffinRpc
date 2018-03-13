package com.my.baffinrpc.core.protocol;

import com.my.baffinrpc.core.common.exception.RPCFrameworkException;
import com.my.baffinrpc.core.common.model.Invocation;
import com.my.baffinrpc.core.common.model.Result;
import com.my.baffinrpc.core.common.model.URL;
import com.my.baffinrpc.core.filter.ExceptionFilter;
import com.my.baffinrpc.core.filter.Filter;
import com.my.baffinrpc.core.protocol.export.Exporter;
import com.my.baffinrpc.core.protocol.invoker.Invoker;

import java.util.ArrayList;
import java.util.List;

public class FilterWrapProtocol implements Protocol {

    private final Protocol wrappedProtocol;
    private final List<Filter> filters = new ArrayList<>();

    public FilterWrapProtocol(Protocol wrappedProtocol) {
        this.wrappedProtocol = wrappedProtocol;
    }

    public Protocol addFilter(Filter filter)
    {
        if (filter != null)
            filters.add(filter);
        return this;
    }

    @Override
    public Exporter export(Invoker invoker) throws RPCFrameworkException {
        return wrappedProtocol.export(buildFilterChainInvoker(invoker));
    }

    @Override
    public Invoker refer(URL url, Class<?> interfaceClz) {
        return buildFilterChainInvoker(wrappedProtocol.refer(url,interfaceClz));
    }

    private Invoker buildFilterChainInvoker(final Invoker invoker)
    {
        Invoker outerInvoker = invoker;
        if (filters.size() > 0)
        {
            for (int i = filters.size() - 1; i >= 0; i--)
            {
                final Filter filter = filters.get(i);
                final Invoker nestedInvoker = outerInvoker;
                outerInvoker = new Invoker() {
                    @Override
                    public Result invoke(Invocation invocation) throws Exception {
                        return filter.invoke(nestedInvoker,invocation);
                    }
                    @Override
                    public URL getUrl() {
                        return invoker.getUrl();
                    }

                    @Override
                    public Class<?> getInterface() {
                        return invoker.getInterface();
                    }

                    @Override
                    public boolean isAvailable() {
                        return invoker.isAvailable();
                    }

                    @Override
                    public void destroy() {
                        nestedInvoker.destroy();
                    }
                };
            }
        }
        return outerInvoker;
    }

    @Override
    public int getPort() {
        return wrappedProtocol.getPort();
    }

    @Override
    public void destroy() {
        wrappedProtocol.destroy();
    }
}
