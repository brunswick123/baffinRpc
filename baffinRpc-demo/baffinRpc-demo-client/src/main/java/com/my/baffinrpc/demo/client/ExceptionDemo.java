package com.my.baffinrpc.demo.client;

import com.my.baffinrpc.demo.api.ExceptionService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/***
 * rpc远程抛出异常示例
 */
public class ExceptionDemo {
    public static void main(String... args)
    {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        ExceptionService exceptionService = applicationContext.getBean("exceptionService",ExceptionService.class);
        try
        {
            exceptionService.sayHiAndThrowException();
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        try
        {
            exceptionService.sayHiAndThrowCheckedException();
        }catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
