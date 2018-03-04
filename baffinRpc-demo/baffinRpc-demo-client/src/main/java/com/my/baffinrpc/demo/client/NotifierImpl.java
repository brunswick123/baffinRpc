package com.my.baffinrpc.demo.client;

import com.my.baffinrpc.demo.api.Notifier;

public class NotifierImpl implements Notifier {
    public void notify(int result,long timeUsed) {
        System.out.println("result is " + result + ", time cost:" + timeUsed);
    }
}
