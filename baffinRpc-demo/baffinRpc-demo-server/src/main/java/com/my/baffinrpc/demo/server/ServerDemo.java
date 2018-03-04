package com.my.baffinrpc.demo.server;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ServerDemo {
    public static void main(String... args) throws InterruptedException {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
    }
}
