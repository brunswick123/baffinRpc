package com.my.baffinrpc.demo.client;

import com.my.baffinrpc.core.common.RpcContext;
import com.my.baffinrpc.demo.api.FutureService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/***
 * rpc异步future示例
 */
public class FutureDemo {
    public static void main(String... args)
    {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        FutureService futureService = applicationContext.getBean("futureService",FutureService.class);
        futureService.getDate();
        Future<?> dateFuture = RpcContext.getContext().getFuture();
        try {
            System.out.println("async result is " + dateFuture.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
