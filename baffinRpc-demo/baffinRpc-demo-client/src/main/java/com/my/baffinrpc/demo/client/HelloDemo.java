package com.my.baffinrpc.demo.client;

import com.my.baffinrpc.demo.api.HelloService;
import com.my.baffinrpc.demo.api.model.LocationAddress;
import com.my.baffinrpc.demo.api.model.Person;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/***
 * rpc helloWorld示例
 */
public class HelloDemo {
    public static void main(String... args)
    {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        HelloService helloService = applicationContext.getBean("helloService",HelloService.class);
        System.out.println("result is " + helloService.hello("world",123));
        System.out.println("result is " + helloService.hiPerson(new Person("world",new LocationAddress(0,"solar"))));
    }
}
