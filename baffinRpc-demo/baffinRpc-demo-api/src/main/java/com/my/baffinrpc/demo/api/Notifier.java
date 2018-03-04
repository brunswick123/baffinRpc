package com.my.baffinrpc.demo.api;

import java.io.Serializable;

public interface Notifier extends Serializable {
    void notify(int result,long timeUsed);
}
