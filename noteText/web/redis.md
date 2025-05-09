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