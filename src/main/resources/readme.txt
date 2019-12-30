(1)Lettuce 和 Jedis 都是Redis的client，所以他们都可以连接 Redis Server
(2)Jedis在实现上是直接连接的Redis Server，如果在多线程环境下是非线程安全的。
(3)Lettuce的连接是基于Netty的，Netty 是一个多线程、事件驱动的 I/O 框架。
连接实例可以在多个线程间共享，当多线程使用同一连接实例时，是线程安全的。


【1】application-single 单机模式

问题描述：SpringBoot连接Redis服务出现DENIED Redis is running in protected mode because protected mode is enabled
解决方法：是说Redis服务处于保护模式，我们需要修改配置文件redis.conf。将NETWORK下的protected-mode yes修改

【2】application-sentinel 主从哨兵模式
【3】application-cluster 集群模式