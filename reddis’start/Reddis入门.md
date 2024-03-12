

# Reddis入门

> #### 一、安装与配置

##### 1.安装

###### 	Mac 安装方法

```shell
//查询redis版本
brew search redis
//安装redis
brew install redis@'版本号'
//通过homebrew方法安装的reddis位于/opt/homebrew/bin/目录中
```

###### 	Linux安装方法

```shell
//reddis官网下载压缩包
//解压到usr/local/src目录中
//进入解压好的reddis文件
make && make install
```

###### 	注意：

这个东西尽管安装好了，它还是给你报两个error来恶心你,这个时候你进入usr/local/bin目录下看reddis-server这些存不存在，存在的话就不用理会报错了

##### 2.配置

- [ ] ###### 备份redis.conf

###### 	Linux下这个文件在/usr/local/src/redis-x.x.x中

###### 	Mac中redis.conf在/opt/homebrew/etc/目录下

```
//提前做一个备份以免原文件配置出错
cp redis.conf redis.conf.bck
```

- [ ] ###### 配置redis.conf

```conf
#bind改为0.0.0.0可以在任意位置访问
bind 0.0.0.0
#daemonize改为yes转变为守护进程，在后台运行
daemonize yes
#requirepass 设置密码
#这个是提前注释掉的所以有点难找在900行左右
requirepass ${password}
#logfile ""默认为空
logfile "redis.log"

```

```
# 监听的端口
port 6379
# 工作目录，默认是当前目录，也就是运行redis-server时的命令，日志、持久化等文件会保存在这个目录
dir .
# 数据库数量，设置为1，代表只使用1个库，默认有16个库，编号0~15
databases 1
# 设置redis能够使用的最大内存
maxmemory 512mb
# 日志文件，默认为空，不记录日志，可以指定日志文件名
logfile "redis.log"
```

> #### 二、redis常见命令 

###### redis数据结构

| String    | Hello redis |
| --------- | ----------- |
| Hash      |             |
| List      |             |
| Set       |             |
| SortedSet |             |
| GEO       |             |
| BitMap    |             |
| HyperLog  |             |

##### 1.开机自启[Linux]

首先，新建一个系统服务文件：

```
vi /etc/systemd/system/redis.service
```

内容如下

```
[Unit]
Description=redis-server
After=network.target

[Service]
Type=forking
ExecStart=/usr/local/bin/redis-server /usr/local/src/redis-6.2.6/redis.conf
PrivateTmp=true

[Install]
WantedBy=multi-user.target
```

完成后可以通过如下指令执行redis

```
# 启动
systemctl start redis
# 停止
systemctl stop redis
# 重启
systemctl restart redis
# 查看状态
systemctl status redis

#自启
systemctl enable redis
```

##### 2.redis客户端

- [ ] ###### 	命令行客户端 	redis-cli


  ```
  redis-cli [options] [commonds]
  
  [options]:
  -h 指定要连接的redis节点的IP地址，默认127.0.0.1
  -p 指定要连接的redis节点的端口，默认6379
  -a 指定redis的访问密码
  
  [commods]一般不指定commond，会进入redis-cil的交互控制台 
  
  redis-cli -a Password
  
  ping 与redis服务器做心跳测试时，服务端正常返回pong
  ```

##### 3.常见命令

- ###### KEYS：查看符合模版的所有key[模糊查找]	不建议在生产设备使用

- ###### DEL  ：删除一个指定的KEY

- ###### EXISTS：判断KEY是否存在

- ###### EXPIRE：给KEY设置有效期[sec]，有效期到时该KEY被自动删除(-1表示永久有效)

- ###### TTL：查看KEY的有效期



##### 4.String 类型常见命令

###### String类型的value为字符串，根据字符串的格式不同，可以分为3类

- string：普通字符串
- int：整数类型，可以做自增、自减操作
- float：浮点类型，类型，可以做自增、自减操作

![String的存储格式](/Users/zhangliuxiao/Documents/myrepository/reddis’start/assets/String的存储格式.png)

###### 命令：

- SET：添加或修改已经存在的一个String类型的键值对
- GET：根据KEY获取对应的VALUE
- MSET：批量添加多个String键值对
- MGET：根据多个KEY获取多个String类型的VALUE
- INCR：让一个整型的KEY自增1
- INCRBY：让一个整型的KEY自增并指定步长
- INCRBYFLOAT：让一个浮点型的KEY自增并指定步长
- DCRE：让一个整型的KEY自减
- SETNX：添加一个String类型的键值对，前提是这个KEY不存在，否则不执行
- SETEX：添加一个String类型的键值对，并指定有效期

###### Redis中没有Table的概念，如何区分不同的KEY？

```
KEY的结构：项目名：业务名：类型：ID	自动分包形成层级结构
heima:user:1
heima:product:1
VALUE以JSON字符串的格式存储
{"id":1,"name":"Jack","age":18}
```

##### 5.Hash类型常见命令

#### ![Hash的存储格式](/Users/zhangliuxiao/Documents/myrepository/reddis’start/assets/Hash的存储格式.png)

###### 命令：

- HSET 	HSET [KEY] [field] [value]
- HGET	HGET [KEY] [field]
- HMSET     HMSET [KEY] [field] [value] [field] [value] .......[field] [value]
- HMGET     HMGET [KEY] [field] [field]..... [field]
- HGETALL  HGETALL [KEY]
- HKEYS       获取一个hash类型的KEY中所有类型的field
- HVALS       获取一个hash类型的KEY中所有类型的value
- HINVRBY   让一个hash类型key的字段值指定步长
- HSTENX     添加一个hash类型的key的field值，前提是该field不存在

##### 6.List类型常见命令 

###### List可以看做是一个双向链表的结构，既可以支持正向检索也可以支持反向检索。

###### 特征：

- 有序
- 元素可以重复
- 插入和删除速度快
- 查询速度一般

###### 命令：

- LPUSH
- LPOP
- RPUSH
- RPOP
- LRANGE    返回一段角标范围内的所有元素
- BLPOP和BRPOP   与LPOP和RPOP相似，不过在没有元素时等待一段时间，而不是直接返回null

###### 可以用List模拟队列、栈、阻塞队列

##### 7.Set类型常见命令

###### 特征：

- 无序
- 元素不可重复
- 查找快
- 支持交集、并集、差级等功能

###### 命令：

- SADD
- SREM
- SCARD：返回Set中元素个数
- SISMEMBER：判断一个元素是否存在于Set中
- SMEMBERS：获取Set中的所有元素
- SINTER：求KEY1与KEY2的交集
- SDIFF   ：求KEY1与KEY2的差集
- SUNION：求KEY1与KEY2的并集

##### 8.SortedSet（跳表SkipList+hash）

###### 特征：

- 可排序
- 元素不可重复
- 查询速度快

###### 命令：

- ZADD
- ZREM
- ZSCORE
- ZRANK
- ZCARD
- ZCOUNT
- ZINCRBY
- ZRANGE
- ZRANGEBYSCORE
- ZDIFF、ZINTER、ZUNION



> #### 三、Redis的java客户端



![redis的java客户端](/Users/zhangliuxiao/Documents/myrepository/reddis’start/assets/redis的java客户端.png)

##### 1.Jedis的连接与使用

- ###### 创建maven项目，引入jedis依赖

  ```xml
  <dependency>
  	<groupId>redis.clients</groupId>
    <artifactId>jedis</artifactId>
    <version>3.7.0</version>
  </dependency>
  ```

- ###### 建立连接

  ```java
  public class JedisTest {
      private Jedis jedis;
      @BeforeEach
      void setUb(){
          jedis = new Jedis("127.0.0.1",6379);
          jedis.auth("951026");
          jedis.select(0);
      }
  }
  ```

- ###### Jedis 中的操作与命令行操作几乎一致

  ```java
  		@Test
      void testString(){
          String result = jedis.set("name","xxxz");
          System.out.println(result);
          String name = jedis.get("name");
          System.out.println(name);
      }
  
      @Test
      void testHash(){
          jedis.hset("user:1","name","Jack");
          jedis.hset("user:1","agr","18");
          Map<String,String> map = jedis.hgetAll("user:1");
          System.out.println(map);
      }
  
  ```

- ###### 使用结束后关闭jedis的连接

  ```java
  		@AfterEach
      void rearDown(){
          if(jedis != null){
              jedis.close();
          }
      }
  ```

##### 2.Jedis的连接池

###### Jedis本身是线程不安全的，频繁创建、销毁、连接会有性能损耗，因此Jedis连接池代替Jedis的直连方式更优

###### 饿汉模式

```java
public class JedisConnectionFactory {
    private static final JedisPool jedisPool;
    static {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        //资源池中的最大连接数    默认8
        poolConfig.setMaxTotal(8);
        //资源池允许的最大空闲连接数 默认8
        poolConfig.setMaxIdle(8);
        //资源池确保的最少空闲连接数 默认0
        poolConfig.setMinIdle(0);
        //当资源池连接用尽后，调用者的最大等待时间（单位为毫秒）。  默认-1 永不超时
        poolConfig.setMaxWaitMillis(1000);
        jedisPool = new JedisPool(poolConfig,
                "127.0.0.1",6379,1000,"951026");
    }
    public static Jedis getJedis(){
        return jedisPool.getResource();
    }
}

```

##### 3.SpringDataRedis
