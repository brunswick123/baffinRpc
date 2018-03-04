# baffin RPC

baffin RPC(Remote Procedure Call) 是一个基于java的分布式远程调用框架<br>
* 支持集群环境部署,提供多种高可用和负载均衡策略
* 运用zookeeper实现分布式环境下的服务注册和发现
* Nett/Mina传输层实现
* 支持异步future、回调、远程抛出异常<br>

程序的设计参考了dubbo的实现 https://github.com/alibaba/dubbo
