package com.my.baffinrpc.demo.client;


import com.my.baffinrpc.demo.api.CallbackService;
import com.my.baffinrpc.demo.api.Notifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/***
 * rpc回调示例
 */
public class CallbackDemo {
    public static void main(String... args)
    {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        CallbackService callbackService = applicationContext.getBean("callbackService",CallbackService.class);
        callbackService.calculateAdd(1, 1,new NotifierImpl());
    }
}
