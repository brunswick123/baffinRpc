package com.my.baffinrpc.demo.server.impl;

import com.my.baffinrpc.demo.api.FutureService;

import java.util.Date;

public class FutureServiceImpl implements FutureService {
    public Date getDate() {
        try {
            System.out.println("running costing time task...");
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new Date();
    }
}
