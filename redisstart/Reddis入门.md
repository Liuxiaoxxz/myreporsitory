

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

![String的存储格式](/Users/zhangliuxiao/Documents/myrepository/redisstart/assets/String的存储格式.png)

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

![Hash的存储格式](/Users/zhangliuxiao/Documents/myrepository/redisstart/assets/Hash的存储格式.png)

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

![redis的java客户端](/Users/zhangliuxiao/Documents/myrepository/redisstart/assets/redis的java客户端.png)

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

###### SpringDataRedis 的使用

- 引入依赖

```xml
<!--SpringDataRedis-->
<dependency>
	<groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
<!--common-pool-->
<dependency>
   <groupId>org.apache.commons</groupId>
   <artifactId>commons-pool2</artifactId>
</dependency>
```

- 注入RedisTemplate

​	该方法存在缺陷：

​	底层调用了默认的 JDK 的序列化方式，把Java对象转为字节，序列化结为"\xac\xed\x00\x05t\x00name"

​	缺陷：可读性差、内存占用大，因此我们需要改变 RedisTemplate 的序列化方式

```java
@SpringBootTest
class RedisDemoApplicationTests {
    @Autowired
    private RedisTemplate redisTemplate;
    @Test
    void contextLoads() {
        //写入一条String数据
        redisTemplate.opsForValue().set("name","小张");
        //获取String数据
        Object name = redisTemplate.opsForValue().get("name");
        System.out.println("name = "+name);
    }
}
```

###### ×修改 RedisTemplate 的序列化方式

```java
@Configuration
public class RedisConfig {
    @Bean
    public RedisTemplate<String,Object> redisTemplate(RedisConnectionFactory connectionFactory){
        //创建RedisTemplate对象
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        //设置连接工厂
        template.setConnectionFactory(connectionFactory);
        //创建JSON序列化工具
        GenericJackson2JsonRedisSerializer jsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
        //设置key的序列化
        template.setKeySerializer(RedisSerializer.string());
        template.setHashKeySerializer(RedisSerializer.string());
        //设置value的序列化
        template.setValueSerializer(jsonRedisSerializer);
        template.setHashValueSerializer(jsonRedisSerializer);
        //返回
        return template;
    }
}
```

> 经过修改后的  RedisTemplate 的序列化结果如下：
>

> ​	在修改后的序列化方式中加入了class的属性，正是这个属性，在序列化与反序列化时我们更加方便，但
> 是，该字段占用了太大的内存，因此为了节省内存，我们不会使用Json序列化器来处理value，而是统一使用
> String序列化器，要求只能存吃String类型的key和value。存储Java对象时，采取手动完成对象的序列化与反序列化。

###### <img src="/Users/zhangliuxiao/Documents/myrepository/redisstart/assets/RedisTemplate修改后的序列化结果.png" alt="RedisTemplate修改后的序列化结果" style="zoom:50%;" />

###### 修改 RedisTemplate 的序列化方式

> Spring 提供了StringRedisTemplate 类，它的 Key 和 Value的序列化方式默认就是 String 方式

```java
@SpringBootTest
public class RedisStringTests {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    void testString(){
        stringRedisTemplate.opsForValue().set("name","虎哥");
        Object name = stringRedisTemplate.opsForValue().get("name");
        System.out.println("name = "+name);
    }

    @Test
    void testSaveUser() throws JsonProcessingException {
        User user = new User("testUser", 28);
        String json = mapper.writeValueAsString(user);
        stringRedisTemplate.opsForValue().set("user:100",json);
        String jsonUser= stringRedisTemplate.opsForValue().get("user:100");
        User user1 = mapper.readValue(jsonUser,User.class);
        System.out.println(user1);
    }
}
```

> ###### private static final ObjectMapper mapper = new ObjectMapper();
>
> 注：常用的对象转 JSON 的方法

##### 基于 Redis 的短信验证功能的实现

###### 1.生成 CODE 并将 CODE 存入 Redis 中 ：

> - 校验手机号格式是否正确
>
> - 利用随机数方法生成一条 指定位数x 位的验证码
>
> - 将验证码以 string 类型的方式插入 Redis 中
>
>   KEY ：业务名+phone，VALUE：CODE，这样每一个用户所对应的 KEY 都唯一
>
> - 将生成 CODE 的结果返回给前端 

```java
 public Result sendCode(String phone, HttpSession session) {
        //TODO 1
        if (RegexUtils.isPhoneInvalid(phone)){
            return Result.fail("手机号格式错误");
        }
        //TODO 2
        String code = RandomUtil.randomNumbers(6);
        //TODO 3
        stringRedisTemplate.opsForValue()
          .set(LOGIN_CODE_KEY+phone,code,LOGIN_CODE_TTL, TimeUnit.MINUTES);

        log.debug("发送短信验证码成功{"+code+"}");
        //TODO 4
        return Result.ok();
    }
```

###### 2.校验用户信息以及用户输入的验证码是否匹配

> - 校验手机号格式是否正确
>
> - 从 Redis 中获取 系统生成的CODE
>
> - 从前端返回的结果中获取 用户输入CODE
>
> - 校验 CODE 是否匹配
>
> - 根据 phone 从数据库中获取用户信息，如果 数据库查询结果为空 则该用户未注册，自动创建用户
>
> - 使用 UUID 随机生成一个 TOKEN
>
> - 防止隐私泄露使用 UserDTO 来截取 User中的部分信息此功能通过BeanUtil.copyProperties方法实现
>
> - 将 TOKEN 以及 UserDTO 以Hash类型存入 redis中，为了减少频繁与 redis 交互，使用BeanUtil.breanToMap方法将 UserDTO 转化为 Map 类型
>
>   ###### 此处有坑：JSON序列化导致Long类型被搞成Integer经典巨坑
>
>   我们采取在 BeanUtil.breanToMap 中设置以下方法将 VALUE 的类型转化为string存储
>
>   setFieldValueEditor((fieldName,fieldValue)->fieldValue.toString()))
>
> - 在 Redis 中设置 TOOKEN 有效期
>
> - 将 TOKEN 返回给前端

```java
 public Result login(LoginFormDTO loginForm,HttpSession session) {
        String phone = loginForm.getPhone();
        if(RegexUtils.isPhoneInvalid(phone)){
            return Result.fail("手机号格式错误");
        }
        Object cacheCode =
          stringRedisTemplate.opsForValue().get(LOGIN_CODE_KEY+phone);
        String code = loginForm.getCode();
        if(cacheCode == null || !cacheCode.toString().equals(code)){
            return Result.fail("验证码错误");
        }
        //根据手机号查询用户
        User user = query().eq("phone",phone).one();
        if(user == null){
            user = createUserWith(phone);
        }
        String token = UUID.randomUUID().toString(true);

        UserDTO userDTO = BeanUtil.copyProperties(user,UserDTO.class);
   			/**********************相当重要的一步操作**********************************/
        Map<String, Object> usermap = BeanUtil.beanToMap(userDTO,new HashMap<>(), 
                CopyOptions.create().setIgnoreNullValue(true)
                .setFieldValueEditor
                        ((fieldName,fieldValue)->fieldValue.toString()));
        stringRedisTemplate.opsForHash()
                .putAll(LOGIN_USER_KEY+token,usermap);
        stringRedisTemplate
                .expire(LOGIN_USER_KEY+token,LOGIN_USER_TTL,TimeUnit.MINUTES);
        return Result.ok(token);
    }

private User createUserWith(String phone) {
        User user = new User();
        user.setPhone(phone);
        user.setNickName(USER_NICK_NAME_PREFIX+RandomUtil.randomString(10));
        save(user);
        return user;
    }
```

###### 3.根据用户的访问来延长 TOKEN 有效期

> 通过设置两层拦截器来实现


#### 4.缓存

> ==缓存== 就是数据交换的缓冲区，是存储数据的临时地方，一般读写性能较高

<img src="/Users/zhangliuxiao/Documents/myrepository/redisstart/assets/缓存的利弊.png" alt="缓存的利弊" style="zoom: 33%;" align="left"/>



##### 缓存更新策略：

<img src="/Users/zhangliuxiao/Documents/myrepository/redisstart/assets/缓存更新策略.png" alt="缓存更新策略" style="zoom:50%;" />

业务场景：

- 低一致性需求：使用内存淘汰机制。例如店铺类型的查询
- 高一致性需求：主动更新，并以超时剔除作为兜底方案。例如店铺详情查询

###### 主动更新策略：

###### 1.Cache Aside Pattern：由缓存的调用者，在更新数据库的同时更新缓存

- 删除缓存还是更新缓存

​	删除缓存：更新数据库时让缓存失效，查询时再次更新缓存

- 如何保证缓存与数据库的操作同时成功或失败

​	单体系统，将缓存与数据库操作放在一个事务

​	分布式系统：利用 TCC 等分布式事务方案

- 先操作缓存还是先操作数据库

​	先删除缓存，再操作数据库

###### 			先操作数据库，再删除缓存



2.Read/Write ThroughPattern：缓存与数据库整合为一个服务，由服务来维护一致性。调用者调用该服务，无

需关心缓存一致性问题。

3.Write Behind CachingPattern：调用者只操作缓存，由其它线程异步的将缓存数据持久化到数据库，保证最终一致



##### 缓存穿透

​	客户端请求的数据在缓存中和数据库中都不存在，这样缓存永远都不会生效，这些请求都会打导数据库

###### 解决方案：

- 缓存空对象

​		优点：实现简单，维护方便

​		缺点：额外的内存消耗（通过设置TTL可以缓解）

​			    短期的不一致性（通过更新数据库后更新缓存可以缓解）

- 布隆过滤器：添加到客户端与 Redis 之间（存在误判风险、有一定穿透风险）
- 增强 ID 的复杂度，避免被猜测 ID 规律

```java
 public Result queryById(Long id) {
        String key = CACHE_SHOP_KEY + id;
        //从Redis查缓存
        String shopJson = stringRedisTemplate.opsForValue().get(key);
				//判断字符串是否为空串（null or ""）
        if(StrUtil.isNotBlank(shopJson)){
            Shop shop = JSONUtil.toBean(shopJson,Shop.class);
            return Result.ok(shop);
        }
   			//如果shopJson不为null，则意味着从Redis中查询到了shop但shop信息为空
        if(shopJson != null){
            return Result.fail("店铺不存在");
        }
        //不存在，根据ID查询数据库
        Shop shop = getById(id);

        if(shop == null){
            stringRedisTemplate.opsForValue().set(key,"",CACHE_NULL_TTL,TimeUnit.MINUTES);
            return Result.fail("店铺不存在");
        }
        stringRedisTemplate.opsForValue().set(key,JSONUtil.toJsonStr(shop),CACHE_SHOP_TTL, TimeUnit.MINUTES);

        return Result.ok(shop);
    }
```

​	

<img src="/Users/zhangliuxiao/Documents/myrepository/redisstart/assets/缓存穿透.png" alt="缓存穿透" style="zoom:50%;" />

##### 缓存雪崩

​	在同一时段内大量的缓存 KEY 同时失效或者 Redis 服务宕机，导致大量请求到达数据库，带来巨大压力

###### 解决方案：

- 给不同的 KEY 的 TTL 添加随机值
- 利用 Redis 集群提高服务的可用性
- 给缓存业务添加降级限流策略
- 给业务添加多级缓存

##### 缓存击穿 

​	缓存击穿问题也叫热点 KEY 问题，就是一个被高并发访问并且缓存重建业务较复杂的KEY突然失效了，

无数的请求会在瞬间给数据库带来巨大的冲击<img src="/Users/zhangliuxiao/Documents/myrepository/redisstart/assets/缓存击穿原理图.png" alt="缓存击穿原理图" style="zoom: 67%;" />

###### 解决方案：

- 互斥锁：
<img src="/Users/zhangliuxiao/Documents/myrepository/redisstart/assets/缓存击穿-互斥锁策略.png" alt="缓存击穿-互斥锁策略" align="left" style="zoom: 25%;" />

















​	优点：

​		没有额外的内存消耗

​		保证一致性

​		实现简单

​	缺点：

​		线程需要等待，性能受影响

​		可能有死锁风险

![缓存击穿-互斥锁流程图](/Users/zhangliuxiao/Documents/myrepository/redisstart/assets/缓存击穿-互斥锁流程图.png)

###### 实现：利用 Redis 的 SETNX 操作设计锁

```java
private boolean trylock(String key){
        Boolean flag = stringRedisTemplate.opsForValue()
          																.setIfAbsent(key,"1",10,TimeUnit.SECONDS);
        return BooleanUtil.isTrue(flag);
}

private void unlock(String key){
        stringRedisTemplate.delete(key);
}
//没有获取到锁后 return 递归 ，unlock()会在递归 return 之前释放
public Shop queryWithMutex(Long id){
        String key = CACHE_SHOP_KEY + id;
        //从Redis查缓存
        String shopJson = stringRedisTemplate.opsForValue().get(key);

        if(StrUtil.isNotBlank(shopJson)){
            Shop shop = JSONUtil.toBean(shopJson,Shop.class);
            return shop;
        }
        if(shopJson != null){
            return null;
        }
        String lockkey = LOCK_SHOP_KEY + id;
        //获取互斥锁
        Shop shop = null;
        try {
            boolean isLock = trylock(lockkey);
            if(!isLock){
                Thread.sleep(50);
                return queryWithMutex(id);
            }
            //不存在，根据ID查询数据库
            shop = getById(id);

            if(shop == null){
                stringRedisTemplate.opsForValue()
                  					.set(key,"",CACHE_NULL_TTL,TimeUnit.MINUTES);
                return null;
            }
         		stringRedisTemplate.opsForValue()
              	.set(key,JSONUtil.toJsonStr(shop),CACHE_SHOP_TTL, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            unlock(lockkey);
        }

        return shop;
    }

```





- 逻辑过期：
	<img src="/Users/zhangliuxiao/Documents/myrepository/redisstart/assets/缓存击穿-逻辑过期.png" alt="缓存击穿-逻辑过期" align="left" style="zoom: 67%;" />























​	优点：

​		线程无需等待，性能较好

​	缺点：
​		不保证一致性

​		有额外内存消耗

​		实现复杂

<img src="/Users/zhangliuxiao/Documents/myrepository/redisstart/assets/缓存击穿-逻辑过期流程图.png" alt="缓存击穿-逻辑过期流程图" align="left" style="zoom: 67%;" />

```java
private void saveShop2Redis(Long id,Long expireSec){
        //从数据库查询店铺数据
        Shop shop = getById(id);
        //封装逻辑过期时间
        RedisData redisData = new RedisData();
        redisData.setData(shop);
        redisData.setExpireTime(LocalDateTime.now().plusSeconds(expireSec));

        stringRedisTemplate.opsForValue()
          							.set(CACHE_SHOP_KEY+id,JSONUtil.toJsonStr(redisData));

}
private static final ExecutorService CACHE_REBUILD_EXCUTOR = 									       																						Executors.newFixedThreadPool(10);
    public Shop querryWithLogicalExpire(Long id){
        String key = CACHE_SHOP_KEY + id;
        //从Redis查缓存
        String shopJson = stringRedisTemplate.opsForValue().get(key);
        //这里为空代表不存在该热点key
        if(StrUtil.isNotBlank(shopJson)){
            return null;
        }
        //命中先把JSON反序列化为对象
        RedisData redisData = JSONUtil.toBean(shopJson, RedisData.class);
        Shop shop = JSONUtil.toBean((JSONObject) redisData.getData(),Shop.class);
        LocalDateTime expireTime = redisData.getExpireTime();
        if(expireTime.isAfter(LocalDateTime.now())){
            return shop;
        }
        //过期
        String lockkey = LOCK_SHOP_KEY + id;
        //获取互斥锁
        boolean isLock = trylock(lockkey);
        if(isLock){

                CACHE_REBUILD_EXCUTOR.submit(() -> {
                    try{
                    this.saveShop2Redis(id,20L);
                    }catch (Exception e) {
                        throw new RuntimeException(e);
                    }finally {
                        unlock(lockkey);
                    }
                });
        }

        return shop;
    }
```

##### 缓存工具封装：

- 方法1：将任意Java对象序列化为json并存储在string类型的key中，并且可以设置TTL过期时间
- 方法2：将任意lava对象序列化为json并存储在string类型的key中，并且可以设置逻辑过期时间，用于处理缓存击穿问题
- 方法3：根据指定的key查询缓存，并反序列化为指定类型，利用缓存空值的方式解决缓存穿透问题
- 方法4：根据指定的key查询缓存，并反序列化为指定类型，需要利用逻辑过期解决缓存击穿问题

```java
@Slf4j
@Component
public class CacheClient {
    private final StringRedisTemplate stringRedisTemplate;

    public CacheClient(StringRedisTemplate stringRedisTemplate){
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public void set(String key, Object value, Long time, TimeUnit unit){
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(value),time,unit);
    }

    public void setWithLogicalExpire(String key, Object value, Long time, TimeUnit unit){
        RedisData redisData = new RedisData();
        redisData.setData(value);
        redisData.setExpireTime(LocalDateTime.now().plusSeconds(unit.toSeconds(time)));
        stringRedisTemplate.opsForValue().set(key,JSONUtil.toJsonStr(redisData));
    }

    public <R,ID> R queryWithPassThrough(String keyPrefix, ID id, Class<R> type, Function<ID,R>dbFallback,Long time, TimeUnit unit){
        String key = keyPrefix + id;
        //从Redis查缓存
        String json = stringRedisTemplate.opsForValue().get(key);

        if(StrUtil.isNotBlank(json)){
            return JSONUtil.toBean(json,type);
        }

        if(json != null){
            return null;
        }
        //不存在，根据ID查询数据库
        R r = dbFallback.apply(id);

        if(r == null){
            stringRedisTemplate.opsForValue().set(key,"",CACHE_NULL_TTL,TimeUnit.MINUTES);
            return null;
        }
        this.set(key,r,time,unit);

        return r;
    }

    private static final ExecutorService CACHE_REBUILD_EXCUTOR = Executors.newFixedThreadPool(10);
    public <R,ID> R querryWithLogicalExpire(String keyPrefix,ID id, Class<R> type, Function<ID,R>dbFallback,Long time, TimeUnit unit){
        String key = keyPrefix + id;
        //从Redis查缓存
        String json = stringRedisTemplate.opsForValue().get(key);
        //这里为空代表不存在该热点key
        if(StrUtil.isNotBlank(json)){
            return null;
        }
        //命中先把JSON反序列化为对象
        RedisData redisData = JSONUtil.toBean(json, RedisData.class);
        R r = JSONUtil.toBean((JSONObject) redisData.getData(),type);
        LocalDateTime expireTime = redisData.getExpireTime();
        if(expireTime.isAfter(LocalDateTime.now())){
            return r;
        }
        //过期
        String lockkey = LOCK_SHOP_KEY + id;
        //获取互斥锁
        boolean isLock = trylock(lockkey);
        if(isLock){

            CACHE_REBUILD_EXCUTOR.submit(() -> {
                try{
                    R r1 = dbFallback.apply(id);
                    this.setWithLogicalExpire(key,r1,time,TimeUnit.MINUTES);
                }catch (Exception e) {
                    throw new RuntimeException(e);
                }finally {
                    unlock(lockkey);
                }
            });
        }

        return r;
    }
    private boolean trylock(String key){
        Boolean flag = stringRedisTemplate.opsForValue().setIfAbsent(key,"1",10,TimeUnit.SECONDS);
        return BooleanUtil.isTrue(flag);
    }
    private void unlock(String key){
        stringRedisTemplate.delete(key);
    }
    
}

```









