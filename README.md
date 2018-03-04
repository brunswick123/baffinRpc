# baffin RPC

baffin RPC(Remote Procedure Call) 是一个基于java的分布式远程调用框架<br>
* 支持集群环境部署,提供多种高可用和负载均衡策略
* 运用zookeeper实现分布式环境下的服务注册和发现
* Nett/Mina传输层实现
* 支持异步future、回调、远程抛出异常
* Spring XML样式的配置方式<br>

程序的设计参考了dubbo的实现 https://github.com/alibaba/dubbo

## 快速入门
baffin RPC依赖zookeeper实现服务注册和发现,请先确保已开启zookeeper.<br>
##### 定义接口
```
public interface HelloService {
    String hello(String name, int number);
}
```

##### 实现接口
```
public class HelloServiceImpl implements HelloService {
    public String hello(String name, int number) {
        System.err.println("greeting from " + name);
        return "hello " + name + ", with number: " + number;
    }
}
```


##### 配置服务提供者applicationContext.xml
```
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:rpc="http://www.my.baffinrpc.com/schema"
       xsi:schemaLocation="http://www.springframework.org/schema/beans 
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.my.baffinrpc.com/schema http://www.my.baffinrpc.com/schema/baffinRpc.xsd" >

    <!--配置bean-->
    <bean id="helloService" class="com.my.baffinrpc.demo.server.impl.HelloServiceImpl"/>

    <!--配置zookeeper-->
    <rpc:registry address="192.168.13.99:2181" type="zookeeper"/>

    <!--配置helloService服务-->
    <rpc:exportService name="helloService" interface="com.my.baffinrpc.demo.api.HelloService" ref="helloService"/>
</beans>
```

##### 配置服务消费者applicationContext.xml
```
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:rpc="http://www.my.baffinrpc.com/schema"
       xsi:schemaLocation="http://www.springframework.org/schema/beans 
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.my.baffinrpc.com/schema http://www.my.baffinrpc.com/schema/baffinRpc.xsd" >

    <!--配置zookeeper-->
    <rpc:registry address="192.168.13.99:2181" type="zookeeper"/>

    <!--引用服务-->
    <rpc:reference name="helloService" interface="com.my.baffinrpc.demo.api.HelloService"/>
</beans>
```

##### 启动服务提供者
```
public class ServerDemo {
    public static void main(String... args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
    }
}
```

##### 启动服务消费者 调用服务
```
public class HelloDemo {
    public static void main(String... args)
    {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        HelloService helloService = applicationContext.getBean("helloService",HelloService.class);
        System.out.println("result is " + helloService.hello("world",123));
    }
}
```



