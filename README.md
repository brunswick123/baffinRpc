## baffin RPC

baffin RPC(Remote Procedure Call) 是一个基于java的分布式远程调用框架<br>
* 支持集群环境部署,提供多种高可用和负载均衡策略
* 运用zookeeper实现分布式环境下的服务注册和发现
* Netty/Mina传输层实现
* 支持异步future、回调、远程抛出异常
* Spring XML样式的配置方式<br>

程序的设计参考了dubbo https://github.com/alibaba/dubbo

## 快速入门
baffin RPC依赖zookeeper实现服务注册和发现,请先确保已开启zookeeper。<br>
#### 定义接口
```
public interface HelloService {
    String hello(String name, int number);
}
```

#### 实现接口
```
public class HelloServiceImpl implements HelloService {
    public String hello(String name, int number) {
        System.err.println("greeting from " + name);
        return "hello " + name + ", with number: " + number;
    }
}
```


#### 配置服务提供者applicationContext.xml
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

#### 配置服务消费者applicationContext.xml
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

#### 启动服务提供者
```
public class ServerDemo {
    public static void main(String... args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
    }
}
```

#### 启动服务消费者 调用服务
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
更多示例请参考baffinRpc-demo。

## SPI

baffinRpc 采用SPI机制实现程序的可扩展性,对于程序中的任意扩展点,可使用框架自带的扩展点实现,也可以根据扩展点接口自己实现。

#### 扩展点

##### @Extension
框架内的扩展点采用@Extension注解标记,目前包括以下扩展点
* com.my.baffinrpc.core.protocol.proxy.ProxyFactory
* com.my.baffinrpc.core.communication.transport.TransportFactory 
* com.my.baffinrpc.core.cluster.loadbalance.LoadBalanceStrategy
* com.my.baffinrpc.core.cluster.highavailable.HighAvailableStrategy
* com.my.baffinrpc.core.common.serialization.Serialization
* com.my.baffinrpc.core.message.MessageFactory
* com.my.baffinrpc.core.registry.zookeeper.zookeeperClient.ZookeeperClientFactory
