package com.my.baffinrpc.demo.server.impl;

import com.my.baffinrpc.demo.api.CallbackService;
import com.my.baffinrpc.demo.api.Notifier;

import java.io.Serializable;

public class CallbackServiceImpl implements CallbackService,Serializable {
    public void calculateAdd(int firstNumber,int secondNumber, Notifier notifier) {
        System.out.println("received task " + firstNumber + " + " + secondNumber + ", start calculate");
        long start = System.currentTimeMillis();
        int result = 0;
        try {
            Thread.sleep(2000);
            result = firstNumber + secondNumber;
        }catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        System.out.println("task finished");
        long timeCost = System.currentTimeMillis() - start;
        notifier.notify(result,timeCost);
    }
}
