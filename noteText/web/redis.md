[参考博客](http://open8gu.com)
# 缓存穿透
缓存穿透是指由于请求没有办法命中缓存，因此就会直接打到数据库，当请求量较大时，大量的请求就可能会直接把数据库打挂。
通常情况下，缓存是为了提高数据访问速度，避免频繁查询数据库。但如果攻击者故意请求缓存中不存在的数据，就会导致缓存不命中，请求直接访问数据库
缓存穿透一般有几种解决方案：
1. 空对象值缓存
当查询结果为空时，也将结果进行缓存，但是设置一个较短的过期时间。这样在接下来的一段时间内，如果再次请求相同的数据，就可以直接从缓存中获取，而不是再次访问数据库，可以一定程度上解决缓存穿透问题。
这种方式是比较简单的一种实现方案，会存在一些弊端。那就是当短时间内存在大量恶意请求，缓存系统会存在大量的内存占用。如果要解决这种海量恶意请求带来的内存占用问题，需要搭配一套风控系统，对用户请求缓存不存在数据进行统计，进而封禁用户。整体设计就较为复杂，不推荐使用。
2. 使用锁
当请求发现缓存不存在时，可以使用锁机制来避免多个相同的请求同时访问数据库，只让一个请求去加载数据，其他请求等待。
这种方式可以解决数据库压力过大问题，如果会出现“误杀”现象，那就是如果缓存中不存在但是数据库存在这种情况，也会等待获取锁，用户等待时间过长，不推荐使用。
3. 布隆过滤器
布隆过滤器是一种数据结构，可以用于判断一个元素是否存在于一个集合中。它可以在很大程度上减轻缓存穿透问题，因为它可以快速判断一个数据是否可能存在于缓存中。
这种方式较为推荐，可以将所有存量数据全部放入布隆过滤器，然后如果缓存中不存在数据，紧接着判断布隆过滤器是否存在，如果存在访问数据库请求数据，如果不存在直接返回错误响应即可。
但是这种问题还是会有一些小概率问题，那就是如果使用一种小概率误判的缓存进行攻击，依然会对数据库造成比较大的压力。
4. 布隆过滤器+空对象+分布式锁
上面的这些方案或多或少都会有些问题，应该用三者进行组合用来解决缓存穿透问题。
如果说缓存不存在，那么就通过布隆过滤器进行初步筛选，然后判断是否存在缓存空值，如果存在直接返回失败。如果不存在缓存空值，使用锁机制避免多个相同请求同时访问数据库。最后，如果请求数据库为空，那么将为空的 Key 进行空对象值缓存。
以下是一个简单的实现代码模板：
```java
public notice getNotice(int noticeId){
    String redisKey = "notice:" + noticeId;
    //先用布隆过滤器判断：是否“可能存在”
    if (!bloomFilter.mightContain(noticeId)) {
        return null;
    }
    // 尝试获取缓存
    notice cacheNotice = redisStringTemplate.getObject(redisKey, notice.class);
    if (cacheNotice != null) {
        return cacheNotice;
    }
    // 分布式锁：防止缓存击穿
    String lockKey = "lock:notice:" + noticeId;
    boolean locked = stringRedisTemplate.opsForValue().setIfAbsent(lockKey, "1", 10, TimeUnit.SECONDS);
    if (!locked) {
        // 拿不到锁说明其他线程在查，短暂休眠再重试
        try { Thread.sleep(100); } catch (InterruptedException ignored) {}
        return redisStringTemplate.getObject(redisKey, notice.class); // 再尝试获取一次缓存
    }
    try {
        //查数据库
        notice dbNotice = noticeDao.findByNoticeId(noticeId);
        if (dbNotice != null) {
            redisStringTemplate.setObjectAndDeadTime(redisKey, dbNotice, 30, TimeUnit.MINUTES);
            return dbNotice;
        } else {
            //缓存空对象，防止穿透
            redisStringTemplate.setObjectAndDeadTime(redisKey, null, 5, TimeUnit.MINUTES);
            return null;
        }
    } finally {
        redisCommonTemplate.deleteKey(lockKey);
    }
}
//其中分布式锁部分用的setIfAbsent，是redis的setnx命令，意思是如果不存在就设置，如果存在就不设置。但这样需要手动释放锁，还有很多可能出现的bug，当然你写的好不会，但以访万一推荐使用Redisson框架，它封装了很多分布式锁的操作，使用起来更加方便，如下
@Autowired
private RedissonClient redissonClient;
public void getNotice(int noticeId) {
    //略其他代码，这里只写分布式锁部分
    String lockKey = "lock:notice:" + noticeId;
    RLock lock = redissonClient.getLock(lockKey);
    try {
        boolean success = lock.tryLock(0, 10, TimeUnit.SECONDS); // 最多等0秒，加锁10秒
        if (!success) {
            // 拿不到锁，说明有人在处理
            return;
        }
        // 执行业务：查 DB + 缓存
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
    } finally {
        if (lock.isHeldByCurrentThread()) {
            lock.unlock(); // 安全释放
        }
    }
}
```
## uuid和雪花算法
由于redis无法自增，故无法使用数据库的自增id，此时可以使用uuid或者雪花算法。这里uuid和雪花算法**都是字符串**，所以我们对v用到了redis或其他缓存数据库的实体类设计Id用String，另外，即使 ***用不到id也应该设计成包装类而不是int因为如果用jpa会出现空值时默认映射为0为不是空值。***下面介绍uuid和雪花算法。
### UUID
Java 中的 UUID 是一个 128 位（16 字节）的值，由高位和低位组成，各64位。它的标准字符串格式是：
***8-4-4-4-12*** 的 32 个十六进制数字，示例`f47ac10b-58cc-4372-a567-0e02b2c3d479`
- UUID 的生成方式有多种，Java 中最常用的是 `UUID.randomUUID()` 方法，它会生成一个随机的 UUID。
- 还可以用一个字符串来解析出一个 UUID，如`UUID uuid = UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479");`，注意要符合格式
但UUID位数过长，所以对于一些对性能有更高要求得我们一般用雪花算法。
### 雪花算法
雪花算法生成得ID为64位，结构为`| 1位符号位 | 41位时间戳 | 10位机器标识 | 12位序列号 |`
- 符号位：固定为0，因为生成的ID都是正数。
- 时间戳：41位时间戳，精确到毫秒，最多可以使用69年。
- 机器标识：5 位数据中心 ID（最多 32 个数据中心），5 位机器 ID（每个数据中心最多 32 台机器）
- 序列号：12位序列号，用于同一毫秒内生成多个ID，最多可以生成4096个ID，如果同一毫秒内生成的ID超过4096个，就会等待下一个毫秒。
一下一个简单实现代码
```java
public class SnowflakeIdGenerator {
    private final long epoch = 1609459200000L; // 自定义起始时间 2021-01-01
    private final long datacenterId;
    private final long workerId;
    private final long sequenceBits = 12L;
    private final long workerIdBits = 5L;
    private final long datacenterIdBits = 5L;
    private final long maxWorkerId = ~(-1L << workerIdBits); // 31
    private final long maxDatacenterId = ~(-1L << datacenterIdBits); // 31

    private final long workerIdShift = sequenceBits;
    private final long datacenterIdShift = sequenceBits + workerIdBits;
    private final long timestampShift = sequenceBits + workerIdBits + datacenterIdBits;

    private final long sequenceMask = ~(-1L << sequenceBits);

    private long sequence = 0L;
    private long lastTimestamp = -1L;

    public SnowflakeIdGenerator(long datacenterId, long workerId) {
        if (workerId > maxWorkerId || workerId < 0)
            throw new IllegalArgumentException("workerId out of range");
        if (datacenterId > maxDatacenterId || datacenterId < 0)
            throw new IllegalArgumentException("datacenterId out of range");
        this.datacenterId = datacenterId;
        this.workerId = workerId;
    }

    public synchronized long nextId() {
        long timestamp = currentTime();
        if (timestamp < lastTimestamp)
            throw new RuntimeException("Clock moved backwards");

        if (timestamp == lastTimestamp) {
            sequence = (sequence + 1) & sequenceMask;
            if (sequence == 0)
                timestamp = waitNextMillis(lastTimestamp);
        } else {
            sequence = 0L;
        }

        lastTimestamp = timestamp;

        return ((timestamp - epoch) << timestampShift)
                | (datacenterId << datacenterIdShift)
                | (workerId << workerIdShift)
                | sequence;
    }

    private long waitNextMillis(long lastTimestamp) {
        long timestamp = currentTime();
        while (timestamp <= lastTimestamp) {
            timestamp = currentTime();
        }
        return timestamp;
    }

    private long currentTime() {
        return System.currentTimeMillis();
    }
}
//SpringBoot这样封装
@Component
public class SnowflakeIdGenerator {

    // ================= 配置 =================

    // 起始时间戳（建议固定不变）
    private final long epoch = 1609459200000L; // 2021-01-01

    // 每一部分占用的位数
    private final long datacenterIdBits = 5L;
    private final long workerIdBits = 5L;
    private final long sequenceBits = 12L;

    // 最大值
    private final long maxDatacenterId = ~(-1L << datacenterIdBits); // 31
    private final long maxWorkerId = ~(-1L << workerIdBits); // 31

    // 位移
    private final long workerIdShift = sequenceBits;
    private final long datacenterIdShift = sequenceBits + workerIdBits;
    private final long timestampShift = sequenceBits + workerIdBits + datacenterIdBits;

    private final long sequenceMask = ~(-1L << sequenceBits);

    // ================= 实例变量 =================
    private long datacenterId;
    private long workerId;
    private long sequence = 0L;
    private long lastTimestamp = -1L;

    // ================= 构造 =================

    public SnowflakeIdGenerator(
            @Value("${snowflake.datacenter-id:1}") long datacenterId,
            @Value("${snowflake.worker-id:1}") long workerId) {

        if (datacenterId > maxDatacenterId || datacenterId < 0)
            throw new IllegalArgumentException("Datacenter ID out of range");
        if (workerId > maxWorkerId || workerId < 0)
            throw new IllegalArgumentException("Worker ID out of range");

        this.datacenterId = datacenterId;
        this.workerId = workerId;
    }

    // ================= 主方法 =================

    public synchronized long nextId() {
        long timestamp = timeGen();

        if (timestamp < lastTimestamp) {
            throw new RuntimeException("Clock moved backwards. Refusing to generate ID.");
        }

        if (timestamp == lastTimestamp) {
            sequence = (sequence + 1) & sequenceMask;
            if (sequence == 0) {
                timestamp = waitNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0L;
        }

        lastTimestamp = timestamp;

        return ((timestamp - epoch) << timestampShift)
                | (datacenterId << datacenterIdShift)
                | (workerId << workerIdShift)
                | sequence;
    }

    private long waitNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    private long timeGen() {
        return System.currentTimeMillis();
    }
}
//然后application.yml配置
snowflake:
  datacenter-id: 1
  worker-id: 2
```
也有很多工具类库可以使用，如hutool的IdUtil、百度的UidGenerator、美团的Leaf等。
1. Hutool: 引入依赖即可`cn.hutool.hutool-core`,使用如
```java
Snowflake snowflake = IdUtil.getSnowflake(1, 1); // datacenterId=1, workerId=1
long id = snowflake.nextId();
```
2. 美团Leaf: [GitHub地址](https://github.com/Meituan-Dianping/Leaf)
3. 百度UidGenerator：[GitHub地址](https://github.com/baidu/uid-generator)