package com.my.baffinrpc.core.cluster;

import com.my.baffinrpc.core.protocol.invoker.Invoker;
import com.my.baffinrpc.core.registry.NotifyListener;

import java.util.List;

public interface Directory<T> extends NotifyListener{
    List<Invoker> getInvokers();
    Class<T> getInterface();
}
